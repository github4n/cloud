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
public class PageUserDeviceInfoReq {

    private Integer pageNumber;

    private Integer pageSize;

    @NotNull(message = "tenantId.notnull")
    private Long tenantId;

    private Long orgId;

    private Long userId;

    //sub 子设备 root
    private String userType;


}
