package com.iot.shcs.device.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 18:00 2018/11/14
 * @Modify by:
 */
@Data
@ToString
public class TestOnlineReq {

    private String parentDeviceId;

    private String deviceId;

}
