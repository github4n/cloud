package com.iot.control.packagemanager.vo.req.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
  * @despriction：策略if信息
  * @author  yeshiyuan
  * @created 2018/11/23 14:14
  */
@Data
@ApiModel(description = "策略if信息")
public class RuleIfInfoReq {

    @ApiModelProperty(name = "logic", value = "触发逻辑规则", dataType = "String")
    private String logic;

    @ApiModelProperty(name = "trigger", value = "触发具体值", dataType = "TriggerInfoReq")
    private TriggerInfoReq trigger;
}
