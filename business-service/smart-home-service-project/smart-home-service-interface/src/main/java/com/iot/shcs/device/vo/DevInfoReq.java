package com.iot.shcs.device.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DevInfoReq implements Serializable {

    /**
     * 设备id
     */
    private String devId;
    /**
     * 是否app子设备
     */
    private Integer isAppDev;

    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备名称
     */
    private String icon;

    private String homeId;

    private String roomId;

    private Long tenantId;
    /**
     * 设备model
     */
    private String devModel;

    private String hwVersion;

    private String mac;
    /**
     * 设备操作地址
     */
    private Long address;
    /**
     * 协议类型,对应product中的config_net_mode
     */
    private String comMode;

    private Long userId;
    /**
     * 遥控器分组id，当设备是遥控器时，为0-255的值，其他设备为0
     */
    private List<Long> remoteGroudId;

}
