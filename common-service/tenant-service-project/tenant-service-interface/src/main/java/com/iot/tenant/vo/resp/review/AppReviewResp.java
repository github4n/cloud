package com.iot.tenant.vo.resp.review;

import java.util.Date;
/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：APP审核列表对象
 * 创建人： maochengyuan 
 * 创建时间：2018/10/25 11:58
 * 修改人： maochengyuan
 * 修改时间：2018/10/25 11:58
 * 修改描述：
 */
public class AppReviewResp {

    /**appId*/
    private Long id;

    /**租户id*/
    private Long tenantId;

    /**app名称*/
    private String appName;

    /**审核状态*/
    private Byte auditStatus;

    /**申请时间*/
    private Date applyTime;

    /**申请操作员ID*/
    private Long applyOperId;

    /**操作员名称*/
    private String applyOperName;

    /**审核时间*/
    private Date auditTime;

    /**审核操作员ID*/
    private Long auditOperId;

    /**审核操作员名称*/
    private String auditOperName;

    /**企业名称*/
    private String entName;

    /**联系电话*/
    private String entCellphone;

    /**联系人*/
    private String entContacts;

    /**主账号邮箱*/
    private String mainAccount;

    public AppReviewResp() {

    }

    public AppReviewResp(Long id, String appName, Byte auditStatus, Long tenantId, Date applyTime, Long applyOperId) {
        this.id = id;
        this.appName = appName;
        this.auditStatus = auditStatus;
        this.tenantId = tenantId;
        this.applyTime = applyTime;
        this.applyOperId = applyOperId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Long getApplyOperId() {
        return applyOperId;
    }

    public void setApplyOperId(Long applyOperId) {
        this.applyOperId = applyOperId;
    }

    public String getApplyOperName() {
        return applyOperName;
    }

    public void setApplyOperName(String applyOperName) {
        this.applyOperName = applyOperName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Long getAuditOperId() {
        return auditOperId;
    }

    public void setAuditOperId(Long auditOperId) {
        this.auditOperId = auditOperId;
    }

    public String getAuditOperName() {
        return auditOperName;
    }

    public void setAuditOperName(String auditOperName) {
        this.auditOperName = auditOperName;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntCellphone() {
        return entCellphone;
    }

    public void setEntCellphone(String entCellphone) {
        this.entCellphone = entCellphone;
    }

    public String getEntContacts() {
        return entContacts;
    }

    public void setEntContacts(String entContacts) {
        this.entContacts = entContacts;
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount;
    }

}
