package com.iot.device.vo.req.device;

import lombok.*;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:56 2018/10/19
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ListDeviceByParamsReq {

    private Long productId;

    private Long deviceTypeId;

    private String deviceType;

    private List<String> deviceIds;
    
    private Long deviceBusinessTypeId;

    private Long locationId;
    
    private Long tenantId;

    private Long orgId;

}
