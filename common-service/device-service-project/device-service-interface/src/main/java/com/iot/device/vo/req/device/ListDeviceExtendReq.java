package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
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
public class ListDeviceExtendReq {

    //租户id
    @NotNull(message = "tenant.id.not.null")
    private Long tenantId;

    //设备ids
    private List<String> deviceIds;


}
