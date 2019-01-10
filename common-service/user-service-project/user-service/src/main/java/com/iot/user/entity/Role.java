package com.iot.user.entity;

import java.util.Date;
/**
 * 项目名称：立达信IOT云平台
 * 模块名称：权限模块
 * 功能描述：角色信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
public class Role {
    /**
     * 角色id，自动增长
     */
    private Long id;

    /**
     * 角色编码 role_type为1时才有值
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色类型 （1：默认角色；2：自定义角色）
     */
    private Integer roleType;

    /**
     * 用户级别
     */
    private Integer userLevel;

    /**
     * 租户id 当用户级别为1时，租户id可为空
     */
    private Long tenantId;

    /**
     * 角色描述
     */
    private String desc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 数据有效性（0：失效，1：有效）
     */
    private Integer dataStatus;

    /**
     * 角色id，自动增长
     *
     * @return id - 角色id，自动增长
     */
    public Long getId() {
        return id;
    }

    /**
     * 角色id，自动增长
     *
     * @param id 角色id，自动增长
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 角色编码 role_type为1时才有值
     *
     * @return role_code - 角色编码 role_type为1时才有值
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 角色编码 role_type为1时才有值
     *
     * @param roleCode 角色编码 role_type为1时才有值
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 角色类型 （1：默认角色；2：自定义角色）
     *
     * @return role_type - 角色类型 （1：默认角色；2：自定义角色）
     */
    public Integer getRoleType() {
        return roleType;
    }

    /**
     * 角色类型 （1：默认角色；2：自定义角色）
     *
     * @param roleType 角色类型 （1：默认角色；2：自定义角色）
     */
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    /**
     * 用户级别
     *
     * @return user_level - 用户级别
     */
    public Integer getUserLevel() {
        return userLevel;
    }

    /**
     * 用户级别
     *
     * @param userLevel 用户级别
     */
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * 租户id 当用户级别为1时，租户id可为空
     *
     * @return tenant_id - 租户id 当用户级别为1时，租户id可为空
     */
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * 租户id 当用户级别为1时，租户id可为空
     *
     * @param tenantId 租户id 当用户级别为1时，租户id可为空
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 角色描述
     *
     * @return desc - 角色描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 角色描述
     *
     * @param desc 角色描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 数据有效性（0：失效，1：有效）
     *
     * @return data_status - 数据有效性（0：失效，1：有效）
     */
    public Integer getDataStatus() {
        return dataStatus;
    }

    /**
     * 数据有效性（0：失效，1：有效）
     *
     * @param dataStatus 数据有效性（0：失效，1：有效）
     */
    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}