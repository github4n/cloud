package com.iot.user.entity;

import java.util.Date;

public class UserLog {
    private String uuid;
    private String userName;
    private Long tenantId;
    private Integer accept;
    private Date acceptTime;
    private Integer cancel;
    private Date cancelTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getAccept() {
        return accept;
    }

    public void setAccept(Integer accept) {
        this.accept = accept;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getCancel() {
        return cancel;
    }

    public void setCancel(Integer cancel) {
        this.cancel = cancel;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                "uuid='" + uuid + '\'' +
                ", userName='" + userName + '\'' +
                ", tenantId=" + tenantId +
                ", accept=" + accept +
                ", acceptTime=" + acceptTime +
                ", cancel=" + cancel +
                ", cancelTime=" + cancelTime +
                '}';
    }
}
