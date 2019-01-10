package com.iot.system.exception;


import com.iot.common.exception.IBusinessException;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：文件服务异常枚举
 * 创建人： 李帅
 * 创建时间：2018年8月13日 下午3:05:50
 * 修改人：李帅
 * 修改时间：2018年8月13日 下午3:05:50
 */
public enum BusinessExceptionEnum implements IBusinessException {

    /**未知异常*/
    UNKNOWN_EXCEPTION("systemservice.unknow.exception"),
    ;

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

}