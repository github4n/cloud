package com.iot.boss.vo.packagemanager.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description boss端修改套包信息入参
 *@author wucheng
 *@create 2018/12/11 16:26
 */
@ApiModel(value = "boss端修改套包信息入参")
@Data
public class BossUpdatePackageReq {
    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "name", value = "套包名称")
    private String name;

    @ApiModelProperty(name = "icon", value = "图片")
    private String icon;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private int packageType;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

}
