package com.iot.building.ifttt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "条件关联信息", description = "多个触发器之间的关联关系，如：AND/OR")
public class RelationVo implements Serializable {

	private static final long serialVersionUID = -5968343262267789409L;

	@ApiModelProperty("主键id，保存时不用传")
	private Long id;
	@ApiModelProperty("条件关联显示标识")
	private String label;
	@ApiModelProperty(value = "条件关联类型，AND/OR", allowableValues = "AND,OR")
	private String type;
	@ApiModelProperty("条件关联的触发器标识，多个用逗号间隔")
	private String[] parentLabels;
	@ApiModelProperty("关联的各个触发器的判断条件，多个用逗号间隔")
	private String combinations;
	@ApiModelProperty("关联ifttt规则的id")
	private Long ruleId;
	@ApiModelProperty("画布上图标的x,y位置")
	private Long[] position;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getParentLabels() {
		return parentLabels;
	}

	public void setParentLabels(String[] parentLabels) {
		this.parentLabels = parentLabels;
	}

	public String getCombinations() {
		return combinations;
	}

	public void setCombinations(String combinations) {
		this.combinations = combinations;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long[] getPosition() {
		return position;
	}

	public void setPosition(Long[] position) {
		this.position = position;
	}

}
