package com.iot.control.packagemanager.vo.req.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * @despriction：套包策略信息
  * @author  yeshiyuan
  * @created 2018/12/5 17:41
  */
@Data
@ApiModel(value = "套包策略信息", description = "套包策略信息")
public class TemplateRuleInfoReq {

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "templateRuleId", value = "策略id", dataType = "Long")
    private Long templateRuleId;

    @ApiModelProperty(name = "ruleName", value = "规则名称")
    private String ruleName;

    @ApiModelProperty(name = "detail", value = "策略详情", dataType = "RuleDetailInfoReq")
    private RuleDetailInfoReq detail;

    @ApiModelProperty(name = "ruleType", value = "策略类型")
    private Integer ruleType;

    public TemplateRuleInfoReq() {
    }


}
