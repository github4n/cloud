package com.iot.ifttt.vo;

import lombok.Data;

import java.util.Map;

/**
 * 描述：校验程序请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/29 11:01
 */
@Data
public class CheckTriggerReq {
    private String type;
    private Long userId;
    private String identity;
    private Map<String, Object> triggerFields;
    private Long tenantId;
}
