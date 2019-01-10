package com.iot.ifttt.vo;

import lombok.Data;

import java.util.Map;

/**
 * 描述：控制设备请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/12/19 19:30
 */
@Data
public class ActionReq {
    private String type;
    private Long tenantId;
    private Long userId;
    private String uuid;
    private Map<String,Object> actionFields;
}
