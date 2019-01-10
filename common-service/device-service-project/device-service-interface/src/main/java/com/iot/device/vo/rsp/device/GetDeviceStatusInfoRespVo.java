package com.iot.device.vo.rsp.device;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:03 2018/10/10
 * @Modify by:
 */
@Data
@ToString
public class GetDeviceStatusInfoRespVo {
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

    private String token;//toB 需要使用
}
