package com.iot.device.vo.rsp;

import java.io.Serializable;

public class EnergyRsp implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 电量值
     */
    private String value;

    /**
     * 时间值
     */
    private String time;

    private String area;

    private Integer step;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
