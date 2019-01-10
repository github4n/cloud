package com.iot.user.entity;

import java.util.Date;
/**
 * 项目名称：立达信IOT云平台
 * 模块名称：权限模块
 * 功能描述：角色资源关系
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
public class RoleAuth {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源id
     */
    private Long authId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色id
     *
     * @return role_id - 角色id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 资源id
     *
     * @return auth_id - 资源id
     */
    public Long getAuthId() {
        return authId;
    }

    /**
     * 资源id
     *
     * @param authId 资源id
     */
    public void setAuthId(Long authId) {
        this.authId = authId;
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
}