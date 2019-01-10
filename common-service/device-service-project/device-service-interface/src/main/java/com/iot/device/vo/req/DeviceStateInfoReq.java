package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
public class DeviceStateInfoReq implements Serializable {

    /***/
    private static final long serialVersionUID = 5694232069085088357L;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 属性描述
     */
    private String propertyDesc;

    /**
     * 设备属性名称
     */
    private String propertyName;

    /**
     * 设备属性值
     */
    private String propertyValue;

    /**
     * 状态上报时间
     */
    private Date logDate;

    private Long groupId;

    /**
     * 租户ID
     */
    private Long tenantId;
}
