package com.iot.boss.vo.packagemanager.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description
 *@author wucheng
 *@create 2018/12/17 11:49
 */
@ApiModel(value = "boss搜索入参")
@Data
public class BossSearchPackageReq {
    @ApiModelProperty(name = "pageNum", value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "当前页显示多少条数据")
    private Integer pageSize;

    @ApiModelProperty(name = "name", value = "套包名称")
    private String name;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;
}
