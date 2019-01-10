package com.iot.shcs.space.service;

import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.shcs.space.util.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:24 2018/11/28
 * @Modify by:
 */
@Slf4j
public class SpaceCoreMainTest {

    public static void main(String[] args) {
        com.iot.shcs.space.vo.SpaceDeviceReq newSpaceDevice = new com.iot.shcs.space.vo.SpaceDeviceReq();
        newSpaceDevice.setCreateTime(new Date());
        newSpaceDevice.setUpdateTime(new Date());
        newSpaceDevice.setLocationId(1L);
        newSpaceDevice.setDeviceId("eb9ea63f713c4fad10d4c901008d1500");
        newSpaceDevice.setSpaceId(1L);
        newSpaceDevice.setTenantId(2L);
        newSpaceDevice.setOrder(1);

        SpaceDeviceReq req = BeanCopyUtil.copyProperties(newSpaceDevice);
        log.info("req{}", req);
    }

}
