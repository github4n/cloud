package com.iot.portal.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.resp.DeviceTypeIdAndName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@description 套包绑定设备详情bean
 *@author wucheng
 *@create 2018/12/13 14:24
 */
@ApiModel(value = "套包绑定设备详情bean")
@Data
public class PackageDeviceTypeDetailResp {
    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "packageName", value = "套包名称")
    private String packageName;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;

    @ApiModelProperty(name = "deviceTypeIdAndNames", value = "套包绑定的设备详细")
    private List<DeviceTypeIdAndName> deviceTypeIdAndNames;
}
