package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * @despriction：if中设备信息
  * @author  yeshiyuan
  * @created 2018/11/23 10:39
  */
@ApiModel(description = "if中设备类型/产品信息")
@Data
public class IfDevInfoReq {

    @ApiModelProperty(name = "id", value = "设备类型id或产品id", dataType = "Long" )
    private Long id;
    @ApiModelProperty(name = "idx", value = "执行顺序", dataType = "Long" )
    private Long idx;
    @ApiModelProperty(name = "attrLogic", value = "属性触发规则（and/or）", dataType = "String" )
    private String attrLogic;
    @ApiModelProperty(name = "attrInfo", value = "if参数信息", dataType = "List" )
    private List<IfAttrInfoReq> attrInfo;

}
