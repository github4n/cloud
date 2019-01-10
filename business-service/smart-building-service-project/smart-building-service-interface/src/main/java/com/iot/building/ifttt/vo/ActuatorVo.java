package com.iot.building.ifttt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "执行动作", description = "联动要执行的动作，如：控制设备、发邮件、发短信等")
public class ActuatorVo implements Serializable {

    private static final long serialVersionUID = 5998682825135331175L;
    public ActuatorVo(String name, String deviceId, String type, Long productId,String properties) {
        this.name = name;
        this.deviceId = deviceId;
        this.type = type;
        this.productId = productId;
        this.properties = properties;
    }

    public ActuatorVo() {
        super();
    }
    @ApiModelProperty("主键id，保存时不用传")
    private Long id;
    @ApiModelProperty("执行动作的名称")
    private String name;
    @ApiModelProperty("执行动作图标的标识")
    private String label;
    @ApiModelProperty("执行动作所需的属性信息，用json串表示")
    private String properties;
    @ApiModelProperty("关联设备id")
    private String deviceId;
    @ApiModelProperty("关联ifttt规则id")
    private Long ruleId;
    @ApiModelProperty("执行动作图标的x,y位置")
    private Long[] position;
    @ApiModelProperty("执行动作的类型，如：Email、sms、hubLight等")
    private String type;
    private Integer idx;
    private Integer delay;
    private Long productId; //产品主键
    // 外部对象id,视type内容而定
    private String objectId;
    private Long tenantId;
    private Long orgId;

    private Long sceneId;
    
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
