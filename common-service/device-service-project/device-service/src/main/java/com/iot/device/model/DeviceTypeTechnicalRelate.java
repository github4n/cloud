package com.iot.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：设备类型技术方案关联表
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 19:26
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 19:26
 * 修改描述：
 */
@ApiModel(description = "设备类型技术方案关联表")
public class DeviceTypeTechnicalRelate {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;
    @ApiModelProperty(name = "technicalSchemeId", value = "技术方案id", dataType = "Long")
    private Long technicalSchemeId;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人", dataType = "Long")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getTechnicalSchemeId() {
        return technicalSchemeId;
    }

    public void setTechnicalSchemeId(Long technicalSchemeId) {
        this.technicalSchemeId = technicalSchemeId;
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

    public DeviceTypeTechnicalRelate() {
    }

    public DeviceTypeTechnicalRelate(Long deviceTypeId, Long technicalSchemeId, Long createBy, Date createTime) {
        this.deviceTypeId = deviceTypeId;
        this.technicalSchemeId = technicalSchemeId;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
