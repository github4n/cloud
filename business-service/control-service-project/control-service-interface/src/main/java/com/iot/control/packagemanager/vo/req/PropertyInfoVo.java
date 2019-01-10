package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
  * @despriction：属性信息详情
  * @author  yeshiyuan
  * @created 2018/11/23 10:47
  */
@ApiModel(description = "属性信息详情")
@Data
public class PropertyInfoVo {
    @ApiModelProperty(name = "propertyId", value = "属性id", dataType = "Long")
    private Long propertyId;

    @ApiModelProperty(name = "value", value = "属性值", dataType = "String")
    private String value;

    @ApiModelProperty(name = "compOp", value = "条件判断符", dataType = "String")
    private String compOp;

    public PropertyInfoVo() {
    }

    public PropertyInfoVo(Long propertyId, String value, String compOp) {
        this.propertyId = propertyId;
        this.value = value;
        this.compOp = compOp;
    }
}
