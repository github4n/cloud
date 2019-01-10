package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public enum CMDEnum {

    STATUS_REPORT("A0", "状态上报"),
    QUERY_STATUS("A1", "状态查询"),
    ACM_COUNT("A2", "统计上报"),
    ACM_QUERY("A3", "统计查询"),
    PUSH_EVENT("A4", "推送事件"),
    HEART_BEAT("AA", "心跳"),
    REGISTER("B0", "注册"),
    GET_NET_CONFIG("B1", "获取网络配置"),
    PUSH_NET_CONFIG("B2", "推送网络配置"),
    SET_NET_CONFIG("B3", "设置网络配置"),
    PUSH_GW_CONFIG("B4", "推送电量信息"),
    GET_GW_CONFIG("B5", "获取电量信息"),
    SET_GW_CONFIG("B7", "设置电量信息"),
    USER_REGISTER("B9", "用户注册"),
    USER_MANAGER("BA", "用户管理"),
    COMMAND("D3", "控制设置命令");

    public String code;
    public String desc;

    CMDEnum (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
