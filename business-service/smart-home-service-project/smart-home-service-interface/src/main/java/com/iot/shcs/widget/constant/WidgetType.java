package com.iot.shcs.widget.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/4 10:02
 * 修改人:
 * 修改时间：
 */
public enum WidgetType {
    SECURITY("security"),
    DEVICE("device"),
    SCENE("scene");

    private String type;

    WidgetType(String type) {
        this.type = type;
    }

    public static WidgetType getWidgetType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        for (WidgetType widgetType : values()) {
            if (widgetType.getType().equals(type)) {
                return widgetType;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
