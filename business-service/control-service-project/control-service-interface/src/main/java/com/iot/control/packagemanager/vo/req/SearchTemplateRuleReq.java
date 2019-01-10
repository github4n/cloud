package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "搜索实体类")
@Data
public class SearchTemplateRuleReq {

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "ruleName", value = "规则名称")
    private String  ruleName;

    public SearchTemplateRuleReq() {
    }

    public SearchTemplateRuleReq(Long packageId, Long tenantId) {
        this.packageId = packageId;
        this.tenantId = tenantId;
    }
}
