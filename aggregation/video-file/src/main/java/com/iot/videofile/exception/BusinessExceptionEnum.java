package com.iot.videofile.exception;

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

    PARAM_ERROR("param.error");
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