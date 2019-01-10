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
 * @Date: 11:04 2018/9/25
 * @Modify by:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ListDeviceInfoReq {

    //设备ids
    private List<String> deviceIds;

    //一次批量获取几个
    private Integer batchSize = 10;



}
