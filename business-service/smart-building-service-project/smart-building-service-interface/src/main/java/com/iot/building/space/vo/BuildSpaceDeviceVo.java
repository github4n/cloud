/*
 * 项目名称 : common-web
 * 创建日期 : 2018年1月29日
 * 修改历史 :
 *     1. [2018年1月29日]创建文件 by linjihuang
 */
package com.iot.building.space.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：空间设备表VO
 *
 * @author linjihuang
 */
@ApiModel(value = "设备空间关联关系", description = "设备空间关联关系")
public class BuildSpaceDeviceVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -686126811451821584L;
    @ApiModelProperty("主键id，保存时不用传")
    private Long id;
    @ApiModelProperty("设备ID")
    private String deviceId;
    @ApiModelProperty("空间ID")
    private Long spaceId;
    @ApiModelProperty("待补充")
    private Long parentId;
    @ApiModelProperty("待补充")
    private String name;
    @ApiModelProperty("待补充")
    private String type;
    @ApiModelProperty("房间挂载状态")
    private String status;
    @ApiModelProperty("设备类型ID")
    private Long deviceTypeId;
    @ApiModelProperty("设备坐标位置")
    private String position;
    @ApiModelProperty("创建时间")
    private Long createTime;
    @ApiModelProperty("上次修改时间")
    private Long lastUpdateDate;
    @ApiModelProperty("顶级区域ID")
    private String locationId;
    @ApiModelProperty("租户ID")
    private String tenantId;

    private String deviceType;

    private Integer order;

    private Date newCreateTime;

    private String defaultSpace;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getNewCreateTime() {
        return newCreateTime;
    }

    public void setNewCreateTime(Date newCreateTime) {
        this.newCreateTime = newCreateTime;
    }

    public String getDefaultSpace() {
        return defaultSpace;
    }

    public void setDefaultSpace(String defaultSpace) {
        this.defaultSpace = defaultSpace;
    }
}
