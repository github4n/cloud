package com.iot.device.web.vo.resp;

import lombok.Data;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:13 2018/9/25
 * @Modify by:
 */
@Data
public class DeviceInfoListResp {

    private String deviceId;

    private CommDeviceInfoResp deviceInfo;

    private CommDeviceExtendResp deviceExtend;

    //集合
    private CommDeviceStateResp deviceState;

    private CommDeviceStatusResp deviceStatus;

    private CommDeviceTypeInfoResp deviceType;

    private CommProductInfoResp productInfo;
}
