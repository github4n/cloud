package com.iot.building.ifttt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "触发器", description = "规则包含的触发器，如：传感器、天气、定时等")
public class SensorVo implements Serializable {

	private static final long serialVersionUID = 1397663022233080560L;

	public SensorVo(String name, String deviceId, String type, Long productId,String properties) {
		this.name = name;
		this.deviceId = deviceId;
		this.type = type;
		this.productId = productId;
		this.properties = properties;
	}

	public SensorVo() {
		super();
	}


	@ApiModelProperty("主键id，保存时不用传")
	private Long id;
	@ApiModelProperty("触发器名称")
	private String name;
	@ApiModelProperty("触发器显示标识")
	private String label;
	@ApiModelProperty("触发器属性信息，用json串表示")
	private String properties;
	@ApiModelProperty("触发器状态，字符串表示")
	private String status;
	@ApiModelProperty("关联设备id")
	private String deviceId;
	@ApiModelProperty("关联ifttt规则id")
	private Long ruleId;
	@ApiModelProperty("在画布中的x,y坐标")
	private Long[] position;
	@ApiModelProperty("触发器类型，如：temperatureSensor")
	private String type;
	private String timing;

	private Integer idx;

	private Integer delay;

	private Long productId; //产品主键

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}
}
