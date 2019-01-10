package com.iot.boss.vo.product.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：产品审核列表出参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 14:57
 * 修改描述：
 */
@ApiModel(description = "产品审核列表出参")
public class BossProductAuditListResp {
    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;
    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    @ApiModelProperty(name = "tenantName", value = "租户名称",dataType = "String")
    private String tenantName;
    @ApiModelProperty(name = "productName", value = "产品名称", dataType = "String")
    private String productName;
    @ApiModelProperty(name = "model", value = "型号", dataType = "String")
    private String model;
    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称", dataType = "String")
    private String deviceTypeName;
    @ApiModelProperty(name="auditStatus", value = "审核状态", dataType = "int")
    private Integer auditStatus;
    @ApiModelProperty(name = "auditStatusStr", value = "审核状态描述", dataType = "String")
    private String auditStatusStr;
    @ApiModelProperty(name="operateUserName", value = "操作人用户名称", dataType = "String")
    private String operateUserName;
    @ApiModelProperty(name="email", value = "邮箱", dataType = "String")
    private String email;
    @ApiModelProperty(name = "applyTime", value = "操作时间", dataType = "Date")
    private Date applyTime;
    @ApiModelProperty(value = "操作人用户id")
    private Long operateUserId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusStr() {
        return auditStatusStr;
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }
}
