package com.iot.shcs.device.vo;

import java.io.Serializable;

public class AirConditionVO implements Serializable {
    private String atuId;
    private String atuName;
    private String atuPwd;
    private String atuRoom;
    private String sortLetters;

    public AirConditionVO() {
    }

    public AirConditionVO(String atuId, String atuName, String atuPwd, String atuRoom, String sortLetters) {
        this.atuId = atuId;
        this.atuName = atuName;
        this.atuPwd = atuPwd;
        this.atuRoom = atuRoom;
        this.sortLetters = sortLetters;
    }

    public String getAtuId() {
        return atuId;
    }

    public void setAtuId(String atuId) {
        this.atuId = atuId;
    }

    public String getAtuName() {
        return atuName;
    }

    public void setAtuName(String atuName) {
        this.atuName = atuName;
    }

    public String getAtuPwd() {
        return atuPwd;
    }

    public void setAtuPwd(String atuPwd) {
        this.atuPwd = atuPwd;
    }

    public String getAtuRoom() {
        return atuRoom;
    }

    public void setAtuRoom(String atuRoom) {
        this.atuRoom = atuRoom;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
