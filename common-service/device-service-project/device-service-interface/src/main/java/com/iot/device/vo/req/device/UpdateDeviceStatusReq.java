package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:19 2018/10/10
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class UpdateDeviceStatusReq {

    @NotBlank(message = "deviceId.notnull")
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
    @NotNull(message = "tenantId.notnull")
    private Long tenantId;

    /**
     * toB 有用
     */
    private String token;

    /**
     * 最后在线时间
     */
    private Date lastLoginTime;

}
