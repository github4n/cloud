package com.iot.shcs.device.entity;

import lombok.Data;

import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：wucheng
 * 创建时间 2018/12/6 18:05
 */
@Data
public class DeviceStatus {

    private Long id;
    /**
     * 设备id extends device table uuid
     */
    private String deviceId;
    /**
     * 开关，1：开启；0：关闭
     * on_off
     */
    private Integer onOff;
    /**
     * 激活状态（0-未激活，1-已激活）
     * active_status
     */
    private Integer activeStatus;
    /**
     * 激活时间
     * active_time
     */
    private Date activeTime;
    /**
     * 在线状态
     * online_status
     */
    private String onlineStatus;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    /**
     * 最后登入时间
     */
    private Date lastLoginTime;
}
