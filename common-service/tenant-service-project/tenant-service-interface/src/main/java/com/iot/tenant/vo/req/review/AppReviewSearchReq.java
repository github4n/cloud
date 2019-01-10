package com.iot.tenant.vo.req.review;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：APP审核列表查询参数
 * 创建人： maochengyuan
 * 创建时间：2018/10/25 14:37
 * 修改人： maochengyuan
 * 修改时间：2018/10/25 14:37
 * 修改描述：
 */
public class AppReviewSearchReq {

    private String tenantName;

    private String appName;

    private int pageNum;

    private int pageSize;

    private Byte auditStatus;

    private Long createBy;

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Integer getPageNum() {
        if (pageNum==0) {
            pageNum = 1;
        }
        return pageNum;
    }


    public Integer getPageSize() {
        if (pageSize==0) {
            pageSize = 20;
        }
        return pageSize;
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

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "AppReviewSearchReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
