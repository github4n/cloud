package com.iot.ifttt.vo;

/**
 * 描述：获取自动化的列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/9 9:10
 */
public class ListReq {
    private String cookieUserId;
    private String homeId;

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }
}
