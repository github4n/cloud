package com.iot.shcs.device.utils;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.req.CommDeviceExtendReq;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.CommDeviceStatusReq;
import com.iot.device.vo.req.DeviceReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import org.springframework.util.StringUtils;

/**
 * @Author: xfz @Descrpiton: @Date: 19:07 2018/6/13 @Modify by:
 */
public class DeviceCoreUtils {


    private static DeviceTypeApi deviceTypeApi= ApplicationContextHelper.getBean(DeviceTypeApi.class);

    public static DeviceReq buildDeviceInfo(String deviceName, String deviceId, Long tenantId) {
        DeviceReq device = new DeviceReq();

        CommDeviceInfoReq deviceInfoReq = new CommDeviceInfoReq();
        deviceInfoReq.setDeviceId(deviceId);
        deviceInfoReq.setTenantId(tenantId);
        deviceInfoReq.setName(deviceName);
        CommDeviceExtendReq deviceExtendReq = new CommDeviceExtendReq();
        deviceExtendReq.setTenantId(tenantId);

        CommDeviceStatusReq deviceStatusReq = new CommDeviceStatusReq();
        deviceStatusReq.setTenantId(tenantId);

        device.setDeviceInfoReq(deviceInfoReq);
        device.setDeviceExtendReq(deviceExtendReq);
        device.setDeviceStatusReq(deviceStatusReq);

        return device;
    }

    public static MqttMsgAck buildLocaleByLocalMessage(int code, String message) {
        LocaleMessageSourceService localeMessageSourceService =
                ApplicationContextHelper.getBean(LocaleMessageSourceService.class);
        String localeMessage = null;
        try {
            localeMessage = localeMessageSourceService.getMessage(message);
        } catch (Exception e) {
            localeMessage = message;
        }
        if (StringUtils.isEmpty(localeMessage)) {
            localeMessage = message;
        }
        MqttMsgAck ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessage);
        return ack;
    }

    public static MqttMsg buildMqttMsg(
            String service,
            String topicMethod,
            String topic,
            Object payload,
            MqttMsgAck ack,
            String seq,
            String srcAddress) {
        MqttMsg msg = new MqttMsg(service, topicMethod, payload);
        msg.setSeq(seq);
        msg.setSrcAddr(srcAddress);
        msg.setPayload(payload);
        msg.setTopic(topic);
        msg.setAck(ack);
        return msg;
    }

    /**
     * 是否 gateway 产品
     *
     * @return
     */
    public static boolean isGateWayProduct(GetProductInfoRespVo productResp) {
        if (productResp == null || productResp.getIsDirectDevice() == null || productResp.getIsDirectDevice() == 0) {
            return false;
        }
        DeviceTypeResp deviceTypeRes = deviceTypeApi.getDeviceTypeById(productResp.getDeviceTypeId());
        if (deviceTypeRes!=null){
            if(deviceTypeRes.getDeviceCatalogId() != null && deviceTypeRes.getDeviceCatalogId() == 1){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
//        if (StringUtil.isNotBlank(productResp.getModel())) {
//            String model = productResp.getModel().toLowerCase();
//            if (model.contains(".gateway.") || model.contains(".siren_hub.")) {
//                return true;
//            }
//        }
//        return false;
    }
}
