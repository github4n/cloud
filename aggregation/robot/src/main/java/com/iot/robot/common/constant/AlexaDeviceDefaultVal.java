package com.iot.robot.common.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/14 14:58
 * @Modify by:
 */
public enum  AlexaDeviceDefaultVal {
    // 开关功能 默认值
    POWER_STATE("powerState", "OnOff", 0);


    private String supportName;
    private String key;
    private Object val;

    AlexaDeviceDefaultVal(String supportName, String key, Object val) {
        this.supportName = supportName;
        this.key = key;
        this.val = val;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
}
