package com.iot.cloud.vo;

import com.iot.vo.UserBaseVo;

public class SingleSpaceVo extends UserBaseVo {

    private Long spaceId;

    private String cookieUserId;

    private String cookieUserToken;

    private Long homeId;

    private Long roomId;

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public String getCookieUserToken() {
        return cookieUserToken;
    }

    public void setCookieUserToken(String cookieUserToken) {
        this.cookieUserToken = cookieUserToken;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}
