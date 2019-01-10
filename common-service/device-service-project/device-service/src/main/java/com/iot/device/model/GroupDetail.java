package com.iot.device.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 组控制详情表
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
@Data
@ToString
@TableName("group_detail")
public class GroupDetail extends Model<GroupDetail> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * group id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;
    @TableField("group_id")
    private Long groupId;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 创建者id
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 更新者id
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "GroupDetail{" +
        ", id=" + id +
        ", deviceId=" + deviceId +
        ", groupId=" + groupId +
        ", tenantId=" + tenantId +
        ", createBy=" + createBy +
        ", updateBy=" + updateBy +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
