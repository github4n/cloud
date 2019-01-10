package com.iot.common.exception;


import java.io.Serializable;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：异常处理类
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 下午17:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 下午17:13:00
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2684365557861576910L;

    private Integer code;

    private Object[] args;

    public BusinessException(IBusinessException exception) {
        super(exception.getMessageKey());
        this.code = exception.getCode();
    }

    public BusinessException(IBusinessException exception, Object... args) {

        super(exception.getMessageKey());
        this.code = exception.getCode();
        this.args = args;
    }

    public BusinessException(IBusinessException exception, Throwable throwable) {
        super(exception.getMessageKey(), throwable);
        this.code = exception.getCode();
    }


    public BusinessException(IBusinessException exception, Throwable throwable, Object... args) {

        super(exception.getMessageKey(), throwable);
        this.code = exception.getCode();
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}