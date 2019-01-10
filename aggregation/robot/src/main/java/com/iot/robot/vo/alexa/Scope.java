package com.iot.robot.vo.alexa;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/7 17:10
 * @Modify by:
 */
public class Scope {
    private String type = "BearerToken";
    private String token;
    public String getType() {
        return type;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
