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
@TableName("data_point_style")
public class DataPointStyle extends Model<DataPointStyle> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="data_point_style";

	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * 功能点id
	 * data_point_id
	 */
	private Long dataPointId;
	/**
	 * 样式模板id
	 * stype_template_id
	 */
	private Long styleTemplateId;
    /**
     * 功能点名称
     */
	private String name;
    /**
     * 模式
     */
	private Integer mode;
    /**
     * 图标
     */
	private String img;
    /**
     * 背景
     */
	private String backgroud;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
