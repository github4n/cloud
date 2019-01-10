package com.iot.portal.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
  * @despriction：套包设备类型信息出参
  * @author  yeshiyuan
  * @created 2018/11/21 10:46
  */
@Data
@ApiModel(description = "套包产品列表出餐出参")
@NoArgsConstructor
@AllArgsConstructor
public class PackageProductInfoResp {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "productName", value = "产品名称", dataType = "String")
    private String productName;



}
