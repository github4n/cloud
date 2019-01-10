package com.iot.boss.vo.review.req;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：Boss聚合
 * 功能描述：APP审核列表查询参数
 * 创建人： maochengyuan
 * 创建时间：2018/10/25 14:37
 * 修改人： maochengyuan
 * 修改时间：2018/10/25 14:37
 * 修改描述：
 */
public class AppReviewListSearchReq {

    private String accountName;

    private String tenantName;

    private String appName;

    private int pageNum;

    private int pageSize;

    private Byte auditStatus;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "AppReviewListSearchReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ",appName =" + appName +
                ",tenantName = " + tenantName +
                "}";
    }
}
