package com.iot.device.model;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *    设备分类
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("device_catalog")
public class DeviceCatalog extends Model<DeviceCatalog> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_catalog";

	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * 父级id
	 */
	private Long parentId;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 描述
     */
	private String description;

	private Long tenantId;

	private Date createTime;

	private Date updateTime;

	private Long createBy;

	private Long updateBy;

	private Integer isDeleted;

	private Integer order;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
