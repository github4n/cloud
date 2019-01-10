package com.iot.system.enums;
/**
 * 项目名称：cloud
 * 功能描述：字典表类型枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
public enum DictTypeEnum {

    GOODS_STANDARD_UNIT((short)1, "商品规格单位"),

    ORDER_TYPE((short)2, "订单类型"),

    ORDER_STATUS((short)3, "订单状态"),

    GOODS_TYPE((short)4,"商品类型"),

    UUID_ORDER_STATUS((short)5,"UUID订单状态"),

    UUID_ACTIVE_STATUS((short)6,"UUID激活状态"),

    UUID_PAY_STATUS((short)7,"UUID支付状态"),

    UUID_PAY_SKIP_URL((short)11,"UUID支付跳转URL"),

    GOOGLE_PAY_SKIP_URL((short)12,"Google接入支付跳转URL"),

    APP_PAY_SKIP_URL((short)13,"APP打包跳转URL"),

    PACKAGE_TYPE((short)14, "套包类型");

    DictTypeEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private Short code;

	/** 描述 */
    private String desc;

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
