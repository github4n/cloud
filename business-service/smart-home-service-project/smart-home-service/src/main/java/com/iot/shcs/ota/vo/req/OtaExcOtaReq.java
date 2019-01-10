package com.iot.shcs.ota.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:21 2018/5/30
 * @Modify by:
 */
public class OtaExcOtaReq implements Serializable {

    private List<String> devId;

    private String timestamp;

    public List<String> getDevId() {
        return devId;
    }

    public void setDevId(List<String> devId) {
        this.devId = devId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
