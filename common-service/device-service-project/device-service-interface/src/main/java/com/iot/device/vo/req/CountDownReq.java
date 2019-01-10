package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CountDownReq implements Serializable{

    private String deviceId;

    private Long userId;

    private Long countdown;

    private Integer isEnable;
    /**
     * 用户所属组织id
     */
    private Long orgId;
    /**
     * 租户ID
     */
    private Long tenantId;

}
