package com.iot.boss.enums.refund;

/**
 * 项目名称：cloud
 * 功能描述：退款日志状态
 * 创建人： yeshiyuan
 * 创建时间：2018/5/22 9:41
 * 修改人： yeshiyuan
 * 修改时间：2018/5/22 9:41
 * 修改描述：
 */
public enum  RefundLogStatusEnum {
    CANCEL(0,"取消"),
    APPLY(1,"退款申请中"),
    SUCCESS(2,"退款成功"),
    FAIL(3,"退款失败"),
    AUDIT_PASS(4,"审核通过"),
    AUDIT_NO_PASS(5,"审核不通过");
    private Integer value;

    private String desc;

    RefundLogStatusEnum(Integer value, String desc) {
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

    public static boolean checkRefundStatus(Integer refundStatus){
        boolean result = false;
        for (RefundLogStatusEnum refundLogStatusEnum: RefundLogStatusEnum.values()) {
            if (refundStatus == refundLogStatusEnum.getValue()){
                result = true;
                break;
            }
        }
        return result;
    }
}
