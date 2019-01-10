package com.iot.tenant.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 用户-组织表
 *
 * 用户跟虚拟组织的关系  用户可以有多个虚拟组织  但都只能归于同一个租户下。
 * 目前一个用户创建完必须归属于自己的虚拟组织（默认创建）呈现默认自己的虚拟组织信息 可以切换不同的虚拟组织
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@TableName("user_to_virtual_org")
public class UserToVirtualOrg extends Model<UserToVirtualOrg> {

    public static final String TABLE_NAME = "user_to_virtual_org";

    private static final long serialVersionUID = 1L;

    public static final Integer DEFAULT_USED = 0;

    //受邀
    public static final Integer USER_BY = 1;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 租户组织id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 默认组织0,受邀请的1 默认为0
     */
    @TableField("default_used")
    private Integer defaultUsed;
    /**
     * 描述
     */
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getDefaultUsed() {
        return defaultUsed;
    }

    public void setDefaultUsed(Integer defaultUsed) {
        this.defaultUsed = defaultUsed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserToVirtualOrg{" +
                ", id=" + id +
                ", userId=" + userId +
                ", orgId=" + orgId +
                ", defaultUsed=" + defaultUsed +
                ", description=" + description +
                "}";
    }
}
