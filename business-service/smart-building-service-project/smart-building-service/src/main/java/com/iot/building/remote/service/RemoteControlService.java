package com.iot.building.remote.service;

/**
 * @Author: linjihuang
 * @Descrpiton: 
 * @Date: 10:54 2018/10/12 
 * @Modify by:
 */
public interface RemoteControlService {

    /**
     * 遥控器下发
     * @param spaceId
     */
    public void synchronousRemoteControl(Long tenantId, Long spaceId);
    
}
