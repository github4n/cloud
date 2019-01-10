package com.iot.shcs.security.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/11 14:09
 * 修改人:
 * 修改时间：
 */
public enum SecurityExceptionEnum implements IBusinessException {
    SECURITY_PASSWORD_IS_NULL(100, "security.password.is.null"),
    SECURITY_PASSWORD_IS_INCORRECT(101, "security.password.is.incorrect"),
    OLD_SECURITY_PASSWORD_IS_INCORRECT(102, "old.security.password.is.incorrect"),
    INPUT_OLD_SECURITY_PASSWORD(103, "input.old.security.password"),

    SECURITY_RULE_NOT_EXIST(110, "security.rule.not.exist"),
    SECURITY_NOT_CREATED(999, "security.not.created"),

    USER_PASSWORD_IS_INCORRECT(105,"user.password.is.incorrect"),
    // 未绑定网关
    UNBOUND_GATEWAY(10001, "unbound.gateway"),
    SET_SECURITY_STATUS_ERROR(113013, "set.security.status.error"),
    SAVE_RULE_ERROR(113020,"save.rule.error");

    private int code;

    private String messageKey;

    SecurityExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    SecurityExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
