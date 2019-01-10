package com.iot.control.packagemanager.vo.req.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@description 修改策略实体类
 *@author wucheng
 *@create 2018/12/11 15:36
 */
@Data
public class UpdateTemplateRuleReq implements Serializable{

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "json", value = "规则体")
    private String json;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "ruleName", value = "规则名称")
    private String  ruleName;

    public UpdateTemplateRuleReq() {}

    public UpdateTemplateRuleReq(Long id, String json, Date updateTime, String ruleName) {
        this.id = id;
        this.json = json;
        this.updateTime = updateTime;
        this.ruleName = ruleName;
    }
}
