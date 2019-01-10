package com.iot.building.device.vo;


import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
public class DeviceRemoteTypeResp {
    private Long id;///主键
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceRemoteTypeResp() {
    }

    public DeviceRemoteTypeResp(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
