package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description 套包设备详情信息实体类
 *@author wucheng
 *@create 2018/12/12 10:27
 */
@ApiModel(value = "套包设备详情信息实体类")
@Data
public class PackageDeviceTypeInfoResp {

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "packageName", value = "套包名称")
    private String packageName;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private String packageType;

    @ApiModelProperty(name = "deviceTypeId", value ="设备类型id")
    private Long  deviceTypeId;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

}
