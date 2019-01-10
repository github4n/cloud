package com.iot.portal.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.resp.rule.RuleDetailInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: cloud
 * @description: 策略列表实体类
 * @author: yeshiyuan
 * @create: 2018-12-13 11:32
 **/
@ApiModel(description = "策略列表实体类")
@Data
public class RuleListResp {

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "ruleName", value = "策略名称")
    private String ruleName;

    @ApiModelProperty(name = "ruleDetail", value = "策略详情")
    private RuleDetailInfoResp ruleDetail;

    public RuleListResp() {
    }

    public RuleListResp(Long id, String ruleName) {
        this.id = id;
        this.ruleName = ruleName;
    }
}
