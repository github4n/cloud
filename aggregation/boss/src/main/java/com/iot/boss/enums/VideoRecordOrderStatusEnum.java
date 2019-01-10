package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：视频订单状态
 * 创建人： yeshiyuan
 * 创建时间：2018/5/23 14:44
 * 修改人： yeshiyuan
 * 修改时间：2018/5/23 14:44
 * 修改描述：
 */
public enum VideoRecordOrderStatusEnum {

    PAY_SUCCESS(0,"支付成功"),
    SERVER_OPEN(1,"服务开通"),
    SERVER_EXPIRE(2,"服务过期"),
    SERVER_CLOSE(3,"服务关闭"),
    REFUND_SUCCESS(4,"退款成功"),
    REFUND_ERROR(5,"退款失败");

    private Integer value;

    private String desc;

    VideoRecordOrderStatusEnum(Integer value, String desc) {
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

    public static String getDescByValue(Integer value){
        String desc = "";
        for (VideoRecordOrderStatusEnum orderStatusEnum: VideoRecordOrderStatusEnum.values()) {
            if (value == orderStatusEnum.getValue()){
                desc = orderStatusEnum.getDesc();
                break;
            }
        }
        return desc;
    }
}
