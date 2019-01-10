package com.iot.portal.packagemanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.PackageApi;
import com.iot.control.packagemanager.api.SceneInfoApi;
import com.iot.control.packagemanager.enums.SearchTypeEnum;
import com.iot.control.packagemanager.utils.CheckSceneConfigUtil;
import com.iot.control.packagemanager.utils.SceneToBeanUtil;
import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.req.scene.SceneConfigReq;
import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import com.iot.control.packagemanager.vo.resp.scene.SceneDetailInfoResp;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.product.PackageProductNameResp;
import com.iot.portal.exception.PackageExceptionEnum;
import com.iot.portal.packagemanager.service.IPackageSceneService;
import com.iot.portal.packagemanager.vo.resp.SceneListResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: cloud
 * @description: 套包场景service实现
 * @author: yeshiyuan
 * @create: 2018-12-10 14:46
 **/
@Service
public class PackageSceneServiceImpl implements IPackageSceneService{

    @Autowired
    private PackageApi packageApi;

    @Autowired
    private SceneInfoApi sceneInfoApi;

    @Autowired
    private ProductApi productApi;

    private static int sceneMaxNum = 30;

    /**
     * @despriction：保存场景配置
     * @author  yeshiyuan
     * @created 2018/12/10 14:49
     */
    @Override
    public void saveSceneConfig(com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq saveReq) {
        com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq.checkParam(saveReq);
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long packageId = saveReq.getPackageId();
        PackageResp packageResp = packageApi.getPackageById(packageId, SaaSContextHolder.currentTenantId());
        if (packageResp == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST);
        } else if (!tenantId.equals(packageResp.getTenantId())) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_TENANTID_ERROR);
        }
        SceneDetailInfoReq sceneDetailInfoReq = saveReq.getDetail();
        List<Long> productIdList = sceneDetailInfoReq.getDevList();
        if (productIdList == null || productIdList.isEmpty()) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NULL);
        }
        SceneConfigReq sceneConfigReq = sceneDetailInfoReq.getConfig();
        CheckSceneConfigUtil.checkSceneConfig(sceneConfigReq, "product");
        if (saveReq.getSceneId() != null) {
            SceneInfoResp sceneInfoResp = sceneInfoApi.getSceneInfoById(saveReq.getSceneId());
            if (sceneInfoResp == null) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_SCENE_NOT_EXIST);
            } else if (!sceneInfoResp.getPackageId().equals(packageId)) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST);
            }
            SceneInfoSaveReq sceneInfoSaveReq = new SceneInfoSaveReq(saveReq.getSceneId(), saveReq.getSceneName(), SaaSContextHolder.getCurrentUserId(), new Date(), JsonUtil.toJson(sceneDetailInfoReq));
            sceneInfoApi.updateByPrimaryKey(sceneInfoSaveReq);
        } else {
            //插入
            int number = sceneInfoApi.countSceneNumber(saveReq.getPackageId(), tenantId);
            if (number >= sceneMaxNum) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_SCENE_NUM_ERROR, sceneMaxNum);
            }
            SceneInfoSaveReq newSceneInfo = new SceneInfoSaveReq(packageId, tenantId, saveReq.getSceneName(),SaaSContextHolder.getCurrentUserId() , new Date(), null);
            newSceneInfo.setJson(JsonUtil.toJson(sceneDetailInfoReq));
            sceneInfoApi.insertSceneInfo(newSceneInfo);
        }
    }

    /**
     * @despriction：删除场景
     * @author  yeshiyuan
     * @created 2018/12/10 18:57
     */
    @Override
    public void deleteScene(Long sceneId) {
        sceneInfoApi.deleteByIdAndTenantId(sceneId, SaaSContextHolder.currentTenantId());
    }

    /**
     *@description 根据套包id，租户id获取场景信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 9:38
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoResp>
     */
    @Override
    public List<SceneListResp> getSceneListByPackageIdAndTenantId(Long packageId) {
        List<SceneListResp> sceneListResps = new ArrayList<>();
        // 根据套包id获取场景信息
        List<SceneInfoResp> sceneInfoResps = sceneInfoApi.getSceneInfoPage(packageId, SaaSContextHolder.currentTenantId());

        boolean first = true;
        Map<Long, String> productNameMap = new HashMap<>();
        if (sceneInfoResps != null && !sceneInfoResps.isEmpty()) {
             for (SceneInfoResp sceneInfo : sceneInfoResps) {
                SceneListResp sceneListResp = new SceneListResp(sceneInfo.getId(), sceneInfo.getSceneName());
                List<String> productNameList = new ArrayList<>();
                SceneDetailInfoReq sceneDetailInfoReq = JSON.parseObject(sceneInfo.getJson(), SceneDetailInfoReq.class);
                List<Long> productIdList = sceneDetailInfoReq.getDevList();
                List<Long> newProductIdList = new ArrayList<>();
                productIdList.forEach(productId -> {
                    String productName = productNameMap.get(productId);
                    if (StringUtil.isBlank(productName)) {
                        newProductIdList.add(productId);
                    } else {
                        productNameList.add(productName);
                    }
                });
                if (!newProductIdList.isEmpty()) {
                    List<PackageProductNameResp> productResps = productApi.getProductByIds(newProductIdList);
                    if (productResps != null || !productResps.isEmpty()) {
                        productResps.forEach(product -> {
                            productNameMap.put(product.getId(), product.getProductName());
                            productNameList.add(product.getProductName());
                        });
                    }
                }
                if (first) {
                    SceneDetailInfoResp scene = SceneToBeanUtil.sceneToBean(sceneInfo.getJson(), productNameMap, SearchTypeEnum.PRODUCT.getCode());
                    sceneListResp.setSceneDetailInfoResp(scene);
                    first = false;
                }
                sceneListResp.setProductNameList(productNameList);
                sceneListResps.add(sceneListResp);
            }
        }
        return sceneListResps;
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
                    List<PackageProductNameResp> packageProductNameResps = productApi.getProductByIds(devs);
                    if (packageProductNameResps != null && packageProductNameResps.size() > 0) {
                        Map<Long, String> maps = packageProductNameResps.stream().collect(Collectors.toMap(PackageProductNameResp::getId, PackageProductNameResp::getProductName, (k1,k2)->k2));
                        SceneDetailInfoResp newSceneDetailInfoReq = SceneToBeanUtil.sceneToBean(json, maps, SearchTypeEnum.PRODUCT.getCode());
                        sceneInfoDetailResp.setSceneDetailInfoResp(newSceneDetailInfoReq);
                    }
                }
            }
        }
        return sceneInfoDetailResp;
    }
}
