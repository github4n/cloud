package com.iot.shcs.ipc.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频云聚合层异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    PARAM_ERROR("subscribe.Service.param.error"),
    /** 通知设备失败*/
    SUCCESS(200,"Success."),
    NOTIFY_DEVICE_FAILED(201,"Service.notify.device.failed"),
    PLANID_IS_NULL(201,"planId is null"),
    PLAN_IS_NOT_EXIST(202,"planId is not exist"),
    GET_URL_ERROR(203,"get url error"),
    DEVICEID_IS_INCORRECT(204,"planned unbound device or device information is incorrect."),
    ERROR(500,"system error"),
    AUTH_ERROR(401,"auth isn't enough");
    /**
     * 异常代码
     */
    private int code;
    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    BusinessExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }


    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }



}