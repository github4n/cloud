package com.iot.portal.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * @despriction：套包类型响应
  * @author  yeshiyuan
  * @created 2018/12/7 10:48
  */
@ApiModel("套包类型响应")
@Data
public class PackageTypeResp {

    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "packageType", value = "套包类型", dataType = "String")
    private String packageType;

    public PackageTypeResp(Integer id, String packageType) {
        this.id = id;
        this.packageType = packageType;
    }
}
