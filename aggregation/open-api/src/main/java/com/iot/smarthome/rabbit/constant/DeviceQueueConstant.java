package com.iot.smarthome.rabbit.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/15 13:40
 * @Modify by:
 */
public class DeviceQueueConstant {


    // 设备属性变化 队列
    public static final String SET_DEV_ATTR_NOTIFY_QUEUE = "openApi_devAttrNotify";
    // 设备disConnect 队列
    public static final String DEV_DISCONNECT_QUEUE = "openApi_devDisconnect";
    // 设备connect 队列
    public static final String DEV_CONNECT_QUEUE = "openApi_devConnect";

    // 设备删除 队列
    public static final String DEVICE_DELETE_QUEUE = "openApi_deviceDelete";

    // 设备新增、更新 队列
    public static final String DEVICE_ADD_OR_UPDATE_QUEUE = "openApi_deviceAddOrUpdate";





}
