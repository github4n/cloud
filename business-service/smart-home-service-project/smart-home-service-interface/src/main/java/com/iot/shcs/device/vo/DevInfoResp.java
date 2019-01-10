package com.iot.shcs.device.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DevInfoResp implements Serializable {

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
     * 设备图标
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

    private Object ota;

    private String version;

    /**
     * 分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位
     */
    private Integer fwType;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 升级阶段
     */
    private Integer stage;

    /**
     * 升级进度
     */
    private Integer percent;
    /**
     * 设备操作地址
     */
    private Long address;
    /**
     * 协议类型,对应product中的config_net_mode
     */
    private String comMode;
    /**
     * 遥控器分组id，当设备是遥控器时，为0-255的值，其他设备为0
     */
    private List<Long> remoteGroudId;

}
