package com.iot.control.ifttt.util;

import com.iot.control.ifttt.entity.*;
import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.RelationVo;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.TriggerVo;
import com.iot.control.ifttt.vo.req.SaveIftttReq;
import com.iot.control.ifttt.vo.req.SaveRuleReq;
import com.iot.control.ifttt.vo.res.RuleResp;

/**
 * 描述：bean字段赋值工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/15 16:53
 */
public class BeanCopyUtil {

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

    public static RuleResp getRuleResp(Rule source) {
        RuleResp target = new RuleResp();
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
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
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
}
