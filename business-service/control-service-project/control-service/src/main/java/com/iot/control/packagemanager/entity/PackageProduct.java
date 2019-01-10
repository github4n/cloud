package com.iot.control.packagemanager.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
  * @despriction：套包产品(portal維護)
  * @author  yeshiyuan
  * @created 2018/11/20 20:44
  */
@TableName("package_product")
@ApiModel(description = "套包产品")
public class PackageProduct extends Model<PackageProduct> {

    @Override
    protected Serializable pkVal() {
        return id;
    }

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "createBy", value = "创建者主键", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }



    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public PackageProduct() {
    }

    public PackageProduct(Long packageId, Long productId, Long tenantId, Long createBy, Date createTime) {
        this.packageId = packageId;
        this.productId = productId;
        this.tenantId = tenantId;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
