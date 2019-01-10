package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@description 返回客户端策略
 *@author wucheng
 *@create 2018/11/22 16:49
 */
@ApiModel("策略返回对象")
@Data
public class TemplateRuleResp implements Serializable{

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "json", value = "json体")
    private String json;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "ruleName", value = "策略名称")
    private String  ruleName;

    @ApiModelProperty(name = "type", value = "套包类型")
    private Integer  type;
}
