package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
@TableName("device_type_style_template")
public class DeviceTypeStyleTemplate extends Model<DeviceTypeStyleTemplate> implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_type_style_template";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 设备类型id
	 * device_type_id
     */
	private Long deviceTypeId;
    /**
     * 样式模板id
	 * style_template_id
     */
	private Long styleTemplateId;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}


}
