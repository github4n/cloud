package com.iot.device.vo.req;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
@ApiModel(description = "新增功能点请求")
public class DataPointReq extends SearchParam {

	/**
	 * 显示名称
	 * label_name
	 */
	@ApiModelProperty(name = "id", value = "功能点id")
	private Long id;

	@ApiModelProperty(name = "labelName", value = "显示名称")
	private String labelName;
	/**
	 * 属性标识
	 * property_code
	 */
	@ApiModelProperty(value = "属性标识", example = "OnOff", dataType = "String")
	private String propertyCode;
	/**
	 * 读写类型(0 r, 1 w, 2 rw)
	 */
	@ApiModelProperty(value = "读写类型(0 r, 1 w, 2 rw)", example = "0", dataType = "Integer")
	private Integer mode;
	/**
	 * 数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）
	 * data_type
	 */
	@ApiModelProperty(value = "数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）", dataType = "Integer")
	private Integer dataType;
	/**
	 * 图标名称
	 * icon_name
	 */
	@ApiModelProperty(value = "图标名称")
	private String iconName;
	/**
	 * 数值属性
	 */
	@ApiModelProperty(value = "数值属性", example = "{\"boolean\":\"0\"}", dataType = "String")
	private String property;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "是否自定义功能点")
	private Integer isCustom;

	private ArrayList<SmartWraper> smart;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	private Long createBy;

	@Data
	@ToString
	public static class SmartWraper {
		private Long id;
		private Integer smart;
		private String code;

	}
}
