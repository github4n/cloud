package com.iot.control.device.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户设备关系表---------*****目前业务 保证当前的表 device_id 唯一*****
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@TableName("user_device")
public class UserDevice extends Model<UserDevice> implements Serializable {
    /**
     * table_name
     */
    public static final String TABLE_NAME = "user_device";

    private static final long serialVersionUID = 1L;
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

    @Override
    protected Serializable pkVal() {
        return id;
    }


}
