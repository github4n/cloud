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
@TableName("product_style_template")
public class ProductStyleTemplate extends Model<ProductStyleTemplate> implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="product_style_template";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 产品id
	 * product_id
     */
	private Long productId;
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
