package com.iot.control.packagemanager.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.packagemanager.enums.AttrTypeEnum;
import com.iot.control.packagemanager.execption.SceneExceptionEnum;
import com.iot.control.packagemanager.vo.req.ActuatorInfoVo;
import com.iot.control.packagemanager.vo.req.PropertyInfoVo;
import com.iot.control.packagemanager.vo.req.ThenAttrInfoReq;
import com.iot.control.packagemanager.vo.req.ThenDevInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SceneConfigReq;
import com.iot.device.api.DeviceTypeToServiceModuleApi;
import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *@description 检查场景json工具类
 *@author wucheng
 *@create 2018/12/5 14:03
 */
public class CheckSceneConfigUtil {


    /**
     * @param sceneConfigReq
     * @param type （类型，根据type加载不同对象）
     * @return
     */
    public static boolean checkSceneConfig(SceneConfigReq sceneConfigReq, String type) {
        if(null == sceneConfigReq){
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "scene config is null");
        }

        List<ThenDevInfoReq> thenDevInfoReqList = sceneConfigReq.getDev();
        List<ActuatorInfoVo> actuatorInfoVos = sceneConfigReq.getActuator();
        int configNull = 0;
        if(null == thenDevInfoReqList || thenDevInfoReqList.isEmpty()){
            configNull++;
        } else {
            for(ThenDevInfoReq thenDevInfoReq : thenDevInfoReqList){
                List<ThenAttrInfoReq> thenAttrInfoReqList = thenDevInfoReq.getAttrInfo();
                if(null == thenAttrInfoReqList || thenAttrInfoReqList.isEmpty()){
                    throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "then attr info is null");
                }
                Long deviceTypeId = thenDevInfoReq.getId();
                if (deviceTypeId == null) {
                    throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "deviceTypeId is null");
                }

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
                for(ThenAttrInfoReq thenAttrInfoReq : thenAttrInfoReqList){
                    if(AttrTypeEnum.PROPERTY.getValue().equals(thenAttrInfoReq.getAttrType())){
                        PropertyResp propertyRespFromDb = propertyRespMap.get(thenAttrInfoReq.getAttrId());
                        if (propertyRespFromDb != null) {
                            CheckPropertyUtil.compareProperty(thenAttrInfoReq.getValue(), propertyRespFromDb);
                        } else {
                            throw new BusinessException(SceneExceptionEnum.PROPERTY_NOT_EXIST, "property is not exist");
                        }
                    }else if(AttrTypeEnum.ACTION.getValue().equals(thenAttrInfoReq.getAttrType())){
                        // 根据id 获取对象action的信息
                        ActionResp actionResp = actionRespMap.get(thenAttrInfoReq.getAttrId());
                        if (actionResp == null) {
                            throw new BusinessException(SceneExceptionEnum.ACTION_NOT_EXIST);
                        }
                        // 获取action下属性列表
                        List<PropertyInfoVo> propertyInfoVoList = thenAttrInfoReq.getActionProperties();
                        if (propertyInfoVoList != null && !propertyInfoVoList.isEmpty()) {
                            // 获取当前对象action的属性列表
                            List<PropertyResp> actionRespPropertyList = actionResp.getProperties();
                            Map<Long, PropertyResp> actionRespPropertyMap = new HashMap<>();
                            if (actionRespPropertyList != null && actionRespPropertyList.size() > 0) {
                                actionRespPropertyMap = actionRespPropertyList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                            }
                            for(PropertyInfoVo piv : propertyInfoVoList) {
                                PropertyResp actionRespProperty = actionRespPropertyMap.get(piv.getPropertyId());
                                // 比较action下的属性是否存在
                                CheckPropertyUtil.compareProperty(piv.getValue(), actionRespProperty);
                            }
                        }
                    }else {
                        throw new BusinessException(SceneExceptionEnum.PROPERTY_NOT_EXIST, "property is not exist");
                    }
                }
            }
        }
        if (actuatorInfoVos == null || actuatorInfoVos.isEmpty()) {
            configNull ++;
        } else {
            //执行者校验...

        }
        if (configNull == 2) {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "scene config detail is null");
        }
        return true;
    }
}
