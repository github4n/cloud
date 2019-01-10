package com.iot.device.vo.req.voicebox;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/29 9:14
 * @Modify by:
 */

@Data
@ToString
public class AddSmartDataPointReq implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    // service_module_property.id
    private Long propertyId;

    private Long tenantId;

    private Long createBy;

    private List<SmartDataPointReq> smartDataPointList;
}
