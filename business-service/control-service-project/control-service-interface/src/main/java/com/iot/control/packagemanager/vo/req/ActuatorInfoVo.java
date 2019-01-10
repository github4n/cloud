package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
  * @despriction：执行者信息
  * @author  yeshiyuan
  * @created 2018/11/23 11:26
  */
@Data
@ApiModel(description = "执行者信息")
public class ActuatorInfoVo {

    @ApiModelProperty(name = "type", value = "执行者类型（比如定时、推送等等）", dataType = "String")
    private String type;
    @ApiModelProperty(name = "config", value = "配置详情", dataType = "Map")
    private Map<String,Object> config;

}
