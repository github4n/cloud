package com.iot.video.enums;

/**
 * 项目名称：cloud
 * 功能描述：video_pay_record的plan_status枚举值
 * 创建人： yeshiyuan
 * 创建时间：2018/5/22 14:18
 * 修改人： yeshiyuan
 * 修改时间：2018/5/22 14:18
 * 修改描述：
 */
public enum VprPlanStatusEnum {

    //1-使用中 2-到期 3-过期定时扫描需要分布式锁 4:退款，计划失效
    USING(1,"使用中"),
    DAOQI(2,"到期"),
    GUOQI(3,"过期"),
    REFUND(4,"退款")
    ;

    private VprPlanStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /** 类型编码 */
    private int code;

    /** 描述 */
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
