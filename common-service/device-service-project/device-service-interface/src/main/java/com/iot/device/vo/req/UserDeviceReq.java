package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:39 2018/4/24
 * @Modify by:
 */
@Data
@ToString
public class UserDeviceReq implements Serializable {

    /**
     * 用户id
     * user_id
     */
    private Long userId;
    /**
     * 设备id  关联 device  的 id
     * device_id
     */
    private String deviceId;
    /**
     * 用户类型（root：主账号 sub: 子账号）
     * user_type
     */
    private String userType;
    /**
     * 用户访问设备秘钥
     */
    private String password;
    /**
     * 事件通知使能（0：开启，1：关闭）
     * event_notify_enabled
     */
    private Integer eventNotifyEnabled;
    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
    /**
     * 租户id
     * tenant_id
     */
    private Long tenantId;
    /**
     * 组织id
     * org_id
     */
    private Long orgId;

}
