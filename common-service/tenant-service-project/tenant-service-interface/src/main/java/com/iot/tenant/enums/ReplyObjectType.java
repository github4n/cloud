package com.iot.tenant.enums;

import com.iot.common.util.StringUtil;

/**
 * 项目名称：cloud
 * 功能描述：回复对象类型
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:58
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:58
 * 修改描述：
 */
public enum ReplyObjectType {

    app("app","APP"),
    app_package("app_package","app打包"),
    service_review("service_review", "虚拟服务审核"),
    product_review("product_review", "产品审核");


    private String value;

    private String desc;

    ReplyObjectType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean checkType(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        boolean result = false;
        for (ReplyObjectType replyObjectType : ReplyObjectType.values() ) {
            if (replyObjectType.getValue().equals(str)){
                result = true;
                break;
            }
        }
        return result;
    }
}
