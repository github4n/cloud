package com.iot.boss.vo.packagemanager.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
  * @despriction：套包设备类型信息出参
  * @author  yeshiyuan
  * @created 2018/11/21 10:46
  */
@Data
@ApiModel(description = "套包设备类型信息出参")
public class PackageDeviceTypeInfoResp {

    private Long id;

    private String name;

    private String deviceCatalogName;

    private String description;

    //private Long deviceCatalogId;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    public PackageDeviceTypeInfoResp() {
    }

    public PackageDeviceTypeInfoResp(Long id, String name, String deviceCatalogName, String description, Integer iftttType) {
        this.id = id;
        this.name = name;
        this.deviceCatalogName = deviceCatalogName;
        this.description = description;
        this.iftttType = iftttType;
    }

    public PackageDeviceTypeInfoResp(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
