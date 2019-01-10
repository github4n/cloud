package com.iot.user.exception;

import com.iot.common.exception.IBusinessException;


/**
 * 描述：用户异常枚举类，代码范围（21001~22000）
 * 创建人： laiguiming
 * 创建时间： 2018年4月09日 下午17:13:00
 */
public enum UserExceptionEnum implements IBusinessException {

    /************************************（21001~22000）************************************/

    USER_IS_EXIST(21001, "user.is.exist"),
    USER_IS_NOT_EXIST(21002, "user.is.not.exist"),
    USERNAME_IS_NULL(21003, "userName.is.null"),
    PASSWORD_IS_ERROR(21004, "password.is.error"),
    USERID_IS_NULL(21005, "userId.is.null"),
    VERIFYCODE_IS_NULL(21049, "verifyCode.is.null"),
    TENANTID_IS_NULL(21007, "tenantId.is.null"),
    REDIS_VERIFYCODE_IS_NULL(21049, "redis.verifyCode.is.null"),
    RESET_PWD_VERIFYCODE_ERROR(21009, "reset.pwd.verifyCode.error"),
    REGISTER_VERIFYCODE_ERROR(21010, "register.verifyCode.error"),
    LOGIN_FAIL_VERIFYCODE_ERROR(21049, "login.fail.verifyCode.error"),
    VERIFYCODE_TYPE_ERROR(21012, "verifyCode.type.error"),
    LOGIN_FAIL(21013, "login.fail"),
    PASSWORD_NEED_VERIFY(21049, "password.need.verifycode"),
    ACCOUNT_IS_LOCKED_1MIN(21060,"account.is.locked.1min"),
    ACCOUNT_IS_LOCKED_3MIN(21061,"account.is.locked.3min"),
    USER_HEAD_IMG_IS_NULL(21015, "user.headimg.is.null"),
    USER_LOGIN_IS_NOT_EXIST(21016, "user.login.is.not.exist"),
    USER_BACKGROUND_IS_NULL(21017, "user.background.is.null"),
    UUID_IS_NULL(21018, "uuid.is.null"),
    USER_NOT_REGISTER(21019, "user.not.register"),
    PWD_IS_NULL(21020, "pwd.is.null"),
    PARAM_IS_ERROR(21021, "param.is.error"),
    TENANT_INFO_IS_NULL(21022, "tenant.info.is.null"),
    TENANT_INFO_IS_NOT_COMPLETE(21023, "tenant.info.is.not.complete"),
    PARAM_SIZE_IS_TOO_LARGE(21023, "param.size.is.too.large"),
    USER_STATUS_IS_NULL(21024, "user.status.is.null"),
    SYSTEM_ERROR(21029, "system.error"),
    /***用户token拦截异常***/
    AUTH_LOGIN_RETRY(21025, "auth.login.retry"),
    AUTH_REFRESH(21026, "auth.refresh"),
    AUTH_HEADER_ILLEGAL(21027, "auth.header.illegal"),
    AUTH_REFRESH_ERROR(21028, "auth.refresh.error"),
	MAIL_TOO_FREQUENT(21029, "The message has been sent and is not allowed to push within one minute."),
	PARAM_CONTAIN_SPECIAL_CHAR(21030, "parameters contain special characters"),
	LOCK_10_MINUTES(21031, "Your account has 3 consecutive password errors, please try to log in 10 minutes."),
	LOCK_1_HOUR(21032, "Your account has 6 consecutive password errors, please try to log in 1 hour."),
	LOCK_24_HOURS(21033, "Your account has 12 consecutive password errors, please try to log in 24 hours."),
    OLD_PASSWORD_IS_ERROR(21034, "old.password.is.error"),
    USER_DO_NOT_HAVE_PERMISSION(21035, "user.do.not.have.permission"),
    ACCOUNT_UNAUDIT(21036, "user.account.is.unaudit"),
    ACCOUNT_AUDIT_FAILED(21037, "user.account.audit.failed"),

    USERLEVEL_IS_NULL(21038, "userLevel.is.null"),
    AUDIT_NOT_PASS(21039, "Account has not been approved"),
    ACCOUNT_LOCKED(21040, "Account locked"),
    ACCESSTOKEN_IS_ERROR(21041, "accessToken.is.error"),

    NO_AUTHORITY(21041, "no.authority")


    ;
    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    UserExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
