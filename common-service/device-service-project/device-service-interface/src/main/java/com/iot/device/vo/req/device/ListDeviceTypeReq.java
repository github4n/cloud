package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 9:48 2018/10/11
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ListDeviceTypeReq {

    //设备ids
    private List<Long> deviceTypeIds;

}
