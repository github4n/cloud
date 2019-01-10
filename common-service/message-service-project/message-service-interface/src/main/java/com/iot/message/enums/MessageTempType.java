package com.iot.message.enums;

/**
 * 项目名称：IOT云平台
 * 模块名称：消息服务
 * 功能描述：消息模板枚举
 * 创建人： mao2080@sina.com
 * 创建时间：2018/4/24 14:45
 * 修改人： mao2080@sina.com
 * 修改时间：2018/4/24 14:45
 * 修改描述：
 */
public enum MessageTempType {

    /*******************************英文模板*******************************/
    EN00001("EN00001", "appuser activate"),

    EN00002("EN00002", "admin reset password"),

    EN00003("EN00003", "appuser share"),

    EN00004("EN00004", "comedueplan comedue remind"),

    EN00005("EN00005", "customer activate"),

    EN00006("EN00006", "inperiodplan comedue remind"),

    EN00007("EN00007", "outdateplan comedue remind"),

    EN00008("EN00008", "payrefund audit"),

    EN00009("EN00009", "reset password"),

    EN00010("EN00010", "share device"),

    EN00011("EN00011", "template 1"),

    EN00012("EN00012", "User Warning Processing Schedule"),

    /*******************************中文模板*******************************/
    CH00001("CH00001", "用户激活"),

    CH00002("CH00002", "管理员重置密码"),

    CH00003("CH00003", "账号分享"),

    CH00004("CH00004", "计划到期提醒"),

    CH00005("CH00005", "客户激活"),

    CH00006("CH00006", "临期邮件提醒"),

    CH00007("CH00007", "过期邮件提醒"),

    CH00008("CH00008", "退款审核"),

    CH00009("CH00009", "重置密码"),

    CH00010("CH00010", "设备分享"),

    CH00011("CH00011", "测试模板"),

    CH00012("CH00012", "用户报障提醒"),

    /*******************************通用模板*******************************/
    AND00001("AND00001", "Android"),

    IOS00001("IOS00001", "IOS"),

    MQTT00001("MQTT00001", "MQTT"),

    SMS00001("SMS00001", "SMS")

    ;

    /**
     * 用户类型编码
     */
    private String tempId;

    /**
     * 用户类型名称
     */
    private String tempName;


    /**
     * 描述：模板类型
     * @param tempId 模板id
     * @param tempName 模板名称
     * @return
     * @author mao2080@sina.com
     * @created 2018/4/24 14:45
     * @since
     */
    private MessageTempType(String tempId, String tempName) {
        this.tempId = tempId;
        this.tempName = tempName;
    }

    public String getTempId() {
        return tempId;
    }

    public String getTempName() {
        return tempName;
    }

}
