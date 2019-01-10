package com.iot.device.vo.rsp.gatewaysubdev;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.*;

/**
 * 项目名称：IOT云平台
 * 模块名称：网关子设备关联表返回前端bean
 * 功能描述：
 * 创建人： wucheng
 * 创建时间：2018-11-08 15:11:12
 */
public class GatewaySubDevRelationResp implements Serializable{

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "租户id", value = "tenantId", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "租户id", value = "icon", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "网关产品id", value = "parDevId", dataType = "Long")
    private Long parDevId;

    @ApiModelProperty(name = "子设备产品id", value = "subdevId", dataType = "Long")
    private Long subDevId;

    @ApiModelProperty(name = "创建者id", value = "createBy", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "创建时间", value = "createTime", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "更新者id", value = "updateBy", dataType = "Long")
    private Long updateBy;

    @ApiModelProperty(name = "更新时间", value = "updateTime", dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(name = "是否删除", value = "isDeleted", dataType = "String")
    private String isDeleted;

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

    public Long getParDevId() {
        return parDevId;
    }

    public void setParDevId(Long parDevId) {
        this.parDevId = parDevId;
    }

    public Long getSubDevId() {
        return subDevId;
    }

    public void setSubDevId(Long subDevId) {
        this.subDevId = subDevId;
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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
