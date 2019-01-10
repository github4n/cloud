package com.iot.boss.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：报障记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 16:31
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 16:31
 * 修改描述：
 */
public class MalfRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户uuid
     */
    private String userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 报障描述
     */
    private String malfDesc;

    /**
     * 报障处理进度(0：及时 1：延迟  2：严重延迟)
     */
    private Integer malfStatus;

    /**
     * 报障等级 0：非问题 1：轻度 2：普通 3：严重
     */
    private Integer malfRank;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 管理员确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date confirmTime;

    /**
     * 是否已经被分配给普通管理员
     */
    private Integer isAllocated;

    /**
     * 处理状态 0：创建 1：处理中 2：修复完毕
     */
    private Integer handleStatus;

    /**
     * 上一个处理人ID
     */
    private Long preHandleAdminId;

    /**
     * 当前处理人ID
     */
    private Long curHandleAdminId;

    /**
     * 运维处理开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date handleStartTime;

    /**
     * 运维处理结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date handleEndTime;

    /**
     * 修复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date renovateTime;

    /**
     * 是否超级管理员退回  0：否 1：是
     */
    private Integer isRollback;

    public MalfRecord() {

    }

    public MalfRecord(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getMalfDesc() {
        return malfDesc;
    }

    public void setMalfDesc(String malfDesc) {
        this.malfDesc = malfDesc;
    }

    public Integer getMalfStatus() {
        return malfStatus;
    }

    public void setMalfStatus(Integer malfStatus) {
        this.malfStatus = malfStatus;
    }

    public Integer getMalfRank() {
        return malfRank;
    }

    public void setMalfRank(Integer malfRank) {
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

    public Integer getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(Integer isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Long getPreHandleAdminId() {
        return preHandleAdminId;
    }

    public void setPreHandleAdminId(Long preHandleAdminId) {
        this.preHandleAdminId = preHandleAdminId;
    }

    public Long getCurHandleAdminId() {
        return curHandleAdminId;
    }

    public void setCurHandleAdminId(Long curHandleAdminId) {
        this.curHandleAdminId = curHandleAdminId;
    }

    public Date getHandleStartTime() {
        return handleStartTime;
    }

    public void setHandleStartTime(Date handleStartTime) {
        this.handleStartTime = handleStartTime;
    }

    public Date getHandleEndTime() {
        return handleEndTime;
    }

    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }

    public Date getRenovateTime() {
        return renovateTime;
    }

    public void setRenovateTime(Date renovateTime) {
        this.renovateTime = renovateTime;
    }

    public Integer getIsRollback() {
        return isRollback;
    }

    public void setIsRollback(Integer isRollback) {
        this.isRollback = isRollback;
    }
}
