package com.iot.shcs.userprofile.constant;

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
public enum UserProfileType {
    // ios 情景 short cut
    SCENE_SHORT_CUT("scene_short_cut");

    private String type;

    UserProfileType(String type) {
        this.type = type;
    }

    public static UserProfileType getProfileType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        for (UserProfileType widgetType : values()) {
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
