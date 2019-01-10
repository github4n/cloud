package com.iot.portal.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloud
 * @description: 保存套包基本信息入参
 * @author: yeshiyuan
 * @create: 2018-12-17 09:50
 **/
@ApiModel(description = "保存套包基本信息入参")
@Data
public class SavePackageBaseInfoReq {

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;
    @ApiModelProperty(name = "icon", value = "图标", dataType = "String")
    private String icon;
    @ApiModelProperty(name = "packageName", value = "套包名称", dataType = "String")
    private String packageName;

}
