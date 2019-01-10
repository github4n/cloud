package com.iot.device.core.utils;

import com.google.common.collect.Maps;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceExtend;
import com.iot.device.model.DeviceStatus;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.model.ServiceModule;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.vo.rsp.DeviceExtendResp;
import com.iot.device.vo.rsp.DeviceInfoListResp;
import com.iot.device.vo.rsp.DevicePropertyInfoResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ProductPageResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import org.apache.commons.lang3.time.DateFormatUtils;
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

    public static void copyDeviceInfo(Device device, DeviceType deviceType, Product product, DeviceInfoListResp target) {
        if (target == null) {
            return;
        }
        if (device != null) {
            target.setDeviceId(device.getUuid());
            target.setTenantId(device.getTenantId());
            target.setDeviceName(device.getName());
            target.setIsDirectDevice(device.getIsDirectDevice());
            target.setProductId(device.getProductId());
        }
        if (deviceType != null) {
            target.setDeviceType(deviceType.getType());
            target.setDeviceTypeId(deviceType.getId());
        }
        if (product != null) {
            target.setProductId(product.getId());
        } else {
            target.setProductId(device.getProductId());
        }
    }

    public static void copyDeviceInfo(List<DeviceType> deviceTypeList, List<Product> productList, List<DeviceInfoListResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        Map<Long, Product> productMap = Maps.newHashMap();
        Map<Long, DeviceType> deviceTypeMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(productList)) {
            productList.forEach(product -> {

                productMap.put(product.getId(), product);
            });
        }
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeList.forEach(deviceType -> {

                deviceTypeMap.put(deviceType.getId(), deviceType);
            });
        }
        int i = 0;
        for (DeviceInfoListResp target : targetList) {
            if (target.getProductId() != null) {
                Product product = productMap.get(target.getProductId());
                if (product == null) {
                    i++;
                    continue;
                }
                if (product.getDeviceTypeId() == null) {
                    i++;
                    continue;
                }
                DeviceType deviceType = deviceTypeMap.get(product.getDeviceTypeId());
                if (deviceType == null) {
                    i++;
                    continue;
                }
                target.setDeviceType(deviceType.getType());
                target.setDeviceTypeId(deviceType.getId());
                targetList.set(i,target);
            }
            i++;
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
//        target.setDevType(res.getDevType);
//        target.setPassword(res.getPassword);
//        target.setActiveStatus(res.getActiveStatus);
//        target.setSwitchStatus(res.getSwitchStatus);
//        target.setOnlineStatus(res.getOnlineStatus);
//        target.setBusinessType(res.getBusinessType);
    }

    public static void copyDeviceStatus(DeviceStatus res, DeviceResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setActiveStatus(res.getActiveStatus());
            target.setSwitchStatus(res.getOnOff() != null ? res.getOnOff() : 0);
            target.setOnlineStatus(res.getOnlineStatus());
        }
    }

    public static void copyDeviceType(DeviceType res, DeviceResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setDevType(res.getType());
        }
    }


    public static void copyDeviceExtend(DeviceExtend res, DeviceExtendResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setDeviceId(res.getDeviceId());
            target.setBatchNumId(res.getBatchNumId());
            target.setCreateTime(res.getCreateTime());
            target.setP2pId(res.getP2pId());
            target.setUuidValidityDays(res.getUuidValidityDays());
            target.setDeviceCipher(res.getDeviceCipher());
            target.setTenantId(res.getTenantId());
            target.setFirstUploadSubDev(res.getFirstUploadSubDev());
            target.setUnbindFlag(res.getUnbindFlag());
            target.setResetFlag(res.getResetFlag());
            target.setArea(res.getArea());
        }
    }

    public static void copyDeviceType(DeviceType deviceType, DeviceTypeResp target) {
        if (target == null) {
            return;
        }
        if (deviceType != null) {
            target.setId(deviceType.getId());
            target.setTenantId(deviceType.getTenantId());
            target.setCreateBy(deviceType.getCreateBy());
            target.setUpdateBy(deviceType.getUpdateBy());
            target.setCreateTime(deviceType.getCreateTime());
            target.setUpdateTime(deviceType.getUpdateTime());
            target.setName(deviceType.getName());
            target.setDeviceCatalogId(deviceType.getDeviceCatalogId());
            target.setDeviceCatalogName(deviceType.getDeviceCatalogName());
            target.setDescription(deviceType.getDescription());
            target.setIsDeleted(deviceType.getIsDeleted());
            target.setVenderFlag(deviceType.getVenderFlag());
            target.setType(deviceType.getType());
            target.setIftttType(deviceType.getIftttType());
        }
    }

    public static void copyProduct(Product product, ProductResp target) {
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
            target.setIsDirectDevice(product.getIsDirectDevice());
            target.setIsKit(product.getIsKit());
            target.setDeviceTypeId(product.getDeviceTypeId());
            target.setIcon(product.getIcon());
            target.setFileId(product.getIcon());
            target.setRemark(product.getRemark());
            target.setAuditStatus(product.getAuditStatus());
        }
    }

    public static void copyProduct(Product product, ProductPageResp target) {
        if (target == null) {
            return;
        }
        if (product != null) {
            target.setId(product.getId());
            target.setProductName(product.getProductName());
            target.setCreateTime(product.getCreateTime() != null ? DateFormatUtils.format(product.getCreateTime(), "yyyy-MM-dd") : null);
            target.setDeviceTypeId(product.getDeviceTypeId());
            target.setProductIcon(product.getIcon());
            target.setDeviceTypeName(product.getDeviceTypeName());
            target.setDevelopStatus(product.getDevelopStatus());
            target.setCommunicationMode(product.getCommunicationMode());
            target.setWhetherSoc(product.getWhetherSoc() == null ? DeviceType.IS_NOT_SOC : product.getWhetherSoc());
            target.setModel(product.getModel());
        }
    }

    public static void copyDeviceList(List<Device> deviceList, List<DeviceResp> targetList) {

        if (!CollectionUtils.isEmpty(deviceList)) {
            for (Device device : deviceList) {
                DeviceResp target = new DeviceResp();
                copyDevice(device, target);
                targetList.add(target);
            }
        }
    }

    public static void copyDeviceStatusList(List<DeviceStatus> deviceStatusList, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(deviceStatusList)) {
            return;
        }

        int i = 0;
        Map<String, DeviceStatus> deviceStatusMap = Maps.newHashMap();
        for (DeviceStatus deviceStatus : deviceStatusList) {
            deviceStatusMap.put(deviceStatus.getDeviceId(), deviceStatus);
        }
        for (DeviceResp target : targetList) {

            DeviceStatus deviceStatus = deviceStatusMap.get(target.getDeviceId());
            if (deviceStatus == null) {
                i++;
                continue;
            }
            copyDeviceStatus(deviceStatus, target);
            targetList.set(i, target);
            i++;
        }

    }

    public static void copyDeviceProperty(Device device, DevicePropertyInfoResp target) {
        if (target == null) {
            return;
        }
        if (device != null) {
            target.setDeviceIcon(device.getIcon());
            target.setDeviceId(device.getUuid());
            target.setParentId(device.getParentId());
            target.setIsDirectDevice(String.valueOf(device.getIsDirectDevice()));
        }
    }

    public static void copyDeviceList(Product resProduct, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (resProduct == null) {
            return;
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getProductId() == null) {
                i++;
                continue;
            }
            if (target.getProductId().compareTo(resProduct.getId()) == 0) {
                target.setDeviceTypeId(resProduct.getDeviceTypeId());
                targetList.set(i, target);
            }
            i++;
        }
    }

    public static void copyProductList(List<Product> resProductList, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(resProductList)) {
            return;
        }

        Map<Long, Product> productMap = Maps.newHashMap();
        for (Product product : resProductList) {
            productMap.put(product.getId(), product);
        }
        int i = 0;
        for (DeviceResp target : targetList) {
            if (target.getProductId() == null) {
                i++;
                continue;
            }
            Product product = productMap.get(target.getProductId());
            if (product == null) {
                i++;
                continue;
            }
            target.setDeviceTypeId(product.getDeviceTypeId());
            targetList.set(i, target);
            i++;
        }
    }

    public static void copyDeviceTypeList(List<DeviceType> deviceTypeList, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(deviceTypeList)) {
            return;
        }

        int i = 0;
        Map<Long, DeviceType> deviceTypeMap = Maps.newHashMap();
        for (DeviceType deviceType : deviceTypeList) {
            deviceTypeMap.put(deviceType.getId(), deviceType);
        }
        for (DeviceResp target : targetList) {
            if (target.getDeviceTypeId() == null) {
                i++;
                continue;
            }
            DeviceType deviceType = deviceTypeMap.get(target.getDeviceTypeId());
            if (deviceType == null) {
                i++;
                continue;
            }
            target.setDevType(deviceType.getType());
            targetList.set(i, target);
            i++;
        }

    }


    public static void copyServiceModel(ServiceModule res, ServiceModuleListResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setParentId(res.getParentId());
            target.setTenantId(res.getTenantId());
            target.setVersion(res.getVersion());
            target.setName(res.getName());
            target.setCode(res.getCode());
            target.setDevelopStatus(res.getDevelopStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setPropertyStatus(res.getPropertyStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setTestCase(res.getTestCase());
            target.setDescription(res.getDescription());
            target.setImg(res.getImg());
            target.setChangeImg(res.getChangeImg());
            target.setStatus(res.getStatus());
        }
    }

    public static void copyServiceModel(ServiceModule res, ServiceModuleInfoResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setParentId(res.getParentId());
            target.setTenantId(res.getTenantId());
            target.setVersion(res.getVersion());
            target.setName(res.getName());
            target.setCode(res.getCode());
            target.setDevelopStatus(res.getDevelopStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setPropertyStatus(res.getPropertyStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setTestCase(res.getTestCase());
            target.setDescription(res.getDescription());
        }
    }

    public static void copyModuleAction(ServiceModuleAction res, ServiceModuleActionResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setParentId(res.getParentId());
            target.setTenantId(res.getTenantId());
            target.setServiceModuleId(res.getServiceModuleId());
            target.setVersion(res.getVersion());
            target.setName(res.getName());
            target.setCode(res.getCode());
            target.setTags(res.getTags());
            target.setApiLevel(res.getApiLevel());
            target.setDevelopStatus(res.getDevelopStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setPropertyStatus(res.getPropertyStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setReqParamType(res.getReqParamType() != null ? (Integer) res.getReqParamType().getValue() : null);
            target.setReturnType(res.getReturnType() != null ? (Integer) res.getReturnType().getValue() : null);
            target.setParams(res.getParams());
            target.setReturnDesc(res.getReturnDesc());
            target.setReturns(res.getReturns());
            target.setTestCase(res.getTestCase());
            target.setDescription(res.getDescription());
            target.setIftttType(res.getIftttType());
            target.setPortalIftttType(res.getPortalIftttType());
        }
    }

    public static void copyModuleEvent(ServiceModuleEvent res, ServiceModuleEventResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setParentId(res.getParentId());
            target.setTenantId(res.getTenantId());
            target.setServiceModuleId(res.getServiceModuleId());
            target.setVersion(res.getVersion());
            target.setName(res.getName());
            target.setCode(res.getCode());
            target.setTags(res.getTags());
            target.setApiLevel(res.getApiLevel());
            target.setDevelopStatus(res.getDevelopStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setPropertyStatus(res.getPropertyStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setParams(res.getParams());
            target.setTestCase(res.getTestCase());
            target.setDescription(res.getDescription());
            target.setIftttType(res.getIftttType());
            target.setPortalIftttType(res.getPortalIftttType());
        }
    }

    public static void copyModuleProperty(ServiceModuleProperty res, ServiceModulePropertyResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setParentId(res.getParentId());
            target.setTenantId(res.getTenantId());
            target.setServiceModuleId(res.getServiceModuleId());
            target.setVersion(res.getVersion());
            target.setName(res.getName());
            target.setCode(res.getCode());
            target.setTags(res.getTags());
            target.setApiLevel(res.getApiLevel());
            target.setDevelopStatus(res.getDevelopStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setReqParamType(res.getReqParamType() != null ? (Integer) res.getReqParamType().getValue() : null);
            target.setReturnType(res.getReturnType() != null ? (Integer) res.getReturnType().getValue() : null);
            target.setPropertyStatus(res.getPropertyStatus() != null ? (Integer) res.getDevelopStatus().getValue() : null);
            target.setRwStatus(res.getRwStatus() != null ? (Integer) res.getRwStatus().getValue() : null);
            target.setParamType(res.getParamType() != null ? (Integer) res.getParamType().getValue() : null);
            target.setMaxValue(res.getMaxValue());
            target.setMinValue(res.getMinValue());
            target.setTestCase(res.getTestCase());
            target.setDescription(res.getDescription());
            target.setAllowedValues(res.getAllowedValues());
            target.setDescription(res.getDescription());
            target.setIftttType(res.getIftttType());
            target.setPortalIftttType(res.getPortalIftttType());
            target.setPropertyType(res.getPropertyType());
            target.setInHomePage(res.getInHomePage());
        }
    }

    public static <T> com.iot.common.helper.Page<T> copyMybatisPlusPageToPage(com.baomidou.mybatisplus.plugins.Page<T> page,com.iot.common.helper.Page<T> myPage){
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageNum(page.getCurrent());
        myPage.setPageSize(page.getSize());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        myPage.setResult(page.getRecords());
        return myPage;
    }

    public static <T1,T2> com.iot.common.helper.Page<T2> copyMybatisPlusPageToPageNoResult(com.baomidou.mybatisplus.plugins.Page<T1> page){
        com.iot.common.helper.Page<T2> myPage = new com.iot.common.helper.Page<T2>();
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageNum(page.getCurrent());
        myPage.setPageSize(page.getSize());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        return myPage;
    }

    public static void copyDeviceExtendList(List<DeviceExtend> deviceExtendList, List<DeviceResp> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        if (CollectionUtils.isEmpty(deviceExtendList)) {
            return;
        }

        int i = 0;
        Map<String, DeviceExtend> deviceStatusMap = Maps.newHashMap();
        for (DeviceExtend deviceExtend : deviceExtendList) {
            deviceStatusMap.put(deviceExtend.getDeviceId(), deviceExtend);
        }
        for (DeviceResp target : targetList) {

            DeviceExtend deviceExtend = deviceStatusMap.get(target.getDeviceId());
            if (deviceExtend == null) {
                i++;
                continue;
            }
            target.setP2pId(deviceExtend.getP2pId());
            targetList.set(i, target);
            i++;
        }
    }
}
