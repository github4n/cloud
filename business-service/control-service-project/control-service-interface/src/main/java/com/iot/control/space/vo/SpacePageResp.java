package com.iot.control.space.vo;

import java.io.Serializable;

public class SpacePageResp implements Serializable {

    private String homeId;

    private String name;

    private Integer defaultHome;

    private String icon;
    
    private Boolean isSecurityPwd;

    private String roomId;

    private Integer devNum;

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultHome() {
        return defaultHome;
    }

    public void setDefaultHome(Integer defaultHome) {
        this.defaultHome = defaultHome;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsSecurityPwd() {
        return isSecurityPwd;
    }

    public void setIsSecurityPwd(Boolean securityPwd) {
        isSecurityPwd = securityPwd;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getDevNum() {
        return devNum;
    }

    public void setDevNum(Integer devNum) {
        this.devNum = devNum;
    }
}
