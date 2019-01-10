package com.iot.ifttt.common;

/**
 * 描述：公式符号枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/29 14:15
 */
public enum IftttSignEnum {
    LARGER(">"),
    LARGER_EQUAL(">="),
    SMALLER("<"),
    SMALLER_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!=");

    private String sign;

    IftttSignEnum(String sign) {
        this.sign = sign;
    }

    public static IftttSignEnum getEnum(String sign) {
        for (IftttSignEnum vo : IftttSignEnum.values()) {
            if (vo.sign.equals(sign)) {
                return vo;
            }
        }
        return null;
    }
}
