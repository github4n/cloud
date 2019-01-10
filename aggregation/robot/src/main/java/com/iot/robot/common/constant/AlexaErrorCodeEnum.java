package com.iot.robot.common.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/10 14:29
 * @Modify by:
 */
public enum AlexaErrorCodeEnum {
    // 用户技能未启用
    SKILL_DISABLED_EXCEPTION("SKILL_DISABLED_EXCEPTION", "The user does not have a valid enablement for your skill.");


    private String code;
    private String codeDesc;

    AlexaErrorCodeEnum(String code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }
}
