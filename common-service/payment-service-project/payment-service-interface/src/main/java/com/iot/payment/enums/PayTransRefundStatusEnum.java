package com.iot.payment.enums;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/28 11:08
 * 修改人： yeshiyuan
 * 修改时间：2018/5/28 11:08
 * 修改描述：
 */
public enum PayTransRefundStatusEnum {

    NO_REFUND(0,"未退款"),
    REFUNDING(1,"退款中"),
    SUCCESS(2,"退款成功"),
    FAIL(3,"退款失败");
    private Integer value;

    private String desc;

    PayTransRefundStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
