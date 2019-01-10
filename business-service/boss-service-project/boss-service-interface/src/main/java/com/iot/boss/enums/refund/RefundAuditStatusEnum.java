package com.iot.boss.enums.refund;

/**
 * 项目名称：cloud
 * 功能描述：退款审批结果枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 9:46
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 9:46
 * 修改描述：
 */
public enum RefundAuditStatusEnum {
    WAIT_AUDIT(0,"待审核"),
    PASS(1,"审核通过"),
    NO_PASS(2,"审核不通过");
    private Integer value;

    private String desc;

    RefundAuditStatusEnum(Integer value, String desc) {
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
