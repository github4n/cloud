package com.iot.control.packagemanager.vo.req.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 模板规则实体类
 *@author wucheng
 *@create 2018/11/22 16:36
 */
@ApiModel("模板实体类")
@Data
public class TemplateRuleReq{

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "type", value = "模板类型 0:安防 1:scene 2:ifttt;3:策略")
    private Integer type;

    @ApiModelProperty(name = "json", value = "规则体")
    private String json;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "ruleName", value = "规则名称")
    private String  ruleName;

    public TemplateRuleReq() {}

    public TemplateRuleReq(Long packageId, Long tenantId, Integer type, String json, Date createTime, Date updateTime, String ruleName) {
        this.packageId = packageId;
        this.tenantId = tenantId;
        this.type = type;
        this.json = json;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.ruleName = ruleName;
    }

    public TemplateRuleReq(Long id, String json, Date updateTime, String ruleName) {
        this.id = id;
        this.json = json;
        this.updateTime = updateTime;
        this.ruleName = ruleName;
    }
}
