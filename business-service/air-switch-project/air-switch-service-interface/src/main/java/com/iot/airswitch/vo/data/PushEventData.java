package com.iot.airswitch.vo.data;

/**
 * @Author: Xieby
 * @Date: 2018/10/22
 * @Description: *
 */
public class PushEventData {
    /**
     * 事件序列号
     */
    private String No;

    /**
     * 消息码
     */
    private String CODE;

    /**
     * 事件ID
     */
    private String EVT_ID;

    /**
     * 告警等级 0-记录 1-提醒 2-告警
     */
    private String EVT_LV;

    /**
     * 事件结果
     */
    private String EVT_RE;

    /**
     * 事件执行者ID 0x00~0x7F 表示设备 0x80~0xFF 保留
     */
    private String EVT_EX;

    /**
     * 事件发起者ID FFFFFF00表示通讯模块
     */
    private String EVT_SP;

    /**
     * 时间戳
     */
    private String TIME;

    /**
     * 附属消息
     */
    private String APX;

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getEVT_ID() {
        return EVT_ID;
    }

    public void setEVT_ID(String EVT_ID) {
        this.EVT_ID = EVT_ID;
    }

    public String getEVT_LV() {
        return EVT_LV;
    }

    public void setEVT_LV(String EVT_LV) {
        this.EVT_LV = EVT_LV;
    }

    public String getEVT_RE() {
        return EVT_RE;
    }

    public void setEVT_RE(String EVT_RE) {
        this.EVT_RE = EVT_RE;
    }

    public String getEVT_EX() {
        return EVT_EX;
    }

    public void setEVT_EX(String EVT_EX) {
        this.EVT_EX = EVT_EX;
    }

    public String getEVT_SP() {
        return EVT_SP;
    }

    public void setEVT_SP(String EVT_SP) {
        this.EVT_SP = EVT_SP;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getAPX() {
        return APX;
    }

    public void setAPX(String APX) {
        this.APX = APX;
    }
}
