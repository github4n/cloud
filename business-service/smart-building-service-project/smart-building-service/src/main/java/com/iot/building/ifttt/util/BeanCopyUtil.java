package com.iot.building.ifttt.util;


import com.iot.building.ifttt.entity.*;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.RelationVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.req.SaveRuleReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.utils.ValueUtils;

/**
 * 描述：bean字段赋值工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/15 16:53
 */
public class BeanCopyUtil {

    public static Actuator getActuator(ActuatorVo source) {
        Actuator target = new Actuator();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLabel(source.getLabel());
        target.setProperties(source.getProperties());
        target.setDeviceId(source.getDeviceId());
        target.setRuleId(source.getRuleId());
        Long[] position = source.getPosition();
        if (position != null && position.length > 0) {
            target.setPosition(ValueUtils.join(source.getPosition(), ","));
        }
        target.setType(source.getType());
        target.setIdx(source.getIdx());
        target.setDelay(source.getDelay());
        target.setProductId(source.getProductId());
        target.setObjectId(source.getObjectId());
        return target;
    }

    public static ActuatorVo getActuatorVo(Actuator source) {
        ActuatorVo target = new ActuatorVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLabel(source.getLabel());
        target.setProperties(source.getProperties());
        target.setDeviceId(source.getDeviceId());
        target.setRuleId(source.getRuleId());
        if (source.getPosition() != null) {
            String[] pArr = source.getPosition().split(",");
            Long[] iArr = new Long[pArr.length];
            for (int i = 0; i < pArr.length; i++) {
                iArr[i] = Long.valueOf(pArr[i]);
            }
            target.setPosition(iArr);
        }
        target.setType(source.getType());
        target.setIdx(source.getIdx());
        target.setDelay(source.getDelay());
        target.setProductId(source.getProductId());
        target.setObjectId(source.getObjectId());
        return target;
    }

    public static Rule getRule(SaveRuleReq source) {
        Rule target = new Rule();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setIcon(source.getIcon());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setIsMulti(source.getIsMulti());
        target.setLocationId(source.getLocationId());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUserId(source.getUserId());
        target.setDirectId(source.getDirectId());
        target.setTemplateFlag(source.getTemplateFlag());
        target.setProductId(source.getProductId());
        target.setRuleType(source.getRuleType());
        target.setSecurityType(source.getSecurityType());
        target.setDelay(source.getDelay());
        target.setIftttType(source.getIftttType());
        target.setTemplateId(source.getTemplateId());
        return target;
    }

    public static Rule getRule(SaveIftttReq source) {
        Rule target = new Rule();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setDeployMentId(source.getDeployMentId());
        target.setName(source.getName());
        target.setIcon(source.getIcon());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setIsMulti(source.getIsMulti());
        target.setLocationId(source.getLocationId());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUserId(source.getUserId());
        target.setDirectId(source.getDirectId());
        target.setTemplateFlag(source.getTemplateFlag());
        target.setProductId(source.getProductId());
        target.setRuleType(source.getRuleType());
        target.setSecurityType(source.getSecurityType());
        target.setDelay(source.getDelay());
        target.setIftttType(source.getIftttType());
        target.setTemplateId(source.getTemplateId());
        target.setOrgId(source.getOrgId());
        target.setShortcut(source.getShortcut());
        return target;
    }

    public static RuleResp getRuleResp(Rule source) {
        RuleResp target = new RuleResp();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setDeployMentId(source.getDeployMentId());
        target.setDeployMentName(source.getDeployMentName());
        target.setName(source.getName());
        target.setIcon(source.getIcon());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setIsMulti(source.getIsMulti());
        target.setLocationId(source.getLocationId());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUserId(source.getUserId());
        target.setDirectId(source.getDirectId());
        target.setTemplateFlag(source.getTemplateFlag());
        target.setProductId(source.getProductId());
        target.setRuleType(source.getRuleType());
        target.setSecurityType(source.getSecurityType());
        target.setDelay(source.getDelay());
        target.setIftttType(source.getIftttType());
        target.setTemplateId(source.getTemplateId());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setShortcut(source.getShortcut());
        return target;
    }

    public static RelationVo getRelationVo(Relation source) {
        RelationVo target = new RelationVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLabel(source.getLabel());
        target.setType(source.getType());
        target.setRuleId(source.getRuleId());
        if (source.getPosition() != null) {
            String[] pArr = source.getPosition().split(",");
            Long[] iArr = new Long[pArr.length];
            for (int i = 0; i < pArr.length; i++) {
                iArr[i] = Long.valueOf(pArr[i]);
            }
            target.setPosition(iArr);
        }
        if (source.getParentLabels() != null) {
            target.setParentLabels(source.getParentLabels().split(","));
        }
        return target;
    }

    public static Relation getRelation(RelationVo source) {
        Relation target = new Relation();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLabel(source.getLabel());
        target.setType(source.getType());
        target.setRuleId(source.getRuleId());
        String[] parentLabels = source.getParentLabels();
        if (parentLabels != null && parentLabels.length > 0) {
            target.setParentLabels(String.join(",", source.getParentLabels()));
        }
        Long[] position = source.getPosition();
        if (position != null && position.length > 0) {
            target.setPosition(ValueUtils.join(source.getPosition(), ","));
        }
        return target;
    }

    public static Sensor getSensor(SensorVo source) {
        Sensor target = new Sensor();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLabel(source.getLabel());
        target.setProperties(source.getProperties());
        target.setType(source.getType());
        target.setDeviceId(source.getDeviceId());
        target.setRuleId(source.getRuleId());
        if (source.getPosition() != null && source.getPosition().length > 0) {
            target.setPosition(ValueUtils.join(source.getPosition(), ","));
        }
        target.setTiming(source.getTiming());
        target.setIdx(source.getIdx());
        target.setDelay(source.getDelay());
        target.setProductId(source.getProductId());
        return target;
    }

    public static SensorVo getSensorVo(Sensor source) {
        SensorVo target = new SensorVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLabel(source.getLabel());
        target.setProperties(source.getProperties());
        target.setType(source.getType());
        target.setDeviceId(source.getDeviceId());
        target.setRuleId(source.getRuleId());
        if (source.getPosition() != null && source.getPosition().contains(",")) {
            String[] pArr = source.getPosition().split(",");
            Long[] iArr = new Long[pArr.length];
            for (int i = 0; i < pArr.length; i++) {
                iArr[i] = Long.valueOf(pArr[i]);
            }
            target.setPosition(iArr);
        }
        target.setTiming(source.getTiming());
        target.setIdx(source.getIdx());
        target.setDelay(source.getDelay());
        target.setProductId(source.getProductId());
        return target;
    }

    public static TriggerVo getTriggerVo(Trigger source) {
        TriggerVo target = new TriggerVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLineId(source.getLineId());
        target.setSourceLabel(source.getSourceLabel());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setDestinationLabel(source.getDestinationLabel());
        target.setInvocationPolicy(source.getInvocationPolicy());
        target.setStatusTrigger(source.getStatusTrigger());
        target.setRuleId(source.getRuleId());
        return target;
    }

    public static Trigger getTrigger(TriggerVo source) {
        Trigger target = new Trigger();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLineId(source.getLineId());
        target.setSourceLabel(source.getSourceLabel());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setDestinationLabel(source.getDestinationLabel());
        target.setInvocationPolicy(source.getInvocationPolicy());
        target.setStatusTrigger(source.getStatusTrigger());
        target.setRuleId(source.getRuleId());
        return target;
    }
}
