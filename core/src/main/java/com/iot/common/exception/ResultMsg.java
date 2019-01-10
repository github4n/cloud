package com.iot.common.exception;

/**
 * <p>基本状态信息</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime 2017/12/1 11:49
 */
public enum ResultMsg implements StatusResult {

    SUCCESS(200, "成功"),
    FAIL(0, "失败"),
    EXCEPTION(-1, "异常"),
    LOGIN_ERROR(-2, "请重新登录"),
    /**
     * HTTP STATUS CODE
     */
    HTTP_STATUS_CODE_400(400, "Bad Request"),
    HTTP_STATUS_CODE_405(405, "Method Not Allowed"),
    HTTP_STATUS_CODE_415(415, "Unsupported Media Type"),
    HTTP_STATUS_CODE_500(500, "Internal Server Error"),

    /**
     * 系统错误  01000
     */
    HTTP_PARAM_ERROR(1000, "参数错误"),
    BUSINESS_PARAM_ASSERT_ERROR(1002, "业务逻辑验证参数错误"),
    REDIS_KEY_NORMATIVE_ERROR(1003, "redis的key规范错误,"),
    COMMON_FILE_FOLDER_RE_NAME(1004, "文件夹重名"),
    COMMON_FILE_FOLDER_PARENT_INEXISTENCE(1004, "父文件夹不存在"),
    COMMON_FILE_FOLDER_MOST_LEVEL(1005, "最多只能创建5级文件夹"),
    COMMON_FILE_FOLDER_INEXISTENCE(1006, "文件夹不存在"),
    COMMON_FILE_UPLOAD_IO_EXCEPTION(1007, "IO异常"),
    COMMON_FILE_INEXISTENCE(1008, "文件不存在"),
    INDEX_UPDATE_ERROR(1100, "索引更新错误"),
    INDEX_QUERY_ERROR(1101, "索引查询错误");

    /**
     * 状态
     */
    private final int code;
    /**
     * 状态描述
     */
    private String msg;

    ResultMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
