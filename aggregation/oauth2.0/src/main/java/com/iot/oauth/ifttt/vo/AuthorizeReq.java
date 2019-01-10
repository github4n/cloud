package com.iot.oauth.ifttt.vo;

import lombok.Data;

/**
 * 描述：授权请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/26 17:34
 */
@Data
public class AuthorizeReq {
    private String client_id;
    private String redirect_uri;
    private String response_type;
    private String code;
    private String state;
}
