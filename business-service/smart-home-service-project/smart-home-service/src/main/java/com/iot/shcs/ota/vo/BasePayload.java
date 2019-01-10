package com.iot.shcs.ota.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:01 2018/5/29
 * @Modify by:
 */
public class BasePayload implements Serializable {
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String timestamp = SIMPLE_DATE_FORMAT.format(new Date());

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
