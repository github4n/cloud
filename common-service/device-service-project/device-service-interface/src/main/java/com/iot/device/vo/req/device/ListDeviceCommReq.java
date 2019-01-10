package com.iot.device.vo.req.device;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:57 2018/10/10
 * @Modify by:
 */
@Data
@ToString
public class ListDeviceCommReq {


    //租户id
    @NotNull(message = "tenant.id.not.null")
    private Long tenantId;

    //设备ids
    private List<String> deviceIds;

    //一次批量获取几个
    private Integer batchSize = 10;

}
