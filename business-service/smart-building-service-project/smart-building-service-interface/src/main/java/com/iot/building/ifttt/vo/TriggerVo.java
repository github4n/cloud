package com.iot.building.ifttt.vo;

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

	//重构后添加的
	@ApiModelProperty("appletId")
	private Long appletId;
    @ApiModelProperty("sensor画布上图标的x,y位置")
    private String sensorPosition;
    @ApiModelProperty("sensor的类型")
    private String sensorType;
    @ApiModelProperty("sensor的属性")
    private String sensorProperties;
	@ApiModelProperty("sensor设备的id")
	private String sensorDeviceId;


    @ApiModelProperty("actuctor画布上图标的x,y位置")
    private String actuctorPosition;
    @ApiModelProperty("actuctor的类型")
    private String actuctorType;
    @ApiModelProperty("actuctor的属性")
    private String actuctorProperties;
	@ApiModelProperty("actuctor设备的id")
	private String actuctorDeviceId;
	@ApiModelProperty("tenantId")
	private Long tenantId;

	private Long[] position;//or and的位置

	public Long[] getPosition() {
		return position;
	}

	public void setPosition(Long[] position) {
		this.position = position;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getSensorDeviceId() {
		return sensorDeviceId;
	}

	public void setSensorDeviceId(String sensorDeviceId) {
		this.sensorDeviceId = sensorDeviceId;
	}

	public String getActuctorDeviceId() {
		return actuctorDeviceId;
	}

	public void setActuctorDeviceId(String actuctorDeviceId) {
		this.actuctorDeviceId = actuctorDeviceId;
	}

	public String getSensorProperties() {
		return sensorProperties;
	}

	public void setSensorProperties(String sensorProperties) {
		this.sensorProperties = sensorProperties;
	}

	public String getActuctorProperties() {
		return actuctorProperties;
	}

	public void setActuctorProperties(String actuctorProperties) {
		this.actuctorProperties = actuctorProperties;
	}

	public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

	public String getSensorPosition() {
		return sensorPosition;
	}

	public void setSensorPosition(String sensorPosition) {
		this.sensorPosition = sensorPosition;
	}

	public String getActuctorPosition() {
		return actuctorPosition;
	}

	public void setActuctorPosition(String actuctorPosition) {
		this.actuctorPosition = actuctorPosition;
	}

	public String getActuctorType() {
        return actuctorType;
    }

    public void setActuctorType(String actuctorType) {
        this.actuctorType = actuctorType;
    }

    public Long getAppletId() {
		return appletId;
	}

	public void setAppletId(Long appletId) {
		this.appletId = appletId;
	}

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
