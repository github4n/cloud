package com.iot.device.enums.product;

/**
 * 项目名称：cloud
 * 功能描述：产品流程状态
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 14:04
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 14:04
 * 修改描述：
 */
public enum ProductReviewProcessStatusEnum {

    WAIT_AUDIT(0,"待审核"),
    AUDIT_FAIL(1,"处理失败"),
    AUDIT_SUCCESS(2,"处理成功") ,
    LeaveMessage(3,"消息留言"),
    FeedbackMessage(4,"反馈消息");  ;

    private int value;
    private String desc;

    ProductReviewProcessStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
