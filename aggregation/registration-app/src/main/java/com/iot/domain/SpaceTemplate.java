package com.iot.domain;

import java.util.Date;

public class SpaceTemplate {
    private String spaceTempleId;

    private String spaceId;

    private String tenantId;

    private Date beginTime;

    private Date endTime;

    private Integer galleryful;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private Byte isDeleted;

    public String getSpaceTempleId() {
        return spaceTempleId;
    }

    public void setSpaceTempleId(String spaceTempleId) {
        this.spaceTempleId = spaceTempleId == null ? null : spaceTempleId.trim();
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId == null ? null : spaceId.trim();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getGalleryful() {
        return galleryful;
    }

    public void setGalleryful(Integer galleryful) {
        this.galleryful = galleryful;
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