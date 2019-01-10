package com.iot.control.packagemanager.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.packagemanager.enums.SearchTypeEnum;
import com.iot.control.packagemanager.execption.SceneExceptionEnum;
import com.iot.device.api.*;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;

import java.util.List;

/**
 *@description 根据type 获取devName、PackageServiceModuleDetailResp工具类
 *@author wucheng
 *@create 2018/12/13 16:50
 */
public class ModuleAndDeviceUtil {

    /**
     *@description Boss设置设备类型名称，protal设置产品名称
     *@author wucheng
     *@params [deviceTypeId, type] deviceTypeId: 设备类型id或者产品id
     *@create 2018/12/13 16:50
     *@return void
     */
    public static String getDevName(Long deviceTypeId, String type) {
        // 获取设备名称
        if (SearchTypeEnum.PRODUCT.getCode().equals(type)) {
            ProductApi  productApi = ApplicationContextHelper.getBean(ProductApi.class);
            ProductResp productResp = productApi.getProductById(deviceTypeId);
            if (productResp != null) {
                return productResp.getProductName();
            }
        } else if (SearchTypeEnum.DEVICETYPE.getCode().equals(type)) {
            DeviceTypeApi deviceTypeApi = ApplicationContextHelper.getBean(DeviceTypeApi.class);
            DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(deviceTypeId);
            if (deviceTypeResp != null) {
                return deviceTypeResp.getName();
            }
        } else {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "type error");
        }
        return null;
    }
    /**
     *@description 获取设备绑定的action、event、properties
     *@author wucheng
     *@params [deviceTypeId, iffType, type]
     *@create 2018/12/13 16:54
     *@return com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp
     */
    public static PackageServiceModuleDetailResp getPackageServiceModuleDetail(Long deviceTypeId, String iffType, String type) {
        ProductToServiceModuleApi productToServiceModuleApi = null;
        DeviceTypeToServiceModuleApi deviceTypeToServiceModuleApi = null;

        if (SearchTypeEnum.PRODUCT.getCode().equals(type)) {
            productToServiceModuleApi = ApplicationContextHelper.getBean(ProductToServiceModuleApi.class);
        } else if (SearchTypeEnum.DEVICETYPE.getCode().equals(type)) {
            deviceTypeToServiceModuleApi = ApplicationContextHelper.getBean(DeviceTypeToServiceModuleApi.class);
        } else {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "type error");
        }

        // 获取套包绑定action、event下的属性
        PackageServiceModuleDetailResp psmdr = null;
        if (SearchTypeEnum.PRODUCT.getCode().equals(type)) {
            psmdr = productToServiceModuleApi.queryServiceModuleDetailByIfttt(deviceTypeId, iffType);
        } else if (SearchTypeEnum.DEVICETYPE.getCode().equals(type)) {
            psmdr = deviceTypeToServiceModuleApi.queryServiceModuleDetailByIfttt(deviceTypeId, iffType);
        }
        // 对propertyResps中的allowedValues进行翻译
        if (psmdr != null) {
            ServicePropertyApi servicePropertyApi = ApplicationContextHelper.getBean(ServicePropertyApi.class);
            List<PropertyResp> propertyResps = psmdr.getProperties();
            if (propertyResps != null && propertyResps.size() > 0) {
                for (PropertyResp propertyResp : propertyResps) {
                    ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                    propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                }
            }
            List<ActionResp> actionResps = psmdr.getActions();
            if (actionResps != null && actionResps.size() > 0) {
                for (ActionResp actionResp : actionResps) {
                    List<PropertyResp> actionPropertyResps = actionResp.getProperties();
                    if (actionPropertyResps != null && actionPropertyResps.size() > 0) {
                        for (PropertyResp propertyResp : actionPropertyResps) {
                            ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                            propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                        }
                    }
                }
            }
            List<EventResp> eventResps = psmdr.getEvents();
            if (eventResps != null && eventResps.size() > 0) {
                for (EventResp eventResp : eventResps) {
                    List<PropertyResp> eventPropertyResps = eventResp.getProperties();
                    if (eventPropertyResps != null && eventPropertyResps.size() > 0) {
                        for (PropertyResp propertyResp : eventPropertyResps) {
                            ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                            propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                        }
                    }
                }
            }
        }
        return psmdr;
    }
}
