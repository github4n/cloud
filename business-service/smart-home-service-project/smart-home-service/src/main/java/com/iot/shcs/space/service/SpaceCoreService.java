package com.iot.shcs.space.service;

import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.utils.DescDeviceVoComparator;
import com.iot.shcs.scene.exception.SpaceCoreExceptionEnum;
import com.iot.shcs.space.exception.SpaceExceptionEnum;
import com.iot.shcs.space.util.BeanCopyUtil;
import com.iot.shcs.space.vo.DeviceVo;
import com.iot.shcs.space.vo.SpaceDeviceResp;
import com.iot.shcs.space.vo.SpaceDeviceVo;
import com.iot.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iot.shcs.device.utils.DeviceConstants.DEFAULT_SPACE;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:18 2018/5/10
 * @Modify by:
 */
@Service
public class SpaceCoreService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpaceCoreService.class);

    @Autowired
    private ISpaceService spaceService;
    @Autowired
    private ISpaceDeviceService spaceDeviceService;

    @Autowired
    private DeviceCoreService deviceCoreService;


    public void addSpaceDeviceByUserId(Long tenantId, Long spaceId, String deviceId) {
        LOGGER.info("addSpaceDeviceByUserId========{},{},{}", spaceId, deviceId, tenantId);
        Map<String, Object> paramMap = Maps.newHashMap();
        com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq = new com.iot.shcs.space.vo.SpaceDeviceReq();
        spaceDeviceReq.setDeviceId(deviceId);
        spaceDeviceReq.setSpaceId(spaceId);
        spaceDeviceReq.setTenantId(tenantId);
        Integer res = spaceDeviceService.countSpaceDeviceByCondition(spaceDeviceReq);
        com.iot.shcs.space.vo.SpaceResp space = spaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
        if (space == null) {
            throw new BusinessException(SpaceExceptionEnum.SPACE_IS_NULL);
        }

        if (res == 0) {
            //检查是否已经添加过到对应的房间
            com.iot.shcs.space.vo.SpaceDeviceReq spaceDevice = new com.iot.shcs.space.vo.SpaceDeviceReq();
            spaceDevice.setTenantId(tenantId);
            spaceDevice.setSpaceId(spaceId);
            spaceDevice.setDeviceId(deviceId);
            //TODO res 为空已经表明数据为空，是否还有必要在检查一次
            List<com.iot.shcs.space.vo.SpaceDeviceResp> resps = spaceDeviceService.findSpaceDeviceByCondition(spaceDevice);
            if (CollectionUtils.isEmpty(resps)) {
                com.iot.shcs.space.vo.SpaceDeviceReq req = new com.iot.shcs.space.vo.SpaceDeviceReq();
                req.setTenantId(tenantId);
                req.setDeviceId(deviceId);
                req.setSpaceId(spaceId);
                req.setLocationId(spaceId);
                req.setCreateTime(new Date());
                req.setUpdateTime(new Date());
                spaceDeviceService.inserSpaceDevice(req);
            } else {
                com.iot.shcs.space.vo.SpaceDeviceResp resp = resps.get(0);
                com.iot.shcs.space.vo.SpaceDeviceReq req = BeanCopyUtil.spaceDeviceRespToSpaceDeviceReq(resp);
                if (spaceId.compareTo(req.getSpaceId()) != 0) {
                    req.setSpaceId(spaceId);
                    req.setLocationId(spaceId);
                    req.setUpdateTime(new Date());
                    spaceDeviceService.updateSpaceDevice(req);
                }
            }
        }
        LOGGER.info("spaceDeviceService ok, {}", res);
    }


    public void updateSpaceByUserId(Long userId, Long tenantId, String deviceId, String roomId, String homeId) {
        LOGGER.info("updateSpaceByUserId ====== userId {},tenantId{}, devId{}, roomId{}, homeId{}",userId, tenantId, deviceId, roomId, homeId);
        AssertUtils.notEmpty(homeId, "homeId.notnull");
        com.iot.shcs.space.vo.SpaceDeviceReq spaceDevice = new com.iot.shcs.space.vo.SpaceDeviceReq();
        spaceDevice.setDeviceId(deviceId);
        spaceDevice.setTenantId(tenantId);
        List<SpaceDeviceResp> resps = spaceDeviceService.findSpaceDeviceByCondition(spaceDevice);
//        if(CollectionUtils.isEmpty(resps)){
//            throw new BusinessException(SpaceExceptionEnum.SPACE_DEVICE_NOT_EXIT);
//        }
        if (!"".equals(homeId)) {
            spaceDevice.setLocationId(Long.valueOf(homeId));
        }

        if (DEFAULT_SPACE.equals(roomId)) {
            // // 当网关通知 房间ID为0时，则表示删除房间设备关系，云端也移除房间设备信息(即修改 spaceId)
            spaceDevice.setSpaceId(Long.valueOf(homeId));
        } else {
            if (roomId != null) {
                spaceDevice.setSpaceId(Long.valueOf(roomId));
            } else {
                com.iot.shcs.space.vo.SpaceResp userDefaultSpace = spaceService.findUserDefaultSpace(userId, tenantId);
                if (userDefaultSpace != null) {
                    spaceDevice.setSpaceId(userDefaultSpace.getId());
                }
            }
        }
        List<ListUserDeviceInfoRespVo> userDeviceRelationShipResp = deviceCoreService.listUserDevices(tenantId, userId, deviceId);
        if (CollectionUtils.isEmpty(userDeviceRelationShipResp)) {
            throw new BusinessException(SpaceCoreExceptionEnum.USER_DEVICE_NOT_EXIST);
        }
        if(CollectionUtils.isEmpty(resps)){
            spaceDeviceService.inserSpaceDevice(spaceDevice);
        }else {
            // 修改房间设备信息
            spaceDevice.setId(resps.get(0).getId());
            spaceDeviceService.updateSpaceDevice(spaceDevice);
        }
    }

    public Map<String, Object> findSpaceDeviceList(Long tenantId, Long userId, String roomId, String homeId) {
        Map<String, Object> resultMap = new HashMap<>();
        String spaceId = roomId;
        //1.获取房间下面的设备
        List<DeviceVo> deviceList;
        int totalCount = 0;
        if (DEFAULT_SPACE.equals(spaceId)) {
            spaceId = homeId;
            // 和前端约定，roomId=0 查找家庭下未挂载设备
            deviceList = spaceService.getUserUnMountDevice(tenantId, Long.valueOf(spaceId), userId,0);
//            totalCount = spaceService.getUserUnMountDeviceCount(Long.valueOf(spaceId));
        } else {
            deviceList = spaceService.getDeviceByUserRoom(tenantId, userId, Long.valueOf(spaceId));
//            totalCount = spaceService.getDeviceCountByUserRoom(userId, Long.valueOf(spaceId));
        }

        List<Map<String, Object>> resultDeviceList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deviceList)) {
            totalCount = deviceList.size();
            //降序排序
            Collections.sort(deviceList, new DescDeviceVoComparator());

            for (DeviceVo deviceVo : deviceList) {
                String onlineStatus = deviceVo.getOnlineStatus();
                Integer online = 1;
                if (onlineStatus != null && OnlineStatusEnum.DISCONNECTED.getCode().equals(onlineStatus)) {
                    online = 0;
                }
                //获取设备属性状态
                Map<String, Object> propertyMap = deviceVo.getStateProperty();
                //propertyMap = deviceStateService.getDeviceStateByDeviceId(deviceVo.getDeviceId());
                Map<String, Object> resultDevice = new HashMap<>();
                resultDevice.put("online", online);
                resultDevice.put("devId", deviceVo.getDeviceId());
                resultDevice.put("parentId", (StringUtils.isEmpty(deviceVo.getParentId()) ? "" : deviceVo.getParentId()));//返回""
                resultDevice.put("name", deviceVo.getName());
                resultDevice.put("productId", deviceVo.getProductId());
                resultDevice.put("homeId", deviceVo.getParentSpaceId());
                resultDevice.put("roomId", deviceVo.getSpaceId());
                resultDevice.put("icon", deviceVo.getIcon());
                resultDevice.put("devType", deviceVo.getDevType());
                resultDevice.put("attr", propertyMap);
                resultDeviceList.add(resultDevice);
            }
        }
        resultMap.put("totalCount", totalCount);
        resultMap.put("dev", resultDeviceList);
        return resultMap;
    }

    public void bindSpaceByDeviceId(String directDeviceId, String subDeviceId, Long tenantId) {
        com.iot.shcs.space.vo.SpaceDeviceResp spaceDeviceResp = spaceService.getSpaceDeviceByDeviceUuid(directDeviceId, tenantId);
        if (spaceDeviceResp != null) {
            Date currentTime = new Date();
            com.iot.shcs.space.vo.SpaceDeviceReq newSpaceDevice = new com.iot.shcs.space.vo.SpaceDeviceReq();
            newSpaceDevice.setCreateTime(currentTime);
            newSpaceDevice.setUpdateTime(currentTime);
            newSpaceDevice.setLocationId(spaceDeviceResp.getLocationId());
            newSpaceDevice.setDeviceId(subDeviceId);
            newSpaceDevice.setSpaceId(spaceDeviceResp.getSpaceId());
            newSpaceDevice.setTenantId(spaceDeviceResp.getTenantId());
            newSpaceDevice.setOrder(1);
//            spaceDeviceService.inserSpaceDevice(newSpaceDevice);
            spaceDeviceService.insertOrUpdateSpaceDeviceByDevId(newSpaceDevice);
        }
    }

    public SpaceDeviceVo getSpaceByDeviceId(String deviceId, Long tenantId) {
        com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo req = new com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo();
        List<String> deviceIds = new ArrayList<>();

        deviceIds.add(deviceId);
        req.setDeviceIds(deviceIds);
        req.setTenantId(tenantId);
        List<com.iot.shcs.space.vo.SpaceDeviceVo> spaceDeviceVos = spaceDeviceService.findSpaceDeviceInfoByDeviceIds(req);
        if (!CollectionUtils.isEmpty(spaceDeviceVos)) {
            return spaceDeviceVos.get(0);
        }
        return null;
    }


    public void saveOrUpdateSpaceDevice(Long tenantId, Long spaceId, String deviceId) {
        com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq = com.iot.shcs.space.vo.SpaceDeviceReq.builder()
                .tenantId(tenantId).deviceId(deviceId).build();
        List<com.iot.shcs.space.vo.SpaceDeviceResp> spaceDeviceResps = spaceDeviceService.findSpaceDeviceByCondition(spaceDeviceReq);
        if (CollectionUtils.isEmpty(spaceDeviceResps)) {
            spaceDeviceReq =  com.iot.shcs.space.vo.SpaceDeviceReq.builder()
                    .tenantId(tenantId).deviceId(deviceId).spaceId(spaceId).locationId(spaceId).build();
            spaceDeviceService.inserSpaceDevice(spaceDeviceReq);
        }else{
            spaceDeviceReq =  com.iot.shcs.space.vo.SpaceDeviceReq.builder()
                    .tenantId(tenantId).deviceId(deviceId).spaceId(spaceId).locationId(spaceId).build();
            spaceDeviceReq.setId(spaceDeviceResps.get(0).getId());
            spaceDeviceService.updateSpaceDevice(spaceDeviceReq);
        }
    }

    public void saveOrUpdateBatchSpaceDevice(Long tenantId, Long spaceId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        //TODO 批量处理
        deviceIds.forEach(deviceId ->{
            this.saveOrUpdateSpaceDevice(tenantId, spaceId, deviceId);
        });
    }
}
