package com.iot.boss.vo.packagemanager.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description 返回套包信息实体类
 *@author wucheng
 *@create 2018/12/17 11:32
 */
@ApiModel(value = "返回套包信息实体类")
@Data
public class PackageListResp {

    @ApiModelProperty(name = "id", value = "套包id")
    private Long id;

    @ApiModelProperty(name = "name", value = "套包名称")
    private String name;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;
}
