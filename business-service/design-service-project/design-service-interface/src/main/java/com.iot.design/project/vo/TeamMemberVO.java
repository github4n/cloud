package com.iot.design.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/13
 */
@ApiModel(description = "团队成员VO")
public class TeamMemberVO {
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键ID", example = "1")
    private Long id;
    /**
     * 租户ID
     */
    @ApiModelProperty(name = "tenantId", value = "租户ID", example = "1")
    private Long tenantId;
    /**
     * 团队ID  team:id
     */
    @ApiModelProperty(name = "teamId", value = "团队ID", example = "1")
    private Long teamId;
    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId", value = "用户ID", example = "1")
    private Long userId;
    /**
     * 用户名称
     */
    @ApiModelProperty(name = "userName", value = "用户名称", example = "张三")
    private String userName;
    /**
     * 用户在团队中的角色  1 主要负责人 2 其他负责人 3 审核人 4校对人 5 设计 6制图
     */
    @ApiModelProperty(name = "userRole", value = "用户在团队中的角色1 主要负责人 2 其他负责人 3 审核人 4校对人 5 设计 6制图", example = "1")
    private Integer userRole;
    /**
     * 数据状态 0-无效；1-有效
     */
    @ApiModelProperty(name = "dataStatus", value = "数据状态 0-无效；1-有效", example = "1")
    private Integer dataStatus = 1;
    /**
     * 创建人ID
     */
    @ApiModelProperty(name = "createBy", value = "创建人ID", example = "1")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间", example = "2018-08-06 18:18:12")
    private Date createTime;
    /**
     * 修改人ID
     */
    @ApiModelProperty(name = "updateBy", value = "修改人ID", example = "1")
    private Long updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "updateTime", value = "修改时间", example = "2018-08-06 18:28:12")
    private Date updateTime;

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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
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
}
