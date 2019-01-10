package com.iot.ifttt.common;

/**
 * 描述：校验类型枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 19:37
 */
public enum IftttTypeEnum {
    TIMER("timer"), //定时
    DEV_STATUS("devStatus"); //设备状态变化

    private String type;

    IftttTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static IftttTypeEnum getEnum(String type) {
        for (IftttTypeEnum vo : IftttTypeEnum.values()) {
            if (vo.type.equals(type)) {
                return vo;
            }
        }
        return null;
    }
}
