package com.iot.shcs.device.controller;

import com.google.common.collect.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.service.IDeviceMQTTService;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.vo.SetDevAttrNotifReq;
import com.iot.shcs.device.vo.TestOnlineReq;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:41 2018/8/6
 * @Modify by:
 */
@RestController
@RequestMapping("/test/device")
public class DeviceTestHttpController {

    @Autowired
    @Qualifier("device")
    private IDeviceMQTTService deviceMQTTService;

    @Autowired
    private DeviceCoreService deviceCoreService;
//    @Autowired
//    private DevAttrSender devAttrSender;

    @GetMapping("/test")
    public CommonResponse test() {
        deviceCoreService.getDeviceInfoByDeviceId("222222222222");
        return CommonResponse.success();
    }

    @RequestMapping(value = "/setDevAttrNotif", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse setDevAttrNotif(@RequestBody SetDevAttrNotifReq devAttrNotifReq) {
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("attr", devAttrNotifReq.getAttr());
        payload.put("devId", devAttrNotifReq.getDevId());
        payload.put("online", devAttrNotifReq.getAttr().get("online"));
        MqttMsg msg = new MqttMsg();
        msg.setPayload(payload);
        String reqTopic = "iot/v1/cb/" + devAttrNotifReq.getParentDeviceId() + "/device/setDevAttrNotif";
        msg.setTopic(reqTopic);
        msg.setSeq(RandomUtils.nextInt(10) + "");
        SaaSContextHolder.setCurrentTenantId(1L);
        deviceMQTTService.setDevAttrNotif(msg, reqTopic);
        return CommonResponse.success();
    }

    @RequestMapping(value = "/devStatusNotif", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse devStatusNotif(@RequestBody TestOnlineReq params) {
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("devId", params.getDeviceId());
        payload.put("online", 1);//1 上线 0 下线
        MqttMsg msg = new MqttMsg();
        msg.setPayload(payload);
        String reqTopic = "iot/v1/cb/" + params.getParentDeviceId() + "/device/devStatusNotif";
        msg.setTopic(reqTopic);
        msg.setSeq(RandomUtils.nextInt(10) + "");

        SaaSContextHolder.setCurrentTenantId(1L);
        deviceMQTTService.devStatusNotif(msg, reqTopic);
        return CommonResponse.success();
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse connect(@RequestBody TestOnlineReq params) {
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("devId", params.getDeviceId());
        payload.put("online", 1);//1 上线 0 下线
        MqttMsg msg = new MqttMsg();
        msg.setPayload(payload);
        String reqTopic = "iot/v1/cb/" + params.getParentDeviceId() + "/device/connect";
        msg.setTopic(reqTopic);
        msg.setSeq(RandomUtils.nextInt(10) + "");

        SaaSContextHolder.setCurrentTenantId(1L);
        deviceMQTTService.connect(msg, reqTopic);
        return CommonResponse.success();
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse disconnect(@RequestBody TestOnlineReq params) {
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("devId", params.getDeviceId());
        payload.put("online", 1);//1 上线 0 下线
        MqttMsg msg = new MqttMsg();
        msg.setPayload(payload);
        String reqTopic = "iot/v1/cb/" + params.getParentDeviceId() + "/device/disconnect";
        msg.setTopic(reqTopic);
        msg.setSeq(RandomUtils.nextInt(10) + "");

        SaaSContextHolder.setCurrentTenantId(1L);
        deviceMQTTService.disconnect(msg, reqTopic);
        return CommonResponse.success();
    }
}
