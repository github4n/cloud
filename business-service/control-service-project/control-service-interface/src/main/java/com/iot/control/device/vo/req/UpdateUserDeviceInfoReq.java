package com.iot.control.device.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: lucky
 * @Descrpiton: 添加用户信息
 * @Date: 11:44 2018/10/12
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class UpdateUserDeviceInfoReq {

    public static final String SUB_DEVICE = "sub";

    public static final String ROOT_DEVICE = "root";

    private Long id;
    /**
     * 租户id
     * tenant_id
     */
    @NotNull(message = "tenantId.notnull")
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
    @NotNull(message = "userId.notnull")
    private Long userId;
    /**
     * 设备id  关联 device  的 uuid
     */
    @NotNull(message = "deviceId.notnull")
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
}
