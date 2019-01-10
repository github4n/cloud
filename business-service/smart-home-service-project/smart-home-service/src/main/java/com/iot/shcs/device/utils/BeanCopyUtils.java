package com.iot.shcs.device.utils;

import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:51 2018/6/20
 * @Modify by:
 */
public class BeanCopyUtils {

    public static void copyDevice(ListDeviceInfoRespVo res, DeviceResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setDeviceId(res.getUuid());
            target.setParentId(res.getParentId());
            target.setId(res.getId());
            target.setIsDirectDevice(res.getIsDirectDevice());
            target.setConditional(res.getConditional());
            target.setProductId(res.getProductId());
            target.setBusinessTypeId(res.getBusinessTypeId());
            target.setIcon(res.getIcon());
            target.setIp(res.getIp());
            target.setMac(res.getMac());
            target.setName(res.getName());

            target.setDevModel(res.getDevModel());
            target.setExtraName(res.getExtraName());
            target.setHwVersion(res.getHwVersion());

            target.setLocationId(res.getLocationId());
            target.setRealityId(res.getRealityId());
            target.setResetRandom(res.getResetRandom());
            target.setSn(res.getSn());
            target.setSupplier(res.getSupplier());
            target.setVersion(res.getVersion());
            target.setSsid(res.getSsid());

            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setLastUpdateDate(res.getLastUpdateDate());
        }
    }

    public static void copyDeviceInfoList(List<ListDeviceInfoRespVo> tempList, List<DeviceResp> targetList) {

        if (!CollectionUtils.isEmpty(tempList)) {
            for (ListDeviceInfoRespVo temp : tempList) {
                DeviceResp target = new DeviceResp();
                copyDevice(temp, target);
                targetList.add(target);
            }
        }
    }

    public static void copyUserDeviceList(Map<String, ListUserDeviceInfoRespVo> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            ListUserDeviceInfoRespVo tempData = tempMap.get(target.getDeviceId());
            if (tempData == null) {
                i++;
                continue;
            }
            target.setPassword(tempData.getPassword());
            targetList.set(i, target);
            i++;
        }
    }

    public static void copyDeviceExtendList(Map<String, ListDeviceExtendRespVo> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            ListDeviceExtendRespVo temp = tempMap.get(target.getDeviceId());
            if (temp == null) {
                i++;
                continue;
            }
            target.setP2pId(temp.getP2pId());
            target.setAddress(temp.getAddress());
            targetList.set(i, target);
            i++;
        }
    }


    public static void copyProductList(Map<Long, ListProductRespVo> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getProductId() == null) {
                i++;
                continue;
            }
            ListProductRespVo temp = tempMap.get(target.getProductId());
            if (temp == null) {
                i++;
                continue;
            }
            target.setDeviceTypeId(temp.getDeviceTypeId());
            target.setConfigNetMode(temp.getConfigNetMode());
            targetList.set(i, target);
            i++;
        }
    }

    public static void copyDeviceTypeList(Map<Long, ListDeviceTypeRespVo> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getDeviceTypeId() == null) {
                i++;
                continue;
            }
            ListDeviceTypeRespVo deviceType = tempMap.get(target.getDeviceTypeId());
            if (deviceType == null) {
                i++;
                continue;
            }
            target.setDevType(deviceType.getType());
            target.setDeviceCatalogId(deviceType.getDeviceCatalogId());
            targetList.set(i, target);
            i++;
        }

    }

    public static void copyDeviceStateList(Map<String, Map<String, Object>> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getDeviceId() == null) {
                i++;
                continue;
            }
            Map<String, Object> temp = tempMap.get(target.getDeviceId());
            if (temp == null || temp.size() == 0) {
                i++;
                continue;
            }
            target.setStateProperty(temp);
            targetList.set(i, target);
            i++;
        }
    }

    public static void copyDeviceStatusList(Map<String, ListDeviceStatusRespVo> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getDeviceId() == null) {
                i++;
                continue;
            }
            ListDeviceStatusRespVo deviceStatus = tempMap.get(target.getDeviceId());
            if (deviceStatus == null) {
                i++;
                continue;
            }
            target.setOnlineStatus(deviceStatus.getOnlineStatus());
            target.setActiveStatus(deviceStatus.getActiveStatus());
            targetList.set(i, target);
            i++;
        }
    }
}
