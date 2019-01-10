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
@TableName("product_data_point")
public class ProductDataPoint extends Model<ProductDataPoint> implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="product_data_point";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 功能数据点id
	 * data_point_id
     */
	private Long dataPointId;
    /**
     * 产品id
	 * product_id
     */
	private Long productId;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 *update_time
	 */
	private Date updateTime;
	/**
	 *create_by
	 */
	private Long createBy;
	/**
	 *update_by
	 */
	private Long updateBy;
	/**
	 *is_deleted
	 */
	private Integer isDeleted;

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
