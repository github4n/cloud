package com.iot.design.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/13
 */
@ApiModel(description = "项目团队VO")
public class ProjectTeamVO {
    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", value = "主键ID", example = "1")
    private Long id;
    /**
     * 租户ID
     */
    @ApiModelProperty(name = "tenantId", value = "租户ID", example = "1")
    private Long tenantId;
    /**
     * 项目ID project:id
     */
    @ApiModelProperty(name = "projectId", value = "项目ID", example = "1")
    private Long projectId;
    /**
     * 团队ID  team:id
     */
    @ApiModelProperty(name = "teamId", value = "团队ID", example = "1")
    private Long teamId;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }


}
