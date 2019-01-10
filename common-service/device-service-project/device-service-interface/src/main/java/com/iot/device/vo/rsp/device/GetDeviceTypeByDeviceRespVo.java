package com.iot.device.vo.rsp.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: lucky
 * @Descrpiton: 根据设备id获取设备信息 和设备对应的类型
 * @Date: 13:59 2018/10/16
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class GetDeviceTypeByDeviceRespVo {

    private GetDeviceInfoRespVo deviceInfo;

    private GetProductInfoRespVo productInfo;

    private GetDeviceTypeInfoRespVo deviceTypeInfo;
}
