package com.iot.control.packagemanager.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
  * @despriction：套包支持設備類型(BOSS維護)
  * @author  yeshiyuan
  * @created 2018/11/20 20:44
  */
@TableName("package_device_type")
@ApiModel(description = "套包支持設備類型")
public class PackageDeviceType extends Model<PackageDeviceType> {

    @Override
    protected Serializable pkVal() {
        return id;
    }

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "deviceTypeId", value = "設備類型ID", dataType = "Long")
    private Long deviceTypeId;

    @ApiModelProperty(name = "createBy", value = "创建者主键", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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

    public PackageDeviceType() {
    }

    public PackageDeviceType(Long packageId, Long deviceTypeId, Long createBy, Date createTime) {
        this.packageId = packageId;
        this.deviceTypeId = deviceTypeId;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
