package com.iot.domain;

import java.util.Date;

public class UserSpaceUsed {
    private String userUsedId;

    private String userId;

    private String spaceDateId;

    private String tenantId;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private Byte isDeleted;

    public String getUserUsedId() {
        return userUsedId;
    }

    public void setUserUsedId(String userUsedId) {
        this.userUsedId = userUsedId == null ? null : userUsedId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSpaceDateId() {
        return spaceDateId;
    }

    public void setSpaceDateId(String spaceDateId) {
        this.spaceDateId = spaceDateId == null ? null : spaceDateId.trim();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }
}