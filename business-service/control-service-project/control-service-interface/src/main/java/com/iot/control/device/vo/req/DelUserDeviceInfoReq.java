package com.iot.control.device.vo.req;

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
 * @Date: 9:10 2018/12/19
 * @Modify by:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DelUserDeviceInfoReq {
    @NotNull(message = "tenantId.notnull")
    private Long tenantId;
    @NotNull(message = "userId.notnull")
    private Long userId;
    @NotNull(message = "subDeviceIds.notnull")
    private List<String> subDeviceIds;
}
