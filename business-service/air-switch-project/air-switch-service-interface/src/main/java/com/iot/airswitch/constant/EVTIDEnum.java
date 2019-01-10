package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: 告警类型
 */
public enum EVTIDEnum {

    NODE_ON("30", "节点上线"),
    NODE_OFF("31", "节点下线"),
    NODE_CHANGE("32", "节点变换"),
    UNIPHASE_CHANGE("34", "节点报警改变"),
    TRIPHASE_A_CHANGE("35", "三相节点A报警改变"),
    TRIPHASE_B_CHANGE("36", "三相节点B报警改变"),
    TRIPHASE_C_CHANGE("37", "三相节点C报警改变"),
    TIME_CHANGE("39", "时间改变"),
    RUN_STATUS_CHANGE("41", "电器运行状态改变");

    public String code;
    public String desc;

    EVTIDEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (EVTIDEnum e : EVTIDEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("5BCFE8B6", 16));
    }

}
