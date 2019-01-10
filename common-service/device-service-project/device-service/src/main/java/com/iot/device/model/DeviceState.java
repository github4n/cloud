package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.iot.common.annotation.AutoMongoId;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@TableName("device_state")
@Document(collection = "device_state")
public class DeviceState extends Model<DeviceState> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_state";

	private static final long serialVersionUID = 1L;
	@Id
	@AutoMongoId
	private Long id;
	/**
     * 设备属性描述
	 * property_desc
     */
	private String propertyDesc;
    /**
	 * 设备id extends device table  uuid
     */
	private String deviceId;
    /**
     * 属性名称
	 * property_name
     */
	private String propertyName;
    /**
     * 属性值
	 * property_value
     */
	private String propertyValue;
    /**
     * 产品id
	 * product_id
     */
	private Long productId;
    /**
     * 上报日期
	 * log_date
     */
	private Date logDate;
	/**
	 *group_id
	 */
	private Long groupId;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}


}
