package com.iot.ifttt.vo;

import lombok.Data;

import java.util.Map;

/**
 * 描述：校验通知请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/29 11:04
 */
@Data
public class CheckNotifyReq {
    private String type;
    private Map<String, Object> fields;
}
