package com.iot.user.vo;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：查询请求类
 * 创建人： nongchongwei
 * 创建时间： 2018/7/13 14:23
 */
@ApiModel
public class UserSearchReq extends SearchParam {

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID", dataType = "Long", required = true)
    private Long tenantId;

    /**
     * 用户级别 2:企业级客户;3:终端用户
     */
    @ApiModelProperty(value = "用户级别2:企业级客户;3:终端用户", dataType = "Integer", required = true)

    private Integer userLevel;

    /**
     * 管理员标识(当user_level为1或2时，1：超级管理员;2:普通用户)
     */
    @ApiModelProperty(value = "管理员标识 1：超级管理员;2:普通用户", dataType = "Integer", required = false)

    private Integer adminStatus;

    @ApiModelProperty(value = "用户名称", dataType = "String", required = false)
    private String userName;

    @ApiModelProperty(value = "角色名称", dataType = "String", required = false)
    private String roleName;

    private Long locationId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    @Override
    public String toString() {
        return "UserSearchReq{" +
                "tenantId=" + tenantId +
                ", userLevel=" + userLevel +
                ", adminStatus=" + adminStatus +
                ", userName='" + userName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", locationId=" + locationId +
                '}';
    }
}
