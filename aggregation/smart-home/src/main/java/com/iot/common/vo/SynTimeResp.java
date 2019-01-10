package com.iot.common.vo;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/7/13 18:31
 * 修改人:
 * 修改时间：
 *
 *  同步时间响应
 */
public class SynTimeResp implements Serializable {
    // 设备端记录请求发出的时间
    private String t1;
    // 服务器接收到的时间
    private String t2;
    // 服务器发出的时间搓
    private String t3;
    // utc时间戳
    private long timestamp;

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
