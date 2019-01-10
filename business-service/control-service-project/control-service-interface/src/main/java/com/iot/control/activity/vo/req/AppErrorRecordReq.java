package com.iot.control.activity.vo.req;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description: app error record
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/15 11:01
 **/
public class AppErrorRecordReq implements Serializable {

    @NotNull(message = "userId.notnull")
    private String userId;

    private Integer code;

    private String message;

    private Long timeStamp;

    private String path;

    private Integer pageSize;

    private Integer pageNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
