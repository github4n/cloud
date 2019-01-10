package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
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
@TableName("style_template")
public class StyleTemplate extends Model<StyleTemplate> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 样式名称
	 */
	@TableField("name")
	private String name;
	/**
	 * 样式唯一标识code
	 */
	@TableField("code")
	private String code;
	/**
	 * 图片
	 */
	@TableField("img")
	private String img;
	/**
	 * 创建人
	 */
	@TableField("create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 修改人
	 */
	@TableField("update_by")
	private Long updateBy;
	/**
	 * 修改时间
	 */
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 描述
	 */
	@TableField("description")
	private String description;

	@TableField(exist = false)
	private Long otherId;
	/**
	 * 租户ID
	 */
	private Long tenantId;

	@TableField("resource_link")
	private String resourceLink;

	@TableField("resource_link_validation")
	private String resourceLinkValidation;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
