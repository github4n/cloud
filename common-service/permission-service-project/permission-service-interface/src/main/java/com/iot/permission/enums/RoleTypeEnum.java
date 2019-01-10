package com.iot.permission.enums;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：权限
 * 功能描述：角色类型枚举
 * 创建人： maochengyuan
 * 创建时间：2018/7/14 10:28
 * 修改人： maochengyuan
 * 修改时间：2018/7/14 10:28
 * 修改描述：
 */
public enum RoleTypeEnum {

    Boss("Boss", "Boss"),

    TOB("Portal", "Portal"),

    TOC("2C", "2C")

	;

    RoleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private String code;

	/** 类型描述 */
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
