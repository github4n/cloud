package com.iot.device.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 组控制表
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
@Data
@ToString
@TableName("`group`")
public class Group extends Model<Group>  implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * group id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组名称
     */
    private String name;
    /**
     * 空间id
     */
    @TableField("home_id")
    private Long homeId;
    /**
     * 设备控制组Id
     */
    @TableField("dev_group_id")
    private Integer devGroupId;
    /**
     * 图标
     */
    private String icon;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Integer getDevGroupId() {
        return devGroupId;
    }

    public void setDevGroupId(Integer devGroupId) {
        this.devGroupId = devGroupId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        return "Group{" +
        ", id=" + id +
        ", name=" + name +
        ", homeId=" + homeId +
        ", devGroupId=" + devGroupId +
        ", icon=" + icon +
        ", tenantId=" + tenantId +
        ", createBy=" + createBy +
        ", updateBy=" + updateBy +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
