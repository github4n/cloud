package com.iot.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iot.common.exception.ResultMsg;
import com.iot.common.exception.StatusResult;
import io.swagger.annotations.*;

import java.io.Serializable;

/**
 * <p>通用返回DTO</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime 2017年4月20日 下午5:58:01
 */
@ApiModel("通用的返回模板")
@ApiResponses({
        @ApiResponse(code = 200, message = "成功"),
        @ApiResponse(code = 0, message = "失败"),
        @ApiResponse(code = -1, message = "异常"),
        @ApiResponse(code = -2, message = "请重新登录")}
)
public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = 6095755606671547258L;

    /**
     * 状态码
     * 值必须满足 {@link ResultMsg#code}
     */
    @ApiModelProperty(name = "code", value = "状态码", example = "200")
    private int code;

    /**
     * 状态描述
     */
    @ApiModelProperty(name = "desc", value = "状态描述", example = "成功")
    private String desc;

    /**
     * 详细编码
     */
    //private int detailCode;


    /**
     * 返回数据
     */
    @ApiModelProperty(name = "data", value = "返回数据")
    private T data;


    public CommonResponse() {
    }

    public CommonResponse(StatusResult statusResult) {
        this(statusResult, null, null);
    }

    public CommonResponse(StatusResult statusResult, T data) {
        this(statusResult, null, data);
    }

    public CommonResponse(StatusResult statusResult, String msg, T data) {
        this.code = statusResult.getCode();
        this.desc = msg;
        this.data = data;
    }

    public CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.desc = msg;
        this.data = data;
    }

    public CommonResponse(int code, String msg) {
        this.code = code;
        this.desc = msg;
    }

    /**
     * <p>成功</p>
     *
     * @dateTime 2017/6/1 12:32
     * @author xiangyitao
     */
    public static <T> CommonResponse<T> success() {
        return success(null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(ResultMsg.SUCCESS).data(data);
    }

    /**
     * <p>是否成功</p>
     *
     * @dateTime 2017/6/1 12:32
     * @author xiangyitao
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultMsg.SUCCESS.getCode();
    }

    /**
     * <p>是否失败</p>
     *
     * @dateTime 2017/6/1 12:32
     * @author xiangyitao
     */
    @JsonIgnore
    public boolean isError() {
        return this.code != ResultMsg.SUCCESS.getCode();
    }

    /**
     * <p>方法连 修改状态信息</p>
     *
     * @dateTime 2017/6/1 12:43
     * @author xiangyitao
     */
    @JsonIgnore
    public CommonResponse<T> msg(String msg) {
        this.desc = msg;
        return this;
    }

    /**
     * <p>方法连 添加数据</p>
     *
     * @dateTime 2017/6/1 12:43
     * @author xiangyitao
     */
    @JsonIgnore
    public CommonResponse<T> data(T data) {
        this.data = data;
        return this;
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

    public CommonResponse<T> setDesc(String msg) {
        this.desc = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResponse [code=" + code + ", desc=" + desc + ", data=" + data + "]";
    }

}
