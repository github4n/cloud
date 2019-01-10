package com.iot.system.enums;

/**
 * 项目名称：cloud
 * 功能描述：商品规格单位枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/8/28 9:59
 * 修改人： yeshiyuan
 * 修改时间：2018/8/28 9:59
 * 修改描述：
 */
public enum StandardUnitEnum {
    CI(1, "次"),
    YEAR(2, "年"),
    GE(3, "个"),
    EVENT(4, "事件"),
    DAY(5, "天");

    StandardUnitEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;

    public Integer getCode() {

        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
