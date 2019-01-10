package com.iot.ifttt.vo;

import lombok.Data;

/**
 * 描述：获取产品信息请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/12/19 17:35
 */
@Data
public class GetProductReq {
    private Long tenantId;
    private String type;
}
