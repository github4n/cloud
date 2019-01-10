package com.iot.tenant.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤（portal租户管理）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 13:52
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 13:52
 * 修改描述：
 */
@ApiModel(description = "设备配网步骤（portal租户管理）")
public class DeviceNetworkStepTenant {

    @ApiModelProperty(name = "id", value = "主键", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "appId", value = "App应用", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "networkTypeId", value = "配网模式id", dataType = "Long")
    private Long networkTypeId;

    @ApiModelProperty(name = "isHelp", value = "是否是帮助文档(Y:是，N：否)", dataType = "String")
    private String isHelp;

    @ApiModelProperty(name = "step", value = "对应步骤", dataType = "Integer")
    private Integer step;

    @ApiModelProperty(name = "icon", value = "配网引导图", dataType = "String")
    private String icon;


    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人", dataType = "Long")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(Long networkTypeId) {
        this.networkTypeId = networkTypeId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getIsHelp() {
        return isHelp;
    }

    public void setIsHelp(String isHelp) {
        this.isHelp = isHelp;
    }

    public DeviceNetworkStepTenant() {
    }

    public DeviceNetworkStepTenant(Long tenantId, Long appId, Long productId, Long networkTypeId, String isHelp, Integer step, String icon, Long createBy, Date createTime) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.productId = productId;
        this.networkTypeId = networkTypeId;
        this.isHelp = isHelp;
        this.step = step;
        this.icon = icon;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
