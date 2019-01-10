package com.iot.device.enums.product;

/**
 * 项目名称：cloud
 * 功能描述：产品发布状态枚举（product_publish_history的publish_status字段）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/25 11:08
 * 修改人： yeshiyuan
 * 修改时间：2018/10/25 11:08
 * 修改描述：
 */
public enum ProductPublishStatusEnum {

    REVIEWING("Reviewing"),
    FAIL("Post Failure"),
    SUCCESS("Post Success");

    private String value;

    public String getValue() {
        return value;
    }

    ProductPublishStatusEnum(String value) {
        this.value = value;
    }
}
