package com.iot.tenant.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板（BOSS管理）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 13:52
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 13:52
 * 修改描述：
 */
@ApiModel(description = "设备配网步骤模板（BOSS管理）")
public class DeviceNetworkStepBase {

    @ApiModelProperty(name = "id", value = "主键", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;

    @ApiModelProperty(name = "networkTypeId", value = "配网模式id", dataType = "Long")
    private Long networkTypeId;

    @ApiModelProperty(name = "isHelp", value = "是否是帮助文档(Y:是，N：否)", dataType = "String")
    private String isHelp;

    @ApiModelProperty(name = "step", value = "对应步骤", dataType = "Integer")
    private Integer step;

    @ApiModelProperty(name = "icon", value = "配网引导图", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "dataStatus", value = "数据有效性（invalid:失效；valid：有效）", dataType = "String")
    private String dataStatus;

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

    public Long getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(Long networkTypeId) {
        this.networkTypeId = networkTypeId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
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

    public String getIsHelp() {
        return isHelp;
    }

    public void setIsHelp(String isHelp) {
        this.isHelp = isHelp;
    }

    public DeviceNetworkStepBase() {
    }

    public DeviceNetworkStepBase(Long deviceTypeId, Long networkTypeId, String isHelp, Integer step, String icon, String dataStatus, Long createBy, Date createTime) {
        this.deviceTypeId = deviceTypeId;
        this.networkTypeId = networkTypeId;
        this.isHelp = isHelp;
        this.step = step;
        this.icon = icon;
        this.dataStatus = dataStatus;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
