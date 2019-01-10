package com.iot.tenant.vo.req;

import java.io.Serializable;

/**
 * 描述：获取版本列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 11:08
 */
public class AppVersionListReq implements Serializable {
    /**
     * 版本号
     */
    private String version;
    /**
     * 应用主键
     */
    private Long appId;
    /**
     * 租户主键
     */
    private Long tenantId;

    private Integer pageNum;
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "AppVersionListReq{" +
                "version=" + version +
                ", appId=" + appId +
                ", tenantId=" + tenantId +
                '}';
    }
}
