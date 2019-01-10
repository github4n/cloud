package com.iot.building.ifttt.vo.req;

import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.RelationVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.TriggerVo;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：规则保存请求类
 * 创建人： huangxu
 * 创建时间： 2018/10/12
 */
public class SaveIftttReq implements Serializable {

    private String clientId;

    private Long appletId;

    private Long deployMentId;
    
    private Integer shortcut;
    
    public Integer getShortcut() {
		return shortcut;
	}

	public void setShortcut(Integer shortcut) {
		this.shortcut = shortcut;
	}

	public Long getDeployMentId() {
        return deployMentId;
    }

    public void setDeployMentId(Long deployMentId) {
        this.deployMentId = deployMentId;
    }

    public Long getAppletId() {
        return appletId;
    }

    public void setAppletId(Long appletId) {
        this.appletId = appletId;
    }

    private Long orgId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    private Long id;
    private String name;
    private String icon;
    private String type; //场景类型 0=dev 1=timer
    private Byte status; //启用状态 0=stop 1=running
    private Byte isMulti; //是否跨网关 0=否 1=是
    private Long locationId;
    private Long spaceId;
    private Long tenantId;
    private Long userId;
    private String directId;
    private Long productId; //产品主键
    private Byte templateFlag; //模板标识 0否 1是
    private Byte ruleType; //规则类型 0=普通 1=安防
    private String securityType;
    private Integer delay; // 安防联动延时生效时间,单位秒
    private String iftttType;  // IFTTT类型 _2B  _2C

    private List<SensorVo> sensors; //规则包含的触发器
    private List<RelationVo> relations; //规则包含的条件关联
    private List<TriggerVo> triggers; //规则包含的触发条
    private List<ActuatorVo> actuators; //规则包含的执行器

    private List<SensorVo> sensorSingle;
    private List<ActuatorVo> actuatorSingle;

    public List<SensorVo> getSensorSingle() {
        return sensorSingle;
    }

    public void setSensorSingle(List<SensorVo> sensorSingle) {
        this.sensorSingle = sensorSingle;
    }

    public List<ActuatorVo> getActuatorSingle() {
        return actuatorSingle;
    }

    public void setActuatorSingle(List<ActuatorVo> actuatorSingle) {
        this.actuatorSingle = actuatorSingle;
    }

    private String uploadStatus;//是否下发至网关，0.不是,跨网关 1.是，在同一个网关

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    private boolean isSingleGatewayFlag;

    private Long templateId;

    private String sensorDeviceId;

    private String actuatorDeviceId;

    private List<String> deviceIds;

    private List<Long> sceneIds;

    public boolean isSingleGatewayFlag() {
        return isSingleGatewayFlag;
    }

    public void setSingleGatewayFlag(boolean singleGatewayFlag) {
        isSingleGatewayFlag = singleGatewayFlag;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public List<Long> getSceneIds() {
        return sceneIds;
    }

    public void setSceneIds(List<Long> sceneIds) {
        this.sceneIds = sceneIds;
    }

    public String getSensorDeviceId() {
        return sensorDeviceId;
    }

    public void setSensorDeviceId(String sensorDeviceId) {
        this.sensorDeviceId = sensorDeviceId;
    }

    public String getActuatorDeviceId() {
        return actuatorDeviceId;
    }

    public void setActuatorDeviceId(String actuatorDeviceId) {
        this.actuatorDeviceId = actuatorDeviceId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Byte getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(Byte templateFlag) {
        this.templateFlag = templateFlag;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Byte isMulti) {
        this.isMulti = isMulti;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public List<SensorVo> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorVo> sensors) {
        this.sensors = sensors;
    }

    public List<RelationVo> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationVo> relations) {
        this.relations = relations;
    }

    public List<TriggerVo> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerVo> triggers) {
        this.triggers = triggers;
    }

    public List<ActuatorVo> getActuators() {
        return actuators;
    }

    public void setActuators(List<ActuatorVo> actuators) {
        this.actuators = actuators;
    }

    public Byte getRuleType() {
        return ruleType;
    }

    public void setRuleType(Byte ruleType) {
        this.ruleType = ruleType;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getIftttType() {
		return iftttType;
	}

	public void setIftttType(String iftttType) {
		this.iftttType = iftttType;
	}

	@Override
    public String toString() {
        return "SaveIftttReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", isMulti=" + isMulti +
                ", locationId=" + locationId +
                ", spaceId=" + spaceId +
                ", tenantId=" + tenantId +
                ", userId=" + userId +
                ", directId='" + directId + '\'' +
                ", productId=" + productId +
                ", templateFlag=" + templateFlag +
                ", ruleType=" + ruleType +
                ", securityType='" + securityType + '\'' +
                ", sensors=" + sensors +
                ", relations=" + relations +
                ", triggers=" + triggers +
                ", actuators=" + actuators +
                ", templateId=" + templateId +
                ", delay=" + delay +
                '}';
    }
}
