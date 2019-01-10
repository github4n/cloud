package com.iot.control.packagemanager.vo.resp.rule;

import com.iot.control.packagemanager.vo.req.rule.TriggerInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "策略if信息")
public class RuleIfInfoResp {
    @ApiModelProperty(name = "logic", value = "触发逻辑规则", dataType = "String")
    private String logic;

    @ApiModelProperty(name = "trigger", value = "触发具体值", dataType = "TriggerInfoReq")
    private TriggerInfoResp trigger;
}
