package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 设备SN表
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("device_sn")
public class DeviceSn extends Model<DeviceSn> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_sn";

	private static final long serialVersionUID = 1L;
	private Long id;
	/**
     * 设备SN号
	 * device_sn
     */
	private String deviceSn;
    /**
     * 使用时间
	 * using_date
     */
	private Long usingDate;
	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}




}
