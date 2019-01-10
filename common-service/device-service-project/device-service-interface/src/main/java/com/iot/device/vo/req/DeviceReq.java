package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
@Data
@ToString
public class DeviceReq implements Serializable {

    private String deviceId;

    private Long tenantId;

    private Long orgId;

    private CommDeviceInfoReq deviceInfoReq;

    private CommDeviceStatusReq deviceStatusReq;

    private CommDeviceExtendReq deviceExtendReq;

    /**
     * 一个设备对应多个固件类型版本
     */
    private List<CommDeviceFwTypeVersionReq> fwTypeVersionReqList;


}
