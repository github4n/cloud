package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description
 *@author wucheng
 *@create 2018/12/13 14:15
 */
@ApiModel(value = "设备类型id和名称实体类")
@Data
public class DeviceTypeIdAndName {

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id")
    private Long deviceTypeId;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    public DeviceTypeIdAndName() {
    }

    public DeviceTypeIdAndName(Long deviceTypeId, String deviceTypeName) {
        this.deviceTypeId = deviceTypeId;
        this.deviceTypeName = deviceTypeName;
    }
}
