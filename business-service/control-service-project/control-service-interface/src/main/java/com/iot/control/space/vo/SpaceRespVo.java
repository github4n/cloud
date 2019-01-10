package com.iot.control.space.vo;

import java.io.Serializable;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/02 14:28
 **/
public class SpaceRespVo implements Serializable{

    private String homeId;

    private String roomId;

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
