package com.iot.common.exception;

import com.iot.common.beans.CommonResponse;

/**
 * <p>类的详细说明</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime 2017/6/1 12:35
 */
public interface StatusResult {

    /**
     * 返回错误状态
     *
     * @return
     */
    int getCode();

    /**
     * 返回错误信息
     *
     * @return
     */
    String getMsg();

    /**
     * <p>默认方法 info信息</p>
     *
     * @return
     * @dateTime 2017/6/1 12:35
     * @author xiangyitao
     */
    default <T> CommonResponse<T> info() {
        return new CommonResponse<>(this);
    }

    default <T> CommonResponse<T> info(int code, String msg) {
        return new CommonResponse<>(code, msg);
    }

    default <T> CommonResponse<T> info(int code, String msg, T data) {
        return new CommonResponse<>(code, msg, data);
    }

    default <T> CommonResponse<T> info(Exception exception) {
        return new CommonResponse<>(this, exception.getMessage(),  null);
    }

    /**
     * <p>默认方法 info信息</p>
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     * @dateTime 2017/6/1 12:57
     * @author xiangyitao
     */
    default <T> CommonResponse<T> info(String msg, T data) {
        return new CommonResponse<>(this, msg, data);
    }

    /**
     * <p>默认方法 info信息</p>
     *
     * @param data
     * @param <T>
     * @return
     * @dateTime 2017/6/1 12:57
     * @author xiangyitao
     */
    default <T> CommonResponse<T> info(T data) {
        return new CommonResponse<>(this, data);
    }

}
