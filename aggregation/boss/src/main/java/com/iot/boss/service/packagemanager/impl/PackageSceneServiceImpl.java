package com.iot.boss.service.packagemanager.impl;

import com.alibaba.fastjson.JSON;
import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.packagemanager.IPackageSceneService;
import com.iot.boss.vo.packagemanager.resp.SceneListResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.SceneInfoApi;
import com.iot.control.packagemanager.enums.SearchTypeEnum;
import com.iot.control.packagemanager.utils.CheckSceneConfigUtil;
import com.iot.control.packagemanager.utils.SceneToBeanUtil;
import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SceneConfigReq;
import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import com.iot.control.packagemanager.vo.resp.scene.SceneDetailInfoResp;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PackageSceneServiceImpl implements IPackageSceneService {
    @Autowired
    private SceneInfoApi sceneInfoApi;
    @Autowired
    private DeviceTypeApi deviceTypeApi;

    // BOSS添加的套包，租户id为-1
    private static Long TENANT_ID = -1L;

    @Override
    public int saveSceneInfo(SaveSceneInfoReq req) {
        // 套包id
        Long packageId = req.getPackageId();
        if (packageId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "packageId is null");
        }
        // 讲获取的json参数转换成对象
        SceneDetailInfoReq sceneDetailInfoReq = req.getDetail();
        SceneConfigReq sceneConfigReq = sceneDetailInfoReq.getConfig();
        if (CheckSceneConfigUtil.checkSceneConfig(sceneConfigReq, SearchTypeEnum.DEVICETYPE.getCode())) {
            SceneInfoSaveReq sceneInfoSaveReq = new SceneInfoSaveReq(req.getPackageId(), TENANT_ID, req.getSceneName(), SaaSContextHolder.getCurrentUserId(),new Date(),null);
            sceneInfoSaveReq.setJson(JsonUtil.toJson(sceneDetailInfoReq));
            return sceneInfoApi.insertSceneInfo(sceneInfoSaveReq);
        }
        return 0;
    }

    /**
     *@description 获取boss下的套包场景详细
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 15:03
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp>
     */
    @Override
    public List<SceneListResp> getSceneList(Long packageId) {
        // 设置返回值
        List<SceneListResp> sceneListResps = new ArrayList<>();
        // 获取套包下的场景信息
        List<SceneInfoResp> sceneInfoResps = sceneInfoApi.getSceneInfoPage(packageId, SaaSContextHolder.currentTenantId());
        boolean first = true;
        Map<Long, String> deviceTypeNameMap = new HashMap<>();
        if (sceneInfoResps != null && !sceneInfoResps.isEmpty()) {
            for(SceneInfoResp sceneInfo : sceneInfoResps) {
                SceneListResp sceneListResp = new SceneListResp(sceneInfo.getId(), sceneInfo.getSceneName());

                List<String> deviceTypeNameList = new ArrayList<>();
                SceneDetailInfoReq sceneDetailInfoReq = JSON.parseObject(sceneInfo.getJson(), SceneDetailInfoReq.class);

                List<Long> deviceTypeIdList = sceneDetailInfoReq.getDevList();
                List<Long> newDeviceTypeIdList = new ArrayList<>();
                deviceTypeIdList.forEach(deviceTypeId -> {
                    String deviceTypeName = deviceTypeNameMap.get(deviceTypeId);
                    if (StringUtil.isBlank(deviceTypeName)) {
                        newDeviceTypeIdList.add(deviceTypeId);
                    } else {
                        deviceTypeNameList.add(deviceTypeName);
                    }
                });
                if (!newDeviceTypeIdList.isEmpty()) {
                    List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getByIds(newDeviceTypeIdList);
                    if (deviceTypeResps != null || !deviceTypeResps.isEmpty()) {
                        deviceTypeResps.forEach(deviceType -> {
                            deviceTypeNameMap.put(deviceType.getId(), deviceType.getName());
                            deviceTypeNameList.add(deviceType.getName());
                        });
                    }
                }
                if (first) {
                    first = false;
                    SceneDetailInfoResp scene = SceneToBeanUtil.sceneToBean(sceneInfo.getJson(), deviceTypeNameMap, SearchTypeEnum.DEVICETYPE.getCode());
                    sceneListResp.setSenceDetailInfoResp(scene);
                }
                sceneListResp.setDeviceTypeNames(deviceTypeNameList);
                sceneListResps.add(sceneListResp);
            }
        }
        return sceneListResps;
    }

    @Override
    public int batchInsertSceneInfo(List<SaveSceneInfoReq> req) {
        if (req != null && req.size() > 0) {
            Long packageId = req.get(0).getPackageId();
            int number = sceneInfoApi.countSceneNumber(packageId, TENANT_ID);
            if ((req.size() + number )> 30) {
                throw new BusinessException(BossExceptionEnum.PACKAGE_SCENE_NUMBER_ERROR);
            }
            List<SceneInfoSaveReq> sceneInfoVoList = new ArrayList<>();
            req.forEach(t-> {
                if (StringUtil.isEmpty(t.getSceneName())) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "sceneName is null");
                }
                if (t.getPackageId() == null) {
                    throw new BusinessException(BossExceptionEnum.PACKAGE_ID_IS_NULL, "package id is null");
                }
                // 讲获取的json参数转换成对象
                SceneDetailInfoReq sceneDetailInfoReq = t.getDetail();
                SceneConfigReq sceneConfigReq = sceneDetailInfoReq.getConfig();
                if (CheckSceneConfigUtil.checkSceneConfig(sceneConfigReq, SearchTypeEnum.DEVICETYPE.getCode())) {
                    SceneInfoSaveReq sceneInfoSaveReq = new SceneInfoSaveReq();
                    sceneInfoSaveReq.setSceneName(t.getSceneName());
                    sceneInfoSaveReq.setTenantId(TENANT_ID);
                    sceneInfoSaveReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
                    sceneInfoSaveReq.setCreateTime(new Date());
                    sceneInfoSaveReq.setJson(JsonUtil.toJson(sceneDetailInfoReq));
                    sceneInfoSaveReq.setPackageId(t.getPackageId());
                    sceneInfoVoList.add(sceneInfoSaveReq);
                }
            });
            return sceneInfoApi.batchInsertSceneInfo(sceneInfoVoList);
        }
        return 0;
    }

    @Override
    public int updateByPrimaryKey(SaveSceneInfoReq req) {
        if (StringUtil.isEmpty(req.getSceneName())) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "sceneName is null");
        }
        if (req.getPackageId() == null) {
            throw new BusinessException(BossExceptionEnum.PACKAGE_ID_IS_NULL, "package id is null");
        }
        if (req.getSceneId() == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "scene id is null");
        }
        // 讲获取的json参数转换成对象
        SceneDetailInfoReq sceneDetailInfoReq = req.getDetail();
        SceneInfoSaveReq sceneInfoSaveReq = null;
        if (sceneDetailInfoReq == null) {
            sceneInfoSaveReq = new SceneInfoSaveReq(req.getSceneId(), req.getSceneName(), SaaSContextHolder.getCurrentUserId(), new Date(), null);
        } else {
            SceneConfigReq sceneConfigReq = sceneDetailInfoReq.getConfig();
            if (CheckSceneConfigUtil.checkSceneConfig(sceneConfigReq, SearchTypeEnum.DEVICETYPE.getCode())) {
                sceneInfoSaveReq = new SceneInfoSaveReq(req.getSceneId(), req.getSceneName(), SaaSContextHolder.getCurrentUserId(), new Date(), JsonUtil.toJson(sceneDetailInfoReq));
            }
        }
        return sceneInfoApi.updateByPrimaryKey(sceneInfoSaveReq);
    }

    @Override
    public int deleteSceneInfoById(Long id) {
        if (id == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "sceneId is null");
        }
       return sceneInfoApi.deleteByIdAndTenantId(id, TENANT_ID);
    }

    /**
     *@description 根据场景id，获取该场景的详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:33
     *@return com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp
     */
    @Override
    public SceneInfoDetailResp getSceneInfoDetailById(Long id) {
        SceneInfoDetailResp sceneInfoDetailResp = null;
        SceneInfoResp sceneInfoResp = sceneInfoApi.getSceneInfoById(id);
        if (sceneInfoResp != null) {
            String json = sceneInfoResp.getJson();
            if (StringUtil.isNotBlank(json)) {
                SceneDetailInfoReq sceneDetailInfoReq = JSON.parseObject(json, SceneDetailInfoReq.class);
                List<Long> devs = sceneDetailInfoReq.getDevList();
                if (devs != null && devs.size() > 0) {
                    sceneInfoDetailResp = new SceneInfoDetailResp();
                    BeanUtil.copyProperties(sceneInfoResp, sceneInfoDetailResp);
                    List<DeviceTypeResp>  packageDeviceNameResps = deviceTypeApi.getDeviceTypeIdAndNameByIds(devs);
                    if (packageDeviceNameResps != null && packageDeviceNameResps.size() > 0) {
                        Map<Long, String> maps = packageDeviceNameResps.stream().collect(Collectors.toMap(DeviceTypeResp::getId, DeviceTypeResp::getName, (k1,k2)->k2));
                        SceneDetailInfoResp newSceneDetailInfoReq = SceneToBeanUtil.sceneToBean(json, maps, SearchTypeEnum.DEVICETYPE.getCode());
                        sceneInfoDetailResp.setSceneDetailInfoResp(newSceneDetailInfoReq);
                    }
                }
            }
        }
        return sceneInfoDetailResp;
    }
}

