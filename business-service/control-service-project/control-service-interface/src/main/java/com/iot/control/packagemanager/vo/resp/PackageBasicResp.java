package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@description
 *@create 2018/12/13 15:43
 */
@ApiModel(value = "套包基本信息实体类")
@Data
public class PackageBasicResp implements Serializable{
    @ApiModelProperty(name = "packageId", value = "packageId")
    private Long packageId;

    @ApiModelProperty(name = "packageName", value = "packageName")
    private String packageName;

    @ApiModelProperty(name = "packageType", value = "packageType")
    private Integer packageType;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "icon", value = "图片")
    private String icon;

    @ApiModelProperty(name = "iconUrl", value = "图片url")
    private String iconUrl;
}
