package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class PageDeviceTypeByParamsReq {

    private Long tenantId;
    
    private Long orgId;

    private Integer pageSize = 10;

    private Integer pageNumber = 1;

}
