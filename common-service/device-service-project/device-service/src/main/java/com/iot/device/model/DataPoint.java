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
@TableName("data_point")
public class DataPoint extends Model<DataPoint> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="data_point";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 显示名称
	 * label_name
     */
	private String labelName;
    /**
     * 属性标识
	 * property_code
     */
	private String propertyCode;
    /**
     * 读写类型(0 r, 1 w, 2 rw)
     */
	private Integer mode;
    /**
     * 数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）
	 * data_type
     */
	private Integer dataType;
    /**
     * 图标名称
	 * icon_name
     */
	private String iconName;
    /**
     * 数值属性
     */
	private String property;
    /**
     * 描述
     */
	private String description;
	/**
	 * 是否自定义功能点 1是  0否
	 */
	private Integer isCustom;

	private Long createBy;

	private Date createTime;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}


}
