package com.iot.cloud.vo;

import io.swagger.annotations.Api;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/18
 * 登录接口的实体类
 */
@Api
public class LoginVo {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
