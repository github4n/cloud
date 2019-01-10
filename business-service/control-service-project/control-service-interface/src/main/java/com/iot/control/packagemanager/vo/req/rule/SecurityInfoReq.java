package com.iot.control.packagemanager.vo.req.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
  * @despriction：安防策略信息
  * @author  yeshiyuan
  * @created 2018/11/23 15:32
  */
@Data
@ApiModel(description = "安防策略信息")
public class SecurityInfoReq {

    @ApiModelProperty(name = "type", value = "安防策略类型（stay、away、sos)", dataType = "String")
    private String type;

    @ApiModelProperty(name = "alram", value = "安防警告配置", dataType = "Map")
    private Map<String, Object> alram;
}
