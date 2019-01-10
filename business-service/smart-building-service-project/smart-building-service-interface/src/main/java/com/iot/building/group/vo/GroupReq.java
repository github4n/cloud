package com.iot.building.group.vo;

public class GroupReq extends GroupCommonVo{

    public GroupReq(String groupId, String name, String gatewayId, String model, String deviceId) {
        super(groupId, name, gatewayId, model, deviceId);
    }
    public GroupReq() {
    }
}
