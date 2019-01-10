package com.iot.control.packagemanager.vo.req.rule;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.execption.PackageExceptionEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@description 保存策略入参
 *@author wucheng
 *@create 2018/12/5 10:51
 */
@ApiModel(value = "保存策略入参")
@Data
public class SaveRuleInfoReq {

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "templateRuleId", value = "策略id", dataType = "Long")
    private Long templateRuleId;

    @ApiModelProperty(name = "enabled", value = "是否使用", dataType = "Long")
    private Integer enabled;

    @ApiModelProperty(name = "ruleName", value = "策略名称", dataType = "String")
    private String ruleName;

    @ApiModelProperty(name = "detail", value = "配置详情", dataType = "RuleDetailInfoReq")
    private RuleDetailInfoReq detail;

    /**
      * @despriction：参数校验
      * @author  yeshiyuan
      * @created 2018/12/10 20:20
      */
    public static void chechParam(SaveRuleInfoReq saveRuleInfoReq) {
        if (saveRuleInfoReq.getPackageId() == null) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "package id is null");
        }
        if (StringUtil.isBlank(saveRuleInfoReq.getRuleName())) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "rule name is null");
        }
        if (saveRuleInfoReq.getDetail() == null) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "package rule config is null");
        }
    }
}
