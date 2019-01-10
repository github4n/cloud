package com.iot.boss.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：Boss
 * 功能描述：报障记录
 * 创建人： 490485964@qq.com
 * 创建时间：2018年05月15日 14:42
 * 修改人： 490485964@qq.com
 * 修改时间：2018年05月15日 14:42
 */
public class MalfRecordDto implements Serializable {

    private static final long serialVersionUID = -6605703057339673955L;

    /** 主键id 报障单号 */
    private Long id;

    /** 报障单关联的用户ID */
    private String userId;

    /** 报障单关联的用户所属租户id */
    private Long tenantId;

    /** 报障描述 */
    private String malfDesc;

    /** 报障处理进度 0：及时 1：延迟  2：严重延迟 */
    private Integer malfStatus;

    /** 报障等级 0：非问题 1：轻度 2：普通 3：严重 */
    private Integer malfRank;

    /** 创建时间 */
    private Date createTime;

    /** 管理员确认时间 */
    private Date confirmTime;

    /** 是否已经被分配给普通管理员  0：否 1：是 */
    private Integer isAllocated;

    /** 处理状态 0：创建 1：处理中 2：处理完毕 3：修复完毕 */
    private Integer handleStatus;

    /** 上一个处理人ID */
    private Long preHandleAdminId;

    /** 当前处理人ID */
    private Long curHandleAdminId;

    /** 运维处理开始时间*/
    private Date handleStartTime;

    /** 运维处理结束时间 */
    private Date handleEndTime;

    /** 修复时间 */
    private Date renovateTime;

    /** 是否超级管理员退回  0：否 1：是 */
    private Integer isRollback;

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
