package com.iot.device.vo.rsp.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@description 套包产品名称：用于json体内的产品列表展示
 *@author wucheng
 *@create 2018/12/12 16:46
 */
@ApiModel(value = "套包产品名称")
@Data
public class PackageProductNameResp implements Serializable{

    @ApiModelProperty(name = "id", value = "产品id")
    private Long id;

    @ApiModelProperty(name = "productName", value ="产品名称")
    private String productName;

    @ApiModelProperty(name = "deviceTypeId", value ="设备类型id")
    private Long deviceTypeId;
}
