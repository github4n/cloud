package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class UpdateDeviceStateReq implements Serializable {

    private Long tenantId;

    private String deviceId;

    private List<AddCommDeviceStateInfoReq> stateList;

}
