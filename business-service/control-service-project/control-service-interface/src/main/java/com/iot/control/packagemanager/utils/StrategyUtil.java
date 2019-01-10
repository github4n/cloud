package com.iot.control.packagemanager.utils;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.SceneInfoApi;
import com.iot.control.packagemanager.enums.AttrTypeEnum;
import com.iot.control.packagemanager.enums.ConditionsTypeEnum;
import com.iot.control.packagemanager.enums.PackageTypeEnum;
import com.iot.control.packagemanager.enums.SecurityTypeEnum;
import com.iot.control.packagemanager.execption.SceneExceptionEnum;
import com.iot.control.packagemanager.execption.StrategyExceptionEnum;
import com.iot.control.packagemanager.vo.req.*;
import com.iot.control.packagemanager.vo.req.rule.*;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *@description 解析策略工具类
 *@author wucheng
 *@create 2018/11/24 11:20
 */
public class StrategyUtil {

    /**
      * @despriction：描述
      * @author  wucheng
      * @created 2018/12/13 14:33
      * @param ruleDetailInfoReq
      * @param type 用于区分是设备类型还是产品
      * @param tenantId 租户id
      * @param packageType:套包类型
      * @return boolean
      */
    public static boolean analysisStrategyJson(RuleDetailInfoReq ruleDetailInfoReq, String type, Long tenantId, Integer packageType) {
        if (ruleDetailInfoReq == null) {
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "rule config is null");
        }

        if (packageType == null) {
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "package type is null");
        }
        //如果是安防套包类型
        if (PackageTypeEnum.SECURITY.getValue() == packageType) {
            // 1.获取策略中安全套包, 存在安全套包，解析安全套包， 不存在不解析
            SecurityInfoReq securityInfoReq = ruleDetailInfoReq.getSecurity();
            if (ruleDetailInfoReq.getSecurity() == null) {
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "security info is null");
            }
            analysisSecurity(securityInfoReq); // 解析安防参数
        }
        // 2、判断if中参数
        RuleIfInfoReq ruleIfInfoReq = ruleDetailInfoReq.getRuleIf();
        if (ruleIfInfoReq != null) {
            String logic = ruleIfInfoReq.getLogic();
            if (!(ConditionsTypeEnum.AND.getCondition().equals(logic) || ConditionsTypeEnum.OR.getCondition().equals(logic))) {
                // 抛出异常logic不正确
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "logic only (and/or)");
            }
            // 获取触发信息
            TriggerInfoReq triggerInfoReq = ruleIfInfoReq.getTrigger();
            if (triggerInfoReq != null) {
                // 获取触发设备信息
                List<IfDevInfoReq> ifDevInfoReqList = triggerInfoReq.getDev();
                if (ifDevInfoReqList != null && ifDevInfoReqList.size() > 0) {
                    // 解析if下的设备信息
                    analysisIfDevInfo(ifDevInfoReqList, type);
                }
            }
            // 3、第三方协议 暂时不处理
            List<Map<String, Object>> mapThreeApis = triggerInfoReq.getThreeApi();
        } else {
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "rule if is null");
        }

        // 3、that -> then -> 方法 -> 属性 sence: 是否存在
        RuleThenInfoReq ruleThenInfoReq = ruleDetailInfoReq.getThen();
        if (ruleThenInfoReq == null) {
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "rule then is null");
        }
        List<ThenDevInfoReq> thenDevs = ruleThenInfoReq.getDev();
        if (thenDevs != null && thenDevs.size() > 0) {
            // 解析设备中的数据是否正确
            analysisThenDevInfo(thenDevs, type);
        }

        // 解析场景
        List<Long> scenes = ruleThenInfoReq.getScene();
        if (scenes != null && scenes.size() > 0) {
            //如果是安防套包类型，不用配置场景
            if (PackageTypeEnum.SECURITY.getValue() == packageType) {
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "security package can't config scene");
            }
            // 判断场景是否存在
            SceneInfoApi sceneInfoApi = ApplicationContextHelper.getBean(SceneInfoApi.class);
            boolean result = sceneInfoApi.checkExist(scenes, tenantId);
            if (!result) {
                throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "scene not exist");
            }
        }
        // 解析联动 暂时未处理
        List<ActuatorInfoVo> actuators = ruleThenInfoReq.getActuator();
        return true;
    }

    /**
     * @return boolean
     * @description 解析套包类型
     * @author wucheng
     * @params [securityInfoReq]
     * @create 2018/11/24 14:03
     */
    public static boolean analysisSecurity(SecurityInfoReq securityInfoReq) {
        String securityType = securityInfoReq.getType(); // 获取套包策略类型
        if (StringUtil.isNotBlank(securityType)) {
            if (!(SecurityTypeEnum.AWAY.getType().equals(securityType) || SecurityTypeEnum.STAY.getType().equals(securityType) || SecurityTypeEnum.SOS.getType().equals(securityType))) {
                // 抛出异常 套包类型不对
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "security type is error");
            }
        } else {
            // 抛出异常 套包类型为空
            throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "security type is null");
        }
        // 解析map参数, 暂时不理
        Map<String, Object> map = securityInfoReq.getAlram();
        return true;
    }

    /**
     * @return boolean
     * @description 解析if设备dev下的信息
     * @author wucheng
     * @params [ruleIfInfoReq]
     * @create 2018/11/24 13:50
     */
    public static boolean analysisIfDevInfo(List<IfDevInfoReq> ifDevInfoReqList, String type) {
        for (IfDevInfoReq devInfoReq : ifDevInfoReqList) {
            // 设备类型id
            Long deviceTypeId = devInfoReq.getId();
            if (deviceTypeId == null) {
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_NULL, "deviceTypeId is null");
            }

            String logic = devInfoReq.getAttrLogic();
            if (!(ConditionsTypeEnum.AND.getCondition().equals(logic) || ConditionsTypeEnum.OR.getCondition().equals(logic))) {
                // 抛出异常logic不正确
                throw new BusinessException(StrategyExceptionEnum.PARAM_IS_ERROR, "attr logic only (and/or)");
            }

            //  获取设备绑定的action、event、properties
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
            List<IfAttrInfoReq> ifAttrInfoReqList = devInfoReq.getAttrInfo();
            if (ifAttrInfoReqList != null && ifAttrInfoReqList.size() > 0) { // 存在绑定的属性
                for (IfAttrInfoReq attrInfo : ifAttrInfoReqList) {
                    // 当前设备-属性id
                    Long attrId = attrInfo.getAttrId();
                    if (attrId == null) {
                        throw new BusinessException(StrategyExceptionEnum.PARAM_IS_NULL, "attrId is null");
                    }
                    // 当前设备-属性类型event、property
                    String attrType = attrInfo.getAttrType();
                    if (AttrTypeEnum.EVENT.getValue().equals(attrType)) {
                        EventResp eventResp = eventRespMap.get(attrId);
                        if (eventResp == null) {
                            throw new BusinessException(StrategyExceptionEnum.EVENT_NOT_EXIST);
                        }
                        // 获取事件绑定的eventProperties 列表
                        List<PropertyInfoVo> eventProperties = attrInfo.getEventProperties();
                        if (eventProperties != null && eventProperties.size() > 0) {
                            // 当前事件中包含的属性列表信息
                            if (eventResp != null) {
                                List<PropertyResp> eventPropertyRespList = eventResp.getProperties();
                                Map<Long, PropertyResp> eventPropertyRespMap = new HashMap<>();
                                if (eventPropertyRespList != null && eventPropertyRespList.size() > 0) {
                                    eventPropertyRespMap = eventPropertyRespList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                                }
                                // 循环当前事件配置的属性值，判断该属性值是否存在
                                for (PropertyInfoVo eventPropertie : eventProperties) {
                                    if (eventPropertyRespMap != null) {
                                        PropertyResp eventPropertyResp = eventPropertyRespMap.get(eventPropertie.getPropertyId());
                                        if (eventPropertyResp != null) {
                                            // 比较当前事件下的属性值，是否存在
                                            CheckPropertyUtil.compareProperty(eventPropertie.getValue(), eventPropertyResp);
                                        } else {
                                            // 抛出异常该事件的属性不存在
                                            throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST);
                                        }
                                    }

                                }
                            }
                        }
                    } else if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                        // 根据属性id获取当前属性信息
                        PropertyResp ifPropertyResp = ifPropertyRespMap.get(attrId);
                        if (ifPropertyResp != null) {
                            // 比较属性值，是否存在
                            CheckPropertyUtil.compareProperty(attrInfo.getValue(), ifPropertyResp);
                        } else {
                            // 抛出异常该事件的属性不存在
                            throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST);
                        }
                    } else {
                        throw new BusinessException(StrategyExceptionEnum.ATTRTYPE_NOT_EXIST, "attType is not exist");
                    }
                }
            }
        }
        return true;
    }

    public static  boolean analysisThenDevInfo(List<ThenDevInfoReq> thenDevs, String type) {
        if (thenDevs != null && thenDevs.size() > 0) {
            for (ThenDevInfoReq devInfoReq : thenDevs) {
                Long deviceTypeId = devInfoReq.getId();
                if (deviceTypeId == null) {
                    throw new BusinessException(StrategyExceptionEnum.PARAM_IS_NULL, "deviceTypeId is null");
                }
                //  获取设备绑定的action、event、properties
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
                List<ThenAttrInfoReq> thenAttrInfoReqList = devInfoReq.getAttrInfo();
                for (ThenAttrInfoReq thenAttrInfo : thenAttrInfoReqList) {
                    // 当前设备属性id
                    Long attrId = thenAttrInfo.getAttrId();
                    if (attrId == null) {
                        throw new BusinessException(StrategyExceptionEnum.PARAM_IS_NULL, "attrId is null");
                    }
                    // 当前设备属性类型 event、property
                    String attrType = thenAttrInfo.getAttrType();
                    if (AttrTypeEnum.ACTION.getValue().equals(attrType)) {
                        // 根据id 获取对象action的信息
                        ActionResp actionResp = actionRespMap.get(thenAttrInfo.getAttrId());
                        if (actionResp == null) {
                            throw new BusinessException(StrategyExceptionEnum.ACTION_NOT_EXIST);
                        }

                        // 获取当前action下属性列表
                        List<PropertyInfoVo> propertyInfoVoList = thenAttrInfo.getActionProperties();
                        if (propertyInfoVoList != null && propertyInfoVoList.size() > 0) {
                            // 获取当前action的属性列表
                            List<PropertyResp> actionRespPropertyList = actionResp.getProperties();
                            Map<Long, PropertyResp> actionRespPropertyMap = new HashMap<>();
                            if (actionRespPropertyList != null && actionRespPropertyList.size() > 0) {
                                actionRespPropertyMap = actionRespPropertyList.stream().collect(Collectors.toMap(PropertyResp::getId, a -> a, (k1, k2) -> k2));
                            }
                            for (PropertyInfoVo piv : propertyInfoVoList) {
                                if (actionRespPropertyMap != null && !actionRespPropertyMap.isEmpty()) {
                                    PropertyResp actionRespProperty = actionRespPropertyMap.get(piv.getPropertyId());
                                    // 比较action下的属性是否存在
                                    if (actionRespProperty != null) {
                                        CheckPropertyUtil.compareProperty(piv.getValue(), actionRespProperty);
                                    } else {
                                        throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exist");
                                    }
                                }
                            }
                        }
                    } else if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                        PropertyResp thenPropertyResp = propertyRespMap.get(thenAttrInfo.getAttrId());
                        if (thenPropertyResp != null) {
                            CheckPropertyUtil.compareProperty(thenAttrInfo.getValue(), thenPropertyResp);
                        } else {
                            throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exist");
                        }
                    } else {
                        throw new BusinessException(StrategyExceptionEnum.ATTRTYPE_NOT_EXIST, "attType is not exist");
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // enum类型测试
        String str = "[{\"name\":\"paramsName_2897_0\",\"description\":\"paramsDesc_2897_0\",\"value\":\"paramsValue_2897_0\"},{\"name\":\"paramsName_2897_1\",\"description\":\"paramsDesc_2897_1\",\"value\":\"paramsValue_2897_1\"}]";
        List<PropertyEnumType> jsonArray = JSON.parseArray(str, PropertyEnumType.class);
        for (PropertyEnumType t : jsonArray) {
            System.out.println(t.getValue().substring(t.getValue().lastIndexOf("_") + 1, t.getValue().length()));
            System.out.println(t.getDescription() + " " + t.getName() +" " +t.getValue());
        }
        System.out.println(ConditionsTypeEnum.AND.getCondition());
    }
}
