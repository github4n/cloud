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
@TableName("device_type_data_point")
public class DeviceTypeDataPoint extends Model<DeviceTypeDataPoint> implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_type_data_point";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 功能点id
	 * data_point_id
     */
	private Long dataPointId;
    /**
     * 设备类型id
	 * device_type_id
     */
	private Long deviceTypeId;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * update_time
	 */
	private Date updateTime;
	/**
	 * create_by
	 */
	private Long createBy;
	/**
	 * update_by
	 */
	private Long updateBy;
	/**
	 * is_deleted
	 */
	private Integer isDeleted;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}


}
