package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class UserMalfHistoryDto {

    private long malfId;

    private String userName;

    private int malfStatus;

    private int handleStatus;

    private int malfRank;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    private long tenantId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date renovateTime;

    private List<UserMalfProcessInfoDto> details;

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

    public int getMalfStatus() {
        return malfStatus;
    }

    public void setMalfStatus(int malfStatus) {
        this.malfStatus = malfStatus;
    }

    public int getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(int handleStatus) {
        this.handleStatus = handleStatus;
    }

    public int getMalfRank() {
        return malfRank;
    }

    public void setMalfRank(int malfRank) {
        this.malfRank = malfRank;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getRenovateTime() {
        return renovateTime;
    }

    public void setRenovateTime(Date renovateTime) {
        this.renovateTime = renovateTime;
    }

    public List<UserMalfProcessInfoDto> getDetails() {
        return details;
    }

    public void setDetails(List<UserMalfProcessInfoDto> details) {
        this.details = details;
    }
}
