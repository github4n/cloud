/*
 * 项目名称 : common-web
 * 创建日期 : 2018年1月29日
 * 修改历史 :
 *     1. [2018年1月29日]创建文件 by linjihuang
 */
package com.iot.space.domain;

/**
 * 描述：空间设备表VO
 *
 * @author linjihuang
 */
public class SpaceDeviceVO {

    private String deviceId;

    private String spaceId;

    private String parentId;

    private String name;

    private String type;

    private String status;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
