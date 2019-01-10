package com.iot.device.controller;

import com.iot.device.BaseTest;
import com.iot.device.vo.req.DeviceStateInfoReq;
import com.iot.device.vo.req.DeviceStateReq;
import com.iot.device.vo.req.UserDeviceReq;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:33 2018/4/23
 * @Modify by:
 */
public class DeviceControllerTest extends BaseTest {


    @Override
    public String getBaseUrl() {
        return "/device/";
    }

    @Test
    public void findDeviceListByDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("000010000000141p3gtwqy714");
        deviceIds.add("000010000000547gclq714tw3");

        mockPost("findDeviceListByDeviceIds", deviceIds);

    }

    @Test
    public void addDeviceStatusListByDeviceId() {

        List<DeviceStateInfoReq> reqs = new ArrayList<>();

        DeviceStateInfoReq statusInfoReq = new DeviceStateInfoReq();
        statusInfoReq.setDeviceId("231");
        statusInfoReq.setLogDate(new Date());
        statusInfoReq.setPropertyDesc("dfs");
        statusInfoReq.setPropertyValue("34534");
        statusInfoReq.setPropertyName("354345");
        reqs.add(statusInfoReq);

        DeviceStateReq deviceStateReq = new DeviceStateReq();
        deviceStateReq.setDeviceId("1");
        deviceStateReq.setRemoveCache(false);
        deviceStateReq.setDeviceStateInfoReqList(reqs);
        mockPost("addDeviceStatusListByDeviceId", deviceStateReq);
    }

    @Test
    public void deleteUserDeviceByUserIdAndDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("userId", "2");
        params.put("deviceId", "00e647dae2cb8d403c168fce7e4628e0");
        mockGet("deleteUserDeviceByUserIdAndDeviceId", params);
    }

    @Test
    public void findDeviceInfoListByUserId() {
        Map<String,String> params = new HashMap<>();
        params.put("userId","2");
        mockGet("findDeviceInfoListByUserId", params);

    }

    @Test
    public void findUnDirectDeviceInfoListByUserId() {
        Map<String,String> params = new HashMap<>();
        params.put("userId","2");
        mockGet("findUnDirectDeviceInfoListByUserId", params);
    }

    @Test
    public void findDirectDeviceInfoListByUserId() {
        Map<String,String> params = new HashMap<>();
        params.put("userId","2");
        mockGet("findDirectDeviceInfoListByUserId", params);
    }

    @Test
    public void getDeviceByDeviceUUID() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceUUID", "000010000000549987280288z");
        mockGet("getDeviceByDeviceUUID", params);
    }

    @Test
    public void addChildDeviceByParentDeviceIdAndChildDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("parentDeviceId","000010000000549987280288z");
        params.put("childDeviceId","1");
        mockGet("addChildDeviceByParentDeviceIdAndChildDeviceId", params);
    }

    @Test
    public void delChildDeviceByDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId","1");
        mockGet("delChildDeviceByDeviceId", params);
    }

    @Test
    public void updateUserDeviceBoundRelationshipByUserIdAndDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId", "00001000000054813c4c7fl48");
        params.put("userId", "20");
        mockGet("updateUserDeviceBoundRelationshipByUserIdAndDeviceId", params);
    }

    @Test
    public void getUserDeviceByUserIdAndDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId","1");
        params.put("userId","1");
        mockGet("getUserDeviceByUserIdAndDeviceId", params);
    }

    @Test
    public void updateDeviceOnlineStatusByDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId","1");
        params.put("onlineStatus","1");
        mockGet("updateDeviceOnlineStatusByDeviceId", params);
    }

    @Test
    public void findDeviceListByUserId() {
        Map<String,String> params = new HashMap<>();
        params.put("userId","2");
        mockGet("findDeviceListByUserId", params);
    }

    @Test
    public void findUserDeviceListByDeviceId() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId","1");
        mockGet("findUserDeviceListByDeviceId", params);
    }


    @Test
    public void getDeviceByDeviceIp() {
        Map<String,String> params = new HashMap<>();
        params.put("deviceIp","1");
        mockGet("getDeviceByDeviceIp", params);
    }



    @Test
    public void updateUserDevice() {

        UserDeviceReq userDeviceReq = new UserDeviceReq();

        userDeviceReq.setDeviceId("4234235345");
        userDeviceReq.setUserId(325435L);
        userDeviceReq.setUserType("root");
        userDeviceReq.setEventNotifyEnabled(1);
        userDeviceReq.setPassword("436456465");

        mockPost("updateUserDevice", userDeviceReq);

    }

    @Test
    public void findDevPageByProductId() {

        Map<String, String> params = new HashMap<>();
        params.put("pageNum", "1");
        params.put("pageSize", "15");
        params.put("productId", "1090210037");

        mockGet("findDevPageByProductId", params);

    }
}