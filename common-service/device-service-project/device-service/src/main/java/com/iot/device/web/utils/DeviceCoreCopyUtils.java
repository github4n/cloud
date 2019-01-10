package com.iot.device.web.utils;

import com.google.common.collect.Lists;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceExtend;
import com.iot.device.model.DeviceStatus;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 19:51 2018/11/13
 * @Modify by:
 */
public class DeviceCoreCopyUtils {
    public static void copyDevice(Device res, ListDeviceInfoRespVo target) {
        if (target == null) {
            return;
        }
        if (res != null) {

            target.setUuid(res.getUuid());
            target.setParentId(res.getParentId());
            target.setId(res.getId());
            target.setIsDirectDevice(res.getIsDirectDevice());
            target.setDeviceTypeId(res.getDeviceTypeId());
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

    public static void copyDevice(Device res, GetDeviceInfoRespVo target) {
        if (target == null) {
            return;
        }
        if (res != null) {

            target.setUuid(res.getUuid());
            target.setParentId(res.getParentId());
            target.setId(res.getId());
            target.setIsDirectDevice(res.getIsDirectDevice());
            target.setDeviceTypeId(res.getDeviceTypeId());
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
            target.setIsAppDev(res.getIsAppDev());

            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setLastUpdateDate(res.getLastUpdateDate());

        }
    }

    public static void copyProduct(ListProductRespVo product, ProductResp target) {
        if (target == null) {
            return;
        }
        if (product != null) {
            target.setId(product.getId());
            target.setProductName(product.getProductName());
            target.setTenantId(product.getTenantId());
            target.setCommunicationMode(product.getCommunicationMode());
            target.setTransmissionMode(product.getTransmissionMode());
            target.setCreateTime(product.getCreateTime());
            target.setUpdateTime(product.getUpdateTime());
            target.setModel(product.getModel());
            target.setConfigNetModes(Lists.newArrayList(product.getConfigNetMode()));
            target.setIsDirectDevice(product.getIsDirectDevice());
            target.setIsKit(product.getIsKit());
            target.setDeviceTypeId(product.getDeviceTypeId());
            target.setIcon(product.getIcon());
            target.setFileId(product.getIcon());
            target.setRemark(product.getRemark());
        }
    }


    public static void copyDevice(Device res, DeviceResp target) {
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

    public static void copyDeviceInfoList(List<Device> tempList, List<DeviceResp> targetList) {

        if (!CollectionUtils.isEmpty(tempList)) {
            for (Device temp : tempList) {
                DeviceResp target = new DeviceResp();
                copyDevice(temp, target);
                targetList.add(target);
            }
        }
    }

    public static void copyDeviceExtendList(Map<String, DeviceExtend> tempMap, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(tempMap)) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            DeviceExtend temp = tempMap.get(target.getDeviceId());
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


    public static void copyProductList(Map<Long, Product> tempMap, List<DeviceResp> targetList) {
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
            Product temp = tempMap.get(target.getProductId());
            if (temp == null) {
                i++;
                continue;
            }
            target.setDeviceTypeId(temp.getDeviceTypeId());
            target.setConfigNetmodeRsps(temp.getConfigNetmodeRsps());
            targetList.set(i, target);
            i++;
        }
    }

    public static void copyDeviceTypeList(Map<Long, DeviceType> tempMap, List<DeviceResp> targetList) {
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
            DeviceType deviceType = tempMap.get(target.getDeviceTypeId());
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

    public static void copyDeviceStatusList(Map<String, DeviceStatus> tempMap, List<DeviceResp> targetList) {
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
            DeviceStatus deviceStatus = tempMap.get(target.getDeviceId());
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
