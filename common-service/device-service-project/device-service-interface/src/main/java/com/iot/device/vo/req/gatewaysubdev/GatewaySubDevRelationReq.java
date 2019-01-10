package com.iot.device.vo.req.gatewaysubdev;

import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：网关子设备关联表请求bean
 * 功能描述：
 * 创建人： wucheng
 * 创建时间：2018-11-08 15:11:12
 */
public class GatewaySubDevRelationReq {

    private Long tenantId;

    private Long parDevId;

    private Long subDevId;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String isDeleted;

    public GatewaySubDevRelationReq() {}

    public GatewaySubDevRelationReq(Long tenantId, Long parDevId, Long subDevId, Long createBy, Date createTime, Long updateBy, Date updateTime, String isDeleted) {
        this.tenantId = tenantId;
        this.parDevId = parDevId;
        this.subDevId = subDevId;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
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
}
