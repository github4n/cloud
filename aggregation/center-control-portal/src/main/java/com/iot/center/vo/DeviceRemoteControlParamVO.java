package com.iot.center.vo;

import com.iot.center.enums.DeviceRemoteControlParamEnum;
import com.iot.center.enums.DeviceRemoteControlTypeEnum;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
public class DeviceRemoteControlParamVO {
    private String name;
    private String code;

    public DeviceRemoteControlParamVO() {

    }
    public DeviceRemoteControlParamVO(DeviceRemoteControlParamEnum deviceRemoteControlParamEnum) {
        this.name=deviceRemoteControlParamEnum.getName();
        this.code=deviceRemoteControlParamEnum.getCode();
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
