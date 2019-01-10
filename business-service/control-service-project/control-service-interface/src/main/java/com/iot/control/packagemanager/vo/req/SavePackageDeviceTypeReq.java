package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
  * @despriction：保存套包支持設備類型入參
  * @author  yeshiyuan
  * @created 2018/11/20 20:56
  */
@Data
@ApiModel(description = "保存套包支持設備類型入參")
public class SavePackageDeviceTypeReq {

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "deviceTypeIds", value = "設備類型id", dataType = "List")
    private List<Long> deviceTypeIds;

    @ApiModelProperty(name = "createBy", value = "创建者主键", dataType = "Long")
    private Long createBy;



}
