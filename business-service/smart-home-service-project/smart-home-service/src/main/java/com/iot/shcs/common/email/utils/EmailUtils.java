package com.iot.shcs.common.email.utils;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:00 2018/8/10
 * @Modify by:
 */
public class EmailUtils {

    public static String DEVICE_PRODUCT_CHANGE_TITLE = "设备id:%s,对应出现重大生产问题";

    public static String DEVICE_PRODUCT_CHANGE_CONTENT = "设备id:%s,原产品id:%s 尝试修改成:%s...警告.出现操作生产事故...设备证书异常";

    public static String DEVICE_MAC_CHANGE_TITLE = "设备id:%s,对应出现重大生产问题";

    public static String DEVICE_MAC_CHANGE_CONTENT = "设备id:%s,原产品mac:%s 尝试修改成:%s...警告.出现生产事故...";

    public static String getDeviceProductChangeTitle(String deviceId) {
        return String.format(DEVICE_PRODUCT_CHANGE_TITLE, deviceId);
    }

    public static String getDeviceProductChangeContent(String deviceId, Long origProductId, Long targetProductId) {
        return String.format(DEVICE_PRODUCT_CHANGE_CONTENT, deviceId, String.valueOf(origProductId), String.valueOf(targetProductId));
    }

    public static String getDeviceMacChangeTitle(String deviceId) {
        return String.format(DEVICE_MAC_CHANGE_TITLE, deviceId);
    }

    public static String getDeviceMacChangeContent(String deviceId, String origMac, String targetMac) {
        return String.format(DEVICE_MAC_CHANGE_CONTENT, deviceId, String.valueOf(origMac), String.valueOf(targetMac));
    }

}
