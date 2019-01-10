package com.iot.boss.enums.refund;

/**
 * 项目名称：cloud
 * 功能描述：计划状态
 * 创建人： 490485964@qq.com
 * 创建时间：2018/5/21 9:50
 * 修改人： 490485964@qq.com
 * 修改时间：2018/5/21 9:50
 * 修改描述：
 */
public enum PlanStatusEnum {

    USE(1,"使用中"),
    EXPIRED(2,"到期"),
    EXPIREDLOCK(3,"过期定时扫描需要分布式锁"),
    REFUND(4,"退款，计划失效");
    private Integer value;

    private String desc;

    PlanStatusEnum(Integer value, String desc) {
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
        for (PlanStatusEnum refundStatusEnum: PlanStatusEnum.values()) {
            if (refundStatus == refundStatusEnum.getValue()){
                result = true;
                break;
            }
        }
        return result;
    }
}
