package com.iot.smarthome.vo;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/18 14:57
 * @Modify by:
 */
public class ActivityVo implements Serializable {
    private String activity;
    private String time;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
