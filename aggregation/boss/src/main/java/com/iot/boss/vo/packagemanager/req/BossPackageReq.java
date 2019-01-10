package com.iot.boss.vo.packagemanager.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 *@description boss保存套包入参
 *@author wucheng
 *@create 2018/12/11 16:25
 */
@ApiModel(value = "boss保存套包入参")
@Data
public class BossPackageReq {

    @ApiModelProperty(name = "fileId", value = "文件id")
    private String fileId;

    @ApiModelProperty(name = "name", value = "套包名称")
    private String name;

    @ApiModelProperty(name = "description", value = "描述")
    private  String description;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;

}
