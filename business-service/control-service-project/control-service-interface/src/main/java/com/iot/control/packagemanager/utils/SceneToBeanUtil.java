package com.iot.control.packagemanager.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.enums.AttrTypeEnum;
import com.iot.control.packagemanager.execption.SceneExceptionEnum;
import com.iot.control.packagemanager.vo.resp.PropertyInfoVoResp;
import com.iot.control.packagemanager.vo.resp.ThenAttrInfoResp;
import com.iot.control.packagemanager.vo.resp.ThenDevInfoResp;
import com.iot.control.packagemanager.vo.resp.scene.SceneConfigResp;
import com.iot.control.packagemanager.vo.resp.scene.SceneDetailInfoResp;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *@description
 *@author wucheng
 *@create 2018/12/11 9:45
 */
public class SceneToBeanUtil {
    /**
     *@description
     *@author wucheng
     *@params [json, deviceTypeNameList, type] json字符串，设备类型名称列表，type: boss端传入deviceType，portal端传入product
     *@create 2018/12/11 10:20
     *@return void
     */
    public static SceneDetailInfoResp sceneToBean(String json, Map<Long, String> deviceTypeNameList, String type) {
        if (StringUtil.isBlank(json)) {
            throw  new BusinessException(SceneExceptionEnum.PARAM_ERROR, "json is null");
        }
        SceneDetailInfoResp sceneDetailInfoResp = JsonUtil.fromJson(json, SceneDetailInfoResp.class);
        if (sceneDetailInfoResp != null) {
            SceneConfigResp sceneConfigResp = sceneDetailInfoResp.getConfig();
            if (sceneConfigResp != null) {
                List<ThenDevInfoResp> devs = sceneConfigResp.getDev();
                if (devs != null && devs.size() > 0) {
                   thenDevToBean(devs, deviceTypeNameList, type);
                }
            }
        }
        return sceneDetailInfoResp;
    }

    /**
     *@description 解析then下的设备转成bean
     *@author wucheng
     *@params [devs, deviceTypeNameList, type]
     *@create 2018/12/14 11:21
     *@return void
     */
    public static void thenDevToBean(List<ThenDevInfoResp> devs, Map<Long, String> deviceTypeNameList, String type) {

        for (ThenDevInfoResp thenDevInfoResp : devs){
            // 设置设备信息
            thenDevInfoResp.setDevName(deviceTypeNameList.get(thenDevInfoResp.getId()));

            // 设置返回设备属性列表信息
            List<ThenAttrInfoResp> thenAttrInfoResps = thenDevInfoResp.getAttrInfo();
            if(null == thenAttrInfoResps || thenAttrInfoResps.isEmpty()){
                throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "then attr info is null");
            }
            Long deviceTypeId = thenDevInfoResp.getId();
            if (deviceTypeId == null) {
                throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "deviceTypeId is null");
            }

            // 获取套包绑定action、event下的属性
            PackageServiceModuleDetailResp packageServiceModuleDetailResp = ModuleAndDeviceUtil.getPackageServiceModuleDetail(deviceTypeId, ModuleIftttTypeEnum.THEN.getDesc(), type);

            List<ActionResp> actionRespList  = packageServiceModuleDetailResp.getActions();
            List<PropertyResp> propertyRespList = packageServiceModuleDetailResp.getProperties();

            Map<Long, ActionResp> actionRespMap = new HashMap<>();
            Map<Long, PropertyResp> propertyRespMap = new HashMap<>();
            if(null != actionRespList && actionRespList.size() > 0){
                actionRespMap = actionRespList.stream().collect(Collectors.toMap(ActionResp::getId, a -> a,(k1, k2)->k1));
            }
            if(null != propertyRespList && propertyRespList.size() > 0){
                propertyRespMap = propertyRespList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a,(k1, k2)->k1));
            }

            for(ThenAttrInfoResp thenAttrInfoResp : thenAttrInfoResps){
                if(AttrTypeEnum.PROPERTY.getValue().equals(thenAttrInfoResp.getAttrType())){
                    PropertyResp propertyRespFromDb = propertyRespMap.get(thenAttrInfoResp.getAttrId());
                    if (propertyRespFromDb != null) {
                        thenAttrInfoResp.setName(propertyRespFromDb.getName());
                        thenAttrInfoResp.setParamType(propertyRespFromDb.getParamType());
                    }
                }else if(AttrTypeEnum.ACTION.getValue().equals(thenAttrInfoResp.getAttrType())){
                    // 根据id 获取对象action的信息
                    ActionResp actionResp = actionRespMap.get(thenAttrInfoResp.getAttrId());
                    if (actionResp == null) {
                        throw new BusinessException(SceneExceptionEnum.ACTION_NOT_EXIST);
                    }
                    // 获取action下属性列表
                    List<PropertyInfoVoResp> propertyInfoVoList = thenAttrInfoResp.getActionProperties();
                    if (propertyInfoVoList != null && !propertyInfoVoList.isEmpty()) {
                        // 获取当前对象action的属性列表
                        List<PropertyResp> actionRespPropertyList = actionResp.getProperties();
                        Map<Long, PropertyResp> actionRespPropertyMap = new HashMap<>();
                        if (actionRespPropertyList != null && actionRespPropertyList.size() > 0) {
                            actionRespPropertyMap = actionRespPropertyList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                        }
                        for(PropertyInfoVoResp piv : propertyInfoVoList) {
                            PropertyResp actionRespProperty = actionRespPropertyMap.get(piv.getPropertyId());
                            if (actionRespProperty != null) {
                                piv.setParamType(actionRespProperty.getParamType());
                                piv.setName(actionRespProperty.getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
