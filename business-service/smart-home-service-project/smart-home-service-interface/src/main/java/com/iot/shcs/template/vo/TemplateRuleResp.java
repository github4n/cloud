package com.iot.shcs.template.vo;

import lombok.Data;

import java.util.Date;

/**
 * 描述：套包规则应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/12/20 16:33
 */
@Data
public class TemplateRuleResp {

    /**
     * 主键
     */
    private Long id;
    /**
     * 套包主键
     */
    private Long packageId;
    /**
     * 租户主键
     */
    private Long tenantId;
    /**
     * 模板类型 0安防 1scene 2 ifttt
     */
    private Integer type;
    /**
     * 规则体
     */
    private String json;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
