package com.iot.design.project.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@ApiModel(description = "项目VO")
public class ProjectReq {
    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", value = "主键ID",example="1")
    private Long id;
    /**
     * 租户ID
     */
    @ApiModelProperty(name = "tenantId", value = "租户ID",example="1")
    private Long tenantId;
    /**
     * 项目名称
     */
    @ApiModelProperty(name = "name", value = "项目名称",example="厦门大学")
    private String name;
    /**
     * 项目封面
     */
    @ApiModelProperty(name = "pic", value = "项目封面",example="../aaa/bbb/xxx.png")
    private String pic;
    /**
     * 项目主要负责人ID
     */
    @ApiModelProperty(name = "leaderId", value = "项目主要负责人ID",example = "1")
    private Long leaderId;
    /**
     * 项目主要负责人名称
     */
    @ApiModelProperty(name = "leaderName", value = "项目主要负责人名称",example = "负责人")
    private String leaderName;
    /**
     * 项目类型
     */
    @ApiModelProperty(name = "type", value = "项目类型",example = "1")
    private Integer type;
    /**
     * 项目输出
     */
    @ApiModelProperty(name = "output", value = "项目输出",example = "2")
    private Integer output;
    /**
     * 数据状态 0-无效；1-有效
     */
    @ApiModelProperty(name = "dataStatus", value = "数据状态 0-无效；1-有效",example = "1")
    private Integer dataStatus = 1;
    /**
     * 创建人ID
     */
    @ApiModelProperty(name = "createBy", value = "创建人ID",example = "1")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间",example = "2018-08-06 18:18:12")
    private Date createTime;
    /**
     * 修改人ID
     */
    @ApiModelProperty(name = "updateBy", value = "修改人ID",example = "1")
    private Long updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "updateTime", value = "修改时间",example = "2018-08-06 18:28:12")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
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
