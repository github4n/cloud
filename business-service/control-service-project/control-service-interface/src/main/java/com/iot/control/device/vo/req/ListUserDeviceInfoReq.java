package com.iot.control.device.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:44 2018/10/12
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ListUserDeviceInfoReq {

    @NotNull(message = "tenantId.notnull")
    Long tenantId;

    Long orgId;

    Long userId;

    String deviceId;

    //sub 子设备 root
    String userType;


}
