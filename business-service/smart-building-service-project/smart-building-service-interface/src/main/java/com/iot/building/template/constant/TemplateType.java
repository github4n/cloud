package com.iot.building.template.constant;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述： 模板类型
 *
 * 创建人: yuChangXing
 * 创建时间: 2018/5/10 11:54
 * 修改人:
 * 修改时间：
 */
public enum TemplateType {
    // 套包
    kit("kit"),

    // 情景
    scene("scene"),

    // ifttt
    ifttt("ifttt"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    TemplateType(String value) {
        this.value = value;
    }

    public String toString() {
        return getValue();
    }
}
