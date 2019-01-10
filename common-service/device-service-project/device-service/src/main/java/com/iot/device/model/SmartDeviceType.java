package com.iot.device.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("smart_device_type")
public class SmartDeviceType {

	public static final String TABLE_NAME="smart_device_type";

	private Long id;

	private Long deviceTypeId;

	private String smartType;

	private Integer smart;

	private Long createBy;

	private Date createTime;

	private Long updateBy;

	private Date updateTime;
	/**
	 * 租户ID
	 */
	private Long tenantId;
}
