package com.iot.center.vo;

import com.iot.center.enums.DeviceRemoteControlTypeEnum;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
public class DeviceRemoteControlTypeVO {
    private String name;
    private String code;

    public DeviceRemoteControlTypeVO() {

    }
    public DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum deviceRemoteControlTypeEnum) {
        this.name=deviceRemoteControlTypeEnum.getName();
        this.code=deviceRemoteControlTypeEnum.getCode();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
