package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel(description = "套包实体类")
public class PackageReq {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 产品主键
     */
    private Long productId;
    /**
     * 租户主键
     */
    private Long tenantId;
    /**
     * 创建者主键
     */
    private Long createBy;
    /**
     * 更新者主键
     */
    private Long updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 图标
     */
    private String icon;
    /**
     * 描述
     */
    private String description;
    /**
     * 套包类型
     */
    private int packageType;
    /**
     * 套包id
     */
    private Long packageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public PackageReq() {
    }

    public PackageReq(String name, Long tenantId, Long createBy, Date createTime, String icon, int packageType, Long packageId) {
        this.name = name;
        this.tenantId = tenantId;
        this.createBy = createBy;
        this.createTime = createTime;
        this.icon = icon;
        this.packageType = packageType;
        this.packageId = packageId;
    }
}
