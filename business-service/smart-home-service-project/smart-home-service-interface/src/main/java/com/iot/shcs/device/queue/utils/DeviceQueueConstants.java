package com.iot.shcs.device.queue.utils;

/**
 * @Author: lucky
 * @Descrpiton: device 相关队列
 * @Date: 10:37 2018/8/10
 * @Modify by:
 */
public interface DeviceQueueConstants {

    /**
     * 属性通知队列
     */
    String DEV_ATTR_QUEUE = "dev.attribute.message";

    String DEV_EMAIL_WARN_EXCHANGE = "dev.email.warn.exchange";

    String DEV_EMAIL_WARN_ROUTING = "dev.email.warn.routing";
    /**
     * 短信邮件告警【如：设备id 被不同类型的设备烧入使用】
     */
    String DEV_EMAIL_WARN_QUEUE = "dev.email.warn.message";


    String SET_DEV_ATTR_NOTIFY_EXCHANGE = "set-dev-attr-exchange";
    String SET_DEV_ATTR_NOTIFY_ROUTING = "set-dev-attr-routing";

    String TWO_C_SET_DEV_ATTR_NOTIFY_QUEUE = "setDevAttrNotify-queue";

    String DEVICE_ADD_OR_UPDATE_EXCHANGE = "device-update-exchange";
    String DEVICE_ADD_OR_UPDATE_ROUTING = "device-update-routing";

    String DEVICE_DELETE_EXCHANGE = "device-delete-exchange";
    String DEVICE_DELETE_ROUTING = "device-delete-routing";

    String DEVICE_CONNECT_EXCHANGE = "device-connect-exchange";
    String DEVICE_CONNECT_ROUTING = "device-connect-routing";

    String DEVICE_DISCONNECT_EXCHANGE = "device-disconnect-exchange";
    String DEVICE_DISCONNECT_ROUTING = "device-disconnect-routing";

    String DEV_EVENT_NOTIF_EXCHANGE = "dev_event_notif_exchange";
    String DEV_EVENT_NOTIF_ROUTING = "dev_event_notif_routing";

    String DEV_TAMPER_EVENT_NOTIFY_QUEUE = "devTamperEventNotif-queue";
}
