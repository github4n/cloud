package com.iot.control.space.vo;

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
 * @Date: 14:46 2018/12/19
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DelSpaceDeviceReq {

    @NotNull(message = "tenantId.notnull")
    private Long tenantId;

    @NotNull(message = "device.notnull")
    private List<String> deviceIds;
}
