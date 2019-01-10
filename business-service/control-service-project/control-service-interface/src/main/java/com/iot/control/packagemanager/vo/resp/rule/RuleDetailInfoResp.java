package com.iot.control.packagemanager.vo.resp.rule;

import com.iot.control.packagemanager.vo.req.rule.RuleIfInfoReq;
import com.iot.control.packagemanager.vo.req.rule.SecurityInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "返回前端策略详情bean")
@Data
public class RuleDetailInfoResp implements Serializable{

    @ApiModelProperty(name = "security", value = "安防信息", dataType = "SecurityInfoReq")
    private SecurityInfoResp security;

    @ApiModelProperty(name = "ruleIf", value = "if详情", dataType = "RuleIfInfoReq")
    private RuleIfInfoResp ruleIf;

    @ApiModelProperty(name = "then", value = "then详情", dataType = "RuleThenInfoReq")
    private RuleThenInfoResp then;
}
