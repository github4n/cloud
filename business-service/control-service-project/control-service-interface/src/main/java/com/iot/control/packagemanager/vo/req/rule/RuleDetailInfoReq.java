package com.iot.control.packagemanager.vo.req.rule;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @despriction：策略详情
 * @author  yeshiyuan
 * @created 2018/11/23 15:17
 */
@Data
@ApiModel(description = "策略详情")
public class RuleDetailInfoReq {

    @ApiModelProperty(name = "security", value = "安防信息", dataType = "SecurityInfoReq")
    private SecurityInfoReq security;

    @ApiModelProperty(name = "ruleIf", value = "if详情", dataType = "RuleIfInfoReq")
    private RuleIfInfoReq ruleIf;

    @ApiModelProperty(name = "then", value = "then详情", dataType = "RuleThenInfoReq")
    private RuleThenInfoReq then;

}
