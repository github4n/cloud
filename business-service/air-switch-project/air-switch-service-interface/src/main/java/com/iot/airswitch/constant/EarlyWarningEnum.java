package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/16
 * @Description: 预警类型常量
 */
public enum EarlyWarningEnum {

    PHASE_LOSS(9, "输入缺相(380V)"),
    OVERVOLTAGE(12, "过压"),
    UNDERVOLTAGE(13, "欠压"),
    LEAKAGE(14, "漏电"),
    ELECTRICITY(15, "电流");

    public Integer type;
    public String desc;

    EarlyWarningEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(Integer t) {
        for (EarlyWarningEnum e : EarlyWarningEnum.values()) {
            if (e.type.equals(t)) {
                return e.desc;
            }
        }
        return null;
    }
}
