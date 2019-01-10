package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("device_status")
public class DeviceStatus extends Model<DeviceStatus> implements Serializable {

	public static final String CONNECTED = "connected";

	public static final String DIS_CONNECTED = "disconnected";
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_status";

	private static final long serialVersionUID = 1L;
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

	@Override
	protected Serializable pkVal() {
		return id;
	}

	private String token;

}
