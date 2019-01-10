package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:40 2018/5/9
 * @Modify by:
 */
public enum OtaExceptionEnum implements IBusinessException {

    /**OTA版本已出存在*/
    VERSION_EXIST("ota.version.exist"),

    /**计划配置异常*/
    CONFIG_ERROR("ota.config.error"),

    /**参数错误*/
    PARAM_IS_ERROR("param.is.error"),

    /**OTA版本创建失败*/
    OTA_VERSION_CREATE_FAILED("ota.version.create.failed"),

    /**启动或暂停计划太频繁*/
    OPERATE_PLAN_TOO_FREQUENCY("operate.plan.too.frequency"),

    /**启动状态不能编辑*/
    START_STATE_CAN_NOT_EDITED("start.state.cannot.be.edited"),

    /**OTA不能删除*/
    OTA_NOT_DELETE("ota.not.delete"),

    /**查询升级日志异常*/
    GET_UPGRADE_LOG_ERROR("get.upgrade.log.error"),

    /**查询计划操作日志异常*/
    GET_UPGRADE_PLAN_LOG_ERROR("get.upgrade.plan.log.error"),

    /**查询计划异常*/
    GET_UPGRADE_PLAN__ERROR("get.upgrade.plan.error"),

    /**操作计划异常*/
    OPERATE_UPGRADE_PLAN__ERROR("operate.upgrade.plan.error"),

    /**已经存在现网版本*/
    HAS_OTA_VERSION_IN_USE("an.ota.version.in.use"),

    /**编辑计划异常*/
    EDIT_UPGRADE_PLAN__ERROR("edit.upgrade.plan.error"),

    /**固件删除异常*/
    DELETE_FIRMWARE_ERROR("delete.firmware.error"),

    /**保存策略配置异常*/
    SAVE_STRATEGY_CONFIG_ERROR("save.strategy.config.error"),

    /**查询异常*/
    QUERY_ERROR("query.error");

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
