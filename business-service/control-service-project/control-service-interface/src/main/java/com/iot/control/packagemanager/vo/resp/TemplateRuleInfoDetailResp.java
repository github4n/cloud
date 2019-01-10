package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.rule.RuleDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.rule.RuleDetailInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@description 策略详情实体类bean
 *@author wucheng
 *@create 2018/12/12 14:38
 */
@ApiModel(value = "策略详情实体类bean")
@Data
public class TemplateRuleInfoDetailResp implements Serializable{
    @ApiModelProperty(name = "id", value = "策略id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "ruleName", value = "策略名称", dataType = "String")
    private String ruleName;

    @ApiModelProperty(name = "ruleDetailInfoResp", value = "配置详情", dataType = "RuleDetailInfoResp")
    private RuleDetailInfoResp ruleDetailInfoResp;

    public TemplateRuleInfoDetailResp() {
    }

    public TemplateRuleInfoDetailResp(Long id, String ruleName, RuleDetailInfoResp ruleDetailInfoResp) {
        this.id = id;
        this.ruleName = ruleName;
        this.ruleDetailInfoResp = ruleDetailInfoResp;
    }
}
