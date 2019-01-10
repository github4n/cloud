package com.iot.shcs.device.controller;

import com.iot.shcs.device.vo.DevInfoReq;
import com.iot.shcs.device.vo.SubDevReq;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartHomeDeviceCoreControllerTest {

    @Autowired
    private SmartHomeDeviceCoreController smartHomeDeviceCoreController;

    @Test
    public void addBleSubDevTest() {
        SubDevReq req = new SubDevReq();
        req.setTenantId(2l);
        req.setAddress(1l);
        req.setComMode("BLE");
        req.setDevId("d55a8914aea247ceaebfaad3de341789");
        req.setDevName("DIM-01");
        req.setIsAppDev(1);
        req.setUserUuid("54d0feba62924fe6a14d26c6aca6fce8");
        req.setProductId("lds.Light_Dimmable.020110010");
        String name = smartHomeDeviceCoreController.addBleSubDev(req);
        System.out.println(name);
    }

    @Test
    public void getDevInfo() {
        DevInfoReq req = new DevInfoReq();
        req.setTenantId(2l);
        req.setDevId("368633e5f94a4e6586527cf861c9ce0c");
        smartHomeDeviceCoreController.getDevInfo(req);
    }

    @Test
    public void delSubDev(){
        SubDevReq req = new SubDevReq();
        req.setDevId("d55a8914aea247ceaebfaad3de341656");
        req.setTenantId(2l);
        req.setUserUuid("96e21b9bfc6747f39ab425b7be6fedf2");
        smartHomeDeviceCoreController.delBleSubDev(req);
    }
}