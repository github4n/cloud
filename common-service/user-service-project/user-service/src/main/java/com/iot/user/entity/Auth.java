package com.iot.user.entity;

import java.util.Date;
/**
 * 项目名称：立达信IOT云平台
 * 模块名称：权限模块
 * 功能描述：资源信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
public class Auth {
    /**
     * 资源id
     */
    private Long id;

    /**
     * 资源对应url
     */
    private String authUrl;

    /**
     * 资源名称
     */
    private String authName;

    /**
     * 资源类型（1:菜单；2:按钮；3：资源（js这些））
     */
    private Integer authType;

    /**
     * 资源排序  低的排前面
     */
    private Integer sort;

    /**
     * 资源图标  权限类型为菜单或按钮时才有值
     */
    private String icon;

    /**
     * 父资源id
     */
    private Long pAuthId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 数据有效性（0：失效；1：有效）
     */
    private Integer dataStatus;

    /**
     * 资源id
     *
     * @return id - 资源id
     */
    public Long getId() {
        return id;
    }

    /**
     * 资源id
     *
     * @param id 资源id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 资源对应url
     *
     * @return auth_url - 资源对应url
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * 资源对应url
     *
     * @param authUrl 资源对应url
     */
    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    /**
     * 资源名称
     *
     * @return auth_name - 资源名称
     */
    public String getAuthName() {
        return authName;
    }

    /**
     * 资源名称
     *
     * @param authName 资源名称
     */
    public void setAuthName(String authName) {
        this.authName = authName;
    }

    /**
     * 资源类型（1:菜单；2:按钮；3：资源（js这些））
     *
     * @return auth_type - 资源类型（1:菜单；2:按钮；3：资源（js这些））
     */
    public Integer getAuthType() {
        return authType;
    }

    /**
     * 资源类型（1:菜单；2:按钮；3：资源（js这些））
     *
     * @param authType 资源类型（1:菜单；2:按钮；3：资源（js这些））
     */
    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    /**
     * 资源排序  低的排前面
     *
     * @return sort - 资源排序  低的排前面
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 资源排序  低的排前面
     *
     * @param sort 资源排序  低的排前面
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 资源图标  权限类型为菜单或按钮时才有值
     *
     * @return icon - 资源图标  权限类型为菜单或按钮时才有值
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 资源图标  权限类型为菜单或按钮时才有值
     *
     * @param icon 资源图标  权限类型为菜单或按钮时才有值
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 父资源id
     *
     * @return p_auth_id - 父资源id
     */
    public Long getpAuthId() {
        return pAuthId;
    }

    /**
     * 父资源id
     *
     * @param pAuthId 父资源id
     */
    public void setpAuthId(Long pAuthId) {
        this.pAuthId = pAuthId;
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
     * 数据有效性（0：失效；1：有效）
     *
     * @return data_status - 数据有效性（0：失效；1：有效）
     */
    public Integer getDataStatus() {
        return dataStatus;
    }

    /**
     * 数据有效性（0：失效；1：有效）
     *
     * @param dataStatus 数据有效性（0：失效；1：有效）
     */
    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}