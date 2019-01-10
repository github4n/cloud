package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;

public class DeviceStateResp implements Serializable {

    /***/
    private static final long serialVersionUID = 5694232069085088357L;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 属性描述
     */
    private String propertyDesc;

    /**
     * 设备属性名称
     */
    private String propertyName;

    /**
     * 设备属性值
     */
    private String propertyValue;

    /**
     * 状态上报时间
     */
    private Date logDate;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPropertyDesc() {
        return propertyDesc;
    }

    public void setPropertyDesc(String propertyDesc) {
        this.propertyDesc = propertyDesc;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return "DeviceStatusInfoVO [deviceId=" + deviceId + ", propertyDesc="
                + propertyDesc + ", propertyName=" + propertyName
                + ", propertyValue=" + propertyValue + ", logDate=" + logDate
                + "]";
    }

}
