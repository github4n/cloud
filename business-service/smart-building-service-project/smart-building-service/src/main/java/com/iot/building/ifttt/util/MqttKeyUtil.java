package com.iot.building.ifttt.util;

import com.iot.mqttsdk.common.MqttMsg;

/**
 * 描述：mqtt协议主题工具
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/13 9:34
 */
public class MqttKeyUtil {
    public static final String TOPIC_CLIENT_PREFIX = "iot/v1/c/";
    public static final String TOPIC_CLIENT_SPACER = "/automation/";

    //协议主题
    public static final String DEL_AUTO_REQ = "delAutoReq";
    public static final String DEL_AUTO_RESP = "delAutoResp";
    public static final String GET_AUTO_RULE_RESP = "getAutoRuleResp";
    public static final String ADD_AUTO_RULE_REQ = "addAutoRuleReq";
    public static final String ADD_AUTO_RULE_RESP = "addAutoRuleResp ";
    public static final String EDIT_AUTO_RULE_REQ = "editAutoRuleReq";
    public static final String EDIT_AUTO_RULE_RESP = "editAutoRuleResp";
    public static final String SET_AUTO_ENABLE_REQ = "setAutoEnableReq";
    public static final String SET_AUTO_ENABLE_RESP = "setAutoEnableResp";

    public static MqttMsg getDelAutoReqMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, DEL_AUTO_REQ);
    }

    public static MqttMsg getDelAutoRespMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, DEL_AUTO_RESP);
    }

    public static MqttMsg getGetAutoRuleRespMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, GET_AUTO_RULE_RESP);
    }

    public static MqttMsg getAddAutoRuleReqMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, ADD_AUTO_RULE_REQ);
    }

    public static MqttMsg getAddAutoRuleRespMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, ADD_AUTO_RULE_RESP);
    }

    public static MqttMsg getEditAutoRuleReqMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, EDIT_AUTO_RULE_REQ);
    }

    public static MqttMsg getEditAutoRuleRespMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, EDIT_AUTO_RULE_RESP);
    }

    public static MqttMsg getSetAutoEnableReqMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, SET_AUTO_ENABLE_REQ);
    }

    public static MqttMsg getSetAutoEnableRespMsg(MqttMsg msg, String id) {
        return commonMsg(msg, id, SET_AUTO_ENABLE_RESP);
    }

    private static MqttMsg commonMsg(MqttMsg msg, String id, String method) {
        MqttMsg res = msg;
        if(res == null){
            res = new MqttMsg();
        }
        res.setTopic(TOPIC_CLIENT_PREFIX + id + TOPIC_CLIENT_SPACER + method);
        res.setService("automation");
        res.setMethod(method);
        return res;
    }
}
