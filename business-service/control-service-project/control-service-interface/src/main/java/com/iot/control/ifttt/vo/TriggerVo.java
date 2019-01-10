package com.iot.control.ifttt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "触发流程", description = "执行动作前的触发流程")
public class TriggerVo implements Serializable {

	private static final long serialVersionUID = 6020550306711956508L;

	@ApiModelProperty("主键id，保存时不用传")
	private Long id;
	@ApiModelProperty("线条id")
	private String lineId;
	@ApiModelProperty("触发流程的源标识")
	private String sourceLabel;
	@ApiModelProperty("触发流程的源标识id")
	private Long start;
	@ApiModelProperty("触发流程的目的标识id")
	private Long end;
	@ApiModelProperty("触发流程的目的标识")
	private String destinationLabel;
	@ApiModelProperty("流程动作的调用频率")
	private Integer invocationPolicy;
	@ApiModelProperty("触发流程的判断状态，多个用逗号间隔")
	private String statusTrigger;
	@ApiModelProperty("关联的ifttt规则id")
	private Long ruleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getSourceLabel() {
		return sourceLabel;
	}

	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public String getDestinationLabel() {
		return destinationLabel;
	}

	public void setDestinationLabel(String destinationLabel) {
		this.destinationLabel = destinationLabel;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public Integer getInvocationPolicy() {
		return invocationPolicy;
	}

	public void setInvocationPolicy(Integer invocationPolicy) {
		this.invocationPolicy = invocationPolicy;
	}

	public String getStatusTrigger() {
		return statusTrigger;
	}

	public void setStatusTrigger(String statusTrigger) {
		this.statusTrigger = statusTrigger;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
}
