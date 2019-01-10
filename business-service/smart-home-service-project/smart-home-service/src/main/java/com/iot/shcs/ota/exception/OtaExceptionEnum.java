package com.iot.shcs.ota.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 描述：ota
 * @author nongchongwei
 * @date 2018/11/19 10:34
 * @param
 * @return
 */
public enum OtaExceptionEnum implements IBusinessException {

    /**OTA版本已出存在*/
    VERSION_EXIST("ota.version.exist"),

    /**消息重复*/
    DUPLICATE_MESSAGES_ERROR("duplicate.messages.error"),

    /**计划配置异常*/
    CONFIG_ERROR("ota.config.error"),

    /**更新计划失败（启动）*/
    UPDATE_PLAN_STATUS_ERROR("update.plan.status.error"),

    /**获取用户设备关系失败*/
    GET_USER_DEVICE_ERROR("get.user.device.error"),

    /**获取设备失败*/
    GET_DEVICE_ERROR("get.device.error"),

    /**策略配置要升级的设备总量太大*/
    STRATEGY_TOTAL_TOO_LARGE("strategy.config.upgrade.num.too.large.error"),

    ;

    private int code;

    private String messageKey;

    OtaExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    OtaExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
