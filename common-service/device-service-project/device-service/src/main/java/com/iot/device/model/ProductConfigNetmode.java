package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("product_config_netmode")
public class ProductConfigNetmode extends Model<ProductConfigNetmode> implements Serializable {


	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	private Long productId;
	
	private String name;

	private Long createBy;

	private Date createTime;

	private Long updateBy;

	private Date updateTime;


	@Override
	protected Serializable pkVal() {
		return id;
	}

}
