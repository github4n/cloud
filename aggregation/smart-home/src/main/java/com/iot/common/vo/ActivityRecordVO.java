package com.iot.common.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "活动日志参数", description = "设备、情景、联动活动日志参数")
public class ActivityRecordVO implements Serializable {

    private static final long serialVersionUID = -4120697549382646533L;

    private String cookieUserId;

    private String cookieUserToken;

    private String devId;

    private String type;

    private int pageSize;

    private int offset;

    private String timestamp;

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public String getCookieUserToken() {
        return cookieUserToken;
    }

    public void setCookieUserToken(String cookieUserToken) {
        this.cookieUserToken = cookieUserToken;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
