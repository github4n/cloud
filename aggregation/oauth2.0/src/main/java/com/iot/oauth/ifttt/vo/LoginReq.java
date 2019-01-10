package com.iot.oauth.ifttt.vo;

import lombok.Data;

/**
 * 描述：登录请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/26 20:10
 */
@Data
public class LoginReq {
    private String userName;
    private String password;
    private String state;
    private String redirectUrl;
    private String flag;
    private Long tenantId;
}
