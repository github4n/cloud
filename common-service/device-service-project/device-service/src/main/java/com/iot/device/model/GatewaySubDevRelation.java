package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：网关子设备关联表请求bean
 * 功能描述：
 * 创建人： wucheng
 * 创建时间：2018-11-08 15:11:12
 */
@TableName("gateway_subdev_relation")
public class GatewaySubDevRelation extends Model<GenerateModule> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("tenant_id")
    private Long tenantId;

    @TableField("pardev_id")
    private Long parDevId;

    @TableField("subdev_id")
    private Long subDevId;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    @TableField("is_deleted")
    private String isDeleted;

    @Override
    protected Serializable pkVal() {
        return id;
    }

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
}
