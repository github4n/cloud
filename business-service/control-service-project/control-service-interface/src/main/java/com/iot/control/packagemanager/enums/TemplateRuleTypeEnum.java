package com.iot.control.packagemanager.enums;
/**
 *@description template_rule 模板类型 type 枚举
 *@author wucheng
 *@create 2018/11/23 10:09
 */
public enum TemplateRuleTypeEnum {

    TYPE_SECURITY(0, "安防"),
    TYPE_SCENE(1, "scene"),
    TYPE_IFTTT(2, "ifttt"),
    TYPE_STRATEGY(3, "策略");

    private int code;
    private String description;

    TemplateRuleTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
