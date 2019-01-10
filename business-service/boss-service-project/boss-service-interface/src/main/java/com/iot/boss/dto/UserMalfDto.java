package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserMalfDto {

    /* 报障单号 */
    private long malfId;

    /* 报障用户账号 */
    private String userName;

    /* 报障处理状态 */
    private int handleStatus;

    /* 报障处理进度 */
    private int malfStatus;

    /* 报障等级 */
    private int malfRank;

    /* 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    /* 租户id */
    private long tenantId;

    public long getMalfId() {
        return malfId;
    }

    public void setMalfId(long malfId) {
        this.malfId = malfId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(int handleStatus) {
        this.handleStatus = handleStatus;
    }

    public int getMalfStatus() {
        return malfStatus;
    }

    public void setMalfStatus(int malfStatus) {
        this.malfStatus = malfStatus;
    }

    public int getMalfRank() {
        return malfRank;
    }

    public void setMalfRank(int malfRank) {
        this.malfRank = malfRank;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date handleDate) {
        this.dateTime = handleDate;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }
}