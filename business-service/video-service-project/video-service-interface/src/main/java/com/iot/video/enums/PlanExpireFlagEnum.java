package com.iot.video.enums;

/**
 * 项目名称：cloud
 * 功能描述：计划过期标志枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/9/3 17:07
 * 修改人： yeshiyuan
 * 修改时间：2018/9/3 17:07
 * 修改描述：
 */
public enum  PlanExpireFlagEnum {

    no_expire("0","未过期"),
    expire("1","已过期");

    PlanExpireFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /** 类型编码 */
    private String code;

    /** 描述 */
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
