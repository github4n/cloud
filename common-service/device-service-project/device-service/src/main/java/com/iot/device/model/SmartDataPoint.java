package com.iot.device.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("smart_data_point")
public class SmartDataPoint {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="smart_data_point";


	private Long id;

	private Long dataPointId;

	private String propertyCode;

	private String smartCode;

	private Integer smart;

	private Long createBy;

	private Date createTime;

	private Long updateBy;

	private Date updateTime;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 模组-属性 id
	 */
	private Long propertyId;
}
