package com.iot.common.exception;

import java.io.Serializable;

/**
 * <p>通用返回DTO</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime 2017年4月20日 下午5:58:01
 */
public class ExceptionResult<T> implements Serializable {

    private static final long serialVersionUID = 6095755606671547258L;

    private int code;

    /**
     * 状态描述
     */
    private String desc;

    /**
     * 返回数据
     */
    private T data;

    private Throwable subException;

    public ExceptionResult() {

    }

    public ExceptionResult(String msg) {
        this.desc = msg;
    }

    public ExceptionResult(int code, String msg, T data) {
        this.code = code;
        this.desc = msg;
        this.data = data;
    }

    public ExceptionResult(String desc, Throwable subException) {
        this.desc = desc;
        this.subException = subException;
    }

    public ExceptionResult(int code, String desc, Throwable subException) {
        this.code = code;
        this.desc = desc;
        this.subException = subException;
    }

    public ExceptionResult(int code, String desc, T data, Throwable subException) {
        this.code = code;
        this.desc = desc;
        this.data = data;
        this.subException = subException;
    }

    public ExceptionResult(int code, String msg) {
        this.code = code;
        this.desc = msg;
    }

    public int getCode() {
        return code;
    }

    /**
     * 设置状态码
     *
     * @param code 值必须满足 {@link ResultMsg#code}
     */
    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public ExceptionResult<T> setDesc(String msg) {
        this.desc = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getSubException() {
        return subException;
    }

    public void setSubException(Exception subException) {
        this.subException = subException;
    }
}
