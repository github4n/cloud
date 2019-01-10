package com.iot.control.packagemanager.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.SceneInfoApi;
import com.iot.control.packagemanager.enums.AttrTypeEnum;
import com.iot.control.packagemanager.execption.StrategyExceptionEnum;
import com.iot.control.packagemanager.vo.req.rule.RuleDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.*;
import com.iot.control.packagemanager.vo.resp.rule.RuleDetailInfoResp;
import com.iot.control.packagemanager.vo.resp.rule.RuleIfInfoResp;
import com.iot.control.packagemanager.vo.resp.rule.RuleThenInfoResp;
import com.iot.control.packagemanager.vo.resp.rule.TriggerInfoResp;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import com.iot.saas.SaaSContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *@description 将策略json，转换为bean前端显示
 *@author wucheng
 *@create 2018/12/10 19:31
 */
public class StrategyToBeanUtil {
    /**
     *@description
     *@author wucheng
     *@params [json, type] json字符串 ，type: boss端传入deviceType，portal端传入product
     *@create 2018/12/11 14:36
     *@return com.iot.control.packagemanager.vo.req.rule.RuleDetailInfoReq
     */
    public static RuleDetailInfoResp strategyJsonToBean(String json, String type) {
        if (StringUtil.isBlank(json)) {
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "json is null");
        }
        // 保存策略传参信息
        RuleDetailInfoReq ruleDetailInfoReq = JsonUtil.fromJson(json, RuleDetailInfoReq.class);
        // 定义返回策略信息
        RuleDetailInfoResp ruleDetailInfoResp = JsonUtil.fromJson(json, RuleDetailInfoResp.class);
        // 复制if
        RuleIfInfoResp ruleIfInfoResp = ruleDetailInfoResp.getRuleIf();
        if (ruleIfInfoResp != null) {
            // 获取触发信息
            TriggerInfoResp triggerInfoResp = ruleIfInfoResp.getTrigger();
            if (triggerInfoResp != null) {
                // 获取触发设备信息
                List<IfDevInfoResp> ifDevInfoRespList = triggerInfoResp.getDev();
                if (ifDevInfoRespList != null && ifDevInfoRespList.size() > 0) {
                    // 解析if下的设备信息
                   ifDevInfoToBean(ifDevInfoRespList, type);
                }
            }
        }
        //        // 3、that -> then -> 方法 -> 属性 sence: 是否存在
        RuleThenInfoResp ruleThenInfoResp = ruleDetailInfoResp.getThen();
        if (ruleThenInfoResp != null) {
            List<ThenDevInfoResp> thenDevs = ruleThenInfoResp.getDev();
            if (thenDevs != null && thenDevs.size() > 0) {
               thenDevInfoToBean(thenDevs, type);
            }
            // 解析场景
            List<Long> scenes = ruleDetailInfoReq.getThen().getScene();
            if (scenes != null && scenes.size() > 0) {
                SceneInfoApi sceneInfoApi = ApplicationContextHelper.getBean(SceneInfoApi.class);
                List<SceneInfoIdAndNameResp> sceneInfoIdAndNameResps = sceneInfoApi.getSceneInfoByIds(scenes, SaaSContextHolder.currentTenantId());
                ruleThenInfoResp.setScenes(sceneInfoIdAndNameResps);
            }
        }

        return ruleDetailInfoResp;
    }

    /**
     *@description
     *@author wucheng
     *@params [ifDevInfoRespList, type]
     *@create 2018/12/14 11:22
     *@return void
     */
    public static void ifDevInfoToBean(List<IfDevInfoResp> ifDevInfoRespList, String type) {
        for (IfDevInfoResp ifDevInfoResp : ifDevInfoRespList) {
            // 设备类型id
            Long deviceTypeId = ifDevInfoResp.getId();
            // 获取设备名称
            ifDevInfoResp.setDevName(ModuleAndDeviceUtil.getDevName(deviceTypeId, type));

            // 获取设备绑定的action、event、properties
            PackageServiceModuleDetailResp psmdr = ModuleAndDeviceUtil.getPackageServiceModuleDetail(deviceTypeId, ModuleIftttTypeEnum.IF.getDesc(), type);
            // 获取该设备类型绑定的事件，并转成map，下面就不用去循环取值
            List<EventResp> eventRespList = psmdr.getEvents();
            Map<Long, EventResp> eventRespMap = new HashMap<>();
            if (eventRespList != null && eventRespList.size() > 0) {
                eventRespMap = eventRespList.stream().collect(Collectors.toMap(EventResp::getId, a -> a, (k1, k2) -> k2));
            }
            // 获取该设备的绑定的属性, 并转成map，下面就不用去循环取值
            List<PropertyResp> propertyRespList = psmdr.getProperties();
            Map<Long, PropertyResp> ifPropertyRespMap = new HashMap<>();
            if (propertyRespList != null && propertyRespList.size() > 0) {
                ifPropertyRespMap = propertyRespList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
            }
            // 获取当前设备绑定的-属性信息
            List<IfAttrInfoResp> ifAttrInfoReqList = ifDevInfoResp.getAttrInfo();
            if (ifAttrInfoReqList != null && ifAttrInfoReqList.size() > 0) { // 存在绑定的属性
                for (IfAttrInfoResp ifAttrInfoResp : ifAttrInfoReqList) {
                    // 当前设备-属性id
                    Long attrId = ifAttrInfoResp.getAttrId();
                    // 当前设备-属性类型event、property
                    String attrType = ifAttrInfoResp.getAttrType();
                    // 设置设备-event属性名称
                    EventResp eventResp = eventRespMap.get(attrId);
                    if (eventResp != null) {
                        ifAttrInfoResp.setName(eventResp.getName());
                    }
                    if (AttrTypeEnum.EVENT.getValue().equals(attrType)) {
                        if (eventRespMap == null || eventRespMap.isEmpty()) {
                            throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exit");
                        }
                        // 获取事件绑定的eventProperties 列表
                        List<PropertyInfoVoResp> eventProperties = ifAttrInfoResp.getEventProperties();
                        if (eventProperties != null && eventProperties.size() > 0) {
                            // 当前事件中包含的属性列表信息
                            if (eventResp != null) {
                                List<PropertyResp> eventPropertyRespList = eventResp.getProperties();
                                Map<Long, PropertyResp> eventPropertyRespMap = new HashMap<>();
                                if (eventPropertyRespList != null && eventPropertyRespList.size() > 0) {
                                    eventPropertyRespMap = eventPropertyRespList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                                }
                                // 循环当前事件配置的属性值，判断该属性值是否存在
                                for (PropertyInfoVoResp eventPropertie : eventProperties) {
                                    if (eventPropertyRespMap != null) {
                                        PropertyResp eventPropertyResp = eventPropertyRespMap.get(eventPropertie.getPropertyId());
                                        if (eventPropertyResp != null) {
                                            eventPropertie.setName(eventPropertyResp.getName());
                                            eventPropertie.setParamType(eventPropertyResp.getParamType());
                                        }
                                    }
                                }
                            }
                        }
                    } else if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                        if(null == ifPropertyRespMap || ifPropertyRespMap.isEmpty()){
                            throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exit");
                        }
                        // 根据属性id获取当前属性信息
                        PropertyResp ifPropertyResp = ifPropertyRespMap.get(attrId);
                        if (ifPropertyResp != null) {
                            ifAttrInfoResp.setName(ifPropertyResp.getName());
                            ifAttrInfoResp.setParamType(ifPropertyResp.getParamType());
                        }
                    }
                }
            }
        }
    }

    /**
     *@description
     *@author wucheng
     *@params [thenDevs, type]
     *@create 2018/12/14 11:22
     *@return void
     */
    public static void thenDevInfoToBean(List<ThenDevInfoResp> thenDevs, String type) {
        for (ThenDevInfoResp thenDevInfoResp : thenDevs) {
            Long deviceTypeId = thenDevInfoResp.getId();
            // 获取设备名称
            thenDevInfoResp.setDevName(ModuleAndDeviceUtil.getDevName(deviceTypeId, type));
            // 获取套包绑定action、event、properties下的属性
            PackageServiceModuleDetailResp psmdr = ModuleAndDeviceUtil.getPackageServiceModuleDetail(deviceTypeId, ModuleIftttTypeEnum.THEN.getDesc(), type);

            // 获取当前设备的行为, 并转成map，下面就不用去循环取值
            List<ActionResp> actionRespList = psmdr.getActions();
            Map<Long, ActionResp> actionRespMap = new HashMap<>();
            if (actionRespList != null && actionRespList.size() > 0) {
                actionRespMap = actionRespList.stream().collect(Collectors.toMap(ActionResp::getId, a -> a, (k1, k2) -> k2));
            }
            // 获取该设备的绑定的属性, 并转成map，下面就不用去循环取值
            List<PropertyResp> propertyRespList = psmdr.getProperties();
            Map<Long, PropertyResp> propertyRespMap = new HashMap<>();
            if (propertyRespList != null && propertyRespList.size() > 0) {
                propertyRespMap = propertyRespList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
            }
            // 获取当前设备绑定的属性信息
            List<ThenAttrInfoResp> thenAttrInfoReqList = thenDevInfoResp.getAttrInfo();

            for (ThenAttrInfoResp thenAttrInfoResp : thenAttrInfoReqList) {
                // 当前设备属性id
                Long attrId = thenAttrInfoResp.getAttrId();
                // 当前设备属性类型 event、property
                String attrType = thenAttrInfoResp.getAttrType();
                // 根据id 获取当前action的信息
                ActionResp actionResp = actionRespMap.get(attrId);
                if (actionResp != null) {
                    thenAttrInfoResp.setName(actionResp.getName());
                }
                if (AttrTypeEnum.ACTION.getValue().equals(attrType)) {
                    // 获取当前action下属性列表
                    List<PropertyInfoVoResp> propertyInfoVoList = thenAttrInfoResp.getActionProperties();
                    if (propertyInfoVoList != null && propertyInfoVoList.size() > 0) {
                        // 获取当前action的属性列表
                        List<PropertyResp> actionRespPropertyList = actionResp.getProperties();
                        Map<Long, PropertyResp> actionRespPropertyMap = new HashMap<>();
                        if (actionRespPropertyList != null && actionRespPropertyList.size() > 0) {
                            actionRespPropertyMap = actionRespPropertyList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                        }
                        for (PropertyInfoVoResp piv : propertyInfoVoList) {
                            if (actionRespPropertyMap != null && !actionRespPropertyMap.isEmpty()) {
                                PropertyResp actionRespProperty = actionRespPropertyMap.get(piv.getPropertyId());
                                if (actionRespProperty != null) {
                                    piv.setName(actionRespProperty.getName());
                                    piv.setParamType(actionRespProperty.getParamType());
                                }
                            }
                        }
                    }
                } else if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                    PropertyResp thenPropertyResp = propertyRespMap.get(thenAttrInfoResp.getAttrId());
                    if (thenPropertyResp != null) {
                        thenAttrInfoResp.setName(thenPropertyResp.getName());
                        thenAttrInfoResp.setParamType(thenPropertyResp.getParamType());
                    }
                }
            }
        }
    }
}

