package com.iot.tenant.enums;

/**
 * 项目名称：cloud
 * 功能描述：app能否使用枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:58
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:58
 * 修改描述：
 */
public enum AppCanUsedEnum {

    USED("1","能用"),
    FORBIDDEN("0","禁用");


    private String value;

    private String desc;

    AppCanUsedEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
