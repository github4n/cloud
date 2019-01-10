package com.iot.portal.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
  * @despriction：创建套包入参
  * @author  yeshiyuan
  * @created 2018/11/24 11:42
  */
@ApiModel(description = "创建套包入参")
@Data
public class CreatePackageReq {

    @ApiModelProperty(name = "packageId", value = "选择套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "productIds", value = "产品ids", dataType = "Long")
    private List<Long> productIds;

    @ApiModelProperty(name = "packageType", value = "套包类型（自定义套包需要传）", dataType = "Integer")
    private Integer packageType;

    @ApiModelProperty(name = "packageName", value = "套包名称", dataType = "String")
    private String packageName;

    @ApiModelProperty(name = "icon", value = "图标", dataType = "String")
    private String icon;
}
