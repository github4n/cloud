package com.iot.airswitch.controller;

import com.iot.airswitch.api.AirSwitchApi;
import com.iot.airswitch.service.AirSwitchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/26
 * @Description: *
 */
@RestController
public class AirSwitchController implements AirSwitchApi {

    @Autowired
    private AirSwitchService airSwitchService;

    @Override
    public boolean eventPush(@RequestParam("info") String info) {
        return airSwitchService.eventPush(info);
    }

    @Override
    public boolean modifyDeviceName(@RequestParam("deviceId") String deviceId, @RequestParam("name") String name) {
        return airSwitchService.modifyDeviceName(deviceId, name);
    }

    @Override
    public boolean startup(@RequestParam("port") Integer port) {
        return airSwitchService.startup(port);
    }

    @Override
    public void syncDevice(@RequestParam("netInfo") String netInfo, @RequestParam("swInfo") String swInfo) {
        airSwitchService.syncDevice(netInfo, swInfo);
    }
    
    @Override
    public void synAirSwitch(@RequestParam("ip") String ip) {
        airSwitchService.synAirSwitch(ip);
    }

    @Override
    public void saveElectricity(@RequestParam("info") String info) {
        airSwitchService.saveElectricity(info);
    }

    @Override
    public void setServerAddress(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId, @RequestParam("ip") String ip, @RequestParam("port") String port) {
        airSwitchService.setServerAddress(tenantId, deviceId, ip, port);
    }

    @Override
    public String registerAirSwitch(@RequestParam("info") String info,@RequestParam("tenantId") Long tenantId, @RequestParam("ip") String ip, @RequestParam("locationId") Long locationId) {
        return airSwitchService.registerAirSwitch(info, tenantId, ip, locationId);
    }

    @Override
    public void leakageTest(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchService.leakageTest(deviceId, tenantId);
    }

    @Override
    public void switchOn(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchService.switchOn(deviceId, tenantId);
    }

    @Override
    public void switchOff(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchService.switchOff(deviceId, tenantId);
    }

    @Override
    public void setRTVI(@RequestParam("deviceId") String deviceId, @RequestParam("interval") Integer interval,@RequestParam("tenantId") Long tenantId) {
        airSwitchService.setRTVI(deviceId, interval);
    }

    @Override
    public void selfCheck(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchService.selfCheck(deviceId, tenantId);
    }

    @Override
    public void batchLeakageTest(@RequestBody List<String> deviceIdList) {
        airSwitchService.batchLeakageTest(deviceIdList);
    }
}
