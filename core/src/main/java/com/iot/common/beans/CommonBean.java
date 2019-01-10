package com.iot.common.beans;

import java.io.Serializable;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：公共类,包含创建者，创建时间，更新者，更新时间
 * 创建人： wujianlong
 * 创建时间：2017年11月10日 下午2:02:55
 * 修改人： wujianlong
 * 修改时间：2017年11月10日 下午2:02:55
 */
public class CommonBean implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private String id;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新着
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long lastUpdateDate;

    /**
     * 租户ID
     */
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
