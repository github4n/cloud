package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/16
 * @Description: *
 */
public enum AlarmEnum {

    SHORT(0, "短路"),
    SURGE(1, "浪涌"),
    OVERLOAD(2, "过载"),
    TEMPERATURE(3, "温度"),
    LEAKAGE(4, "漏电"),
    OVERCURRENT(5, "过流"),
    OVERVOLTAGE(6, "过压"),
    LEAKAGE_UNFINISH(8, "漏电保护自检未完成"),
    FIRE(10, "打火"),
    UNDERVOLTAGE(11, "欠压"),
    IMBALANCE(22, "不平衡"),
    PHASE_SEQUENCE(23, "相序ACB");

    public Integer type;
    public String desc;

    AlarmEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(Integer t) {
        for (AlarmEnum e : AlarmEnum.values()) {
            if (e.type.equals(t)) {
                return e.desc;
            }
        }
        return null;
    }
}
