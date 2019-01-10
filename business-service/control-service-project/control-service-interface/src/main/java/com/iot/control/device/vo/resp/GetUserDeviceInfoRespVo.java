package com.iot.control.device.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:44 2018/10/12
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class GetUserDeviceInfoRespVo {

    private Long id;
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
    /**
     * 用户id
     * user_id
     */
    private Long userId;
    /**
     * 设备id  关联 device  的 uuid
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
}
