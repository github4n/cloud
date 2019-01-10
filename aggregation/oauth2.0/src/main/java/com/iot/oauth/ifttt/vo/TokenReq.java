package com.iot.oauth.ifttt.vo;

import lombok.Data;

/**
 * 描述：令牌请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/26 21:05
 */
@Data
public class TokenReq {
    private String grant_type;
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String refresh_token;
}
