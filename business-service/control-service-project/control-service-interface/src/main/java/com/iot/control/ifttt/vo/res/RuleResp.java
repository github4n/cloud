package com.iot.control.ifttt.vo.res;

import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.RelationVo;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.TriggerVo;

import java.util.Date;
import java.util.List;

/**
 * 描述：规则应答对象
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/23 16:15
 */
public class RuleResp {
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
    private Date createTime;
    private Long productId; //产品主键
    private Byte templateFlag; //模板标识 0否 1是
    private Byte ruleType;
    private String securityType;
    private Integer delay;
    private String executeTime; //执行时间
    private String executeDate; //执行日期
    private String iftttType;  // IFTTT类型 _2B  _2C
    private Long templateId;

    private List<SensorVo> sensors; //规则包含的触发器
    private List<RelationVo> relations; //规则包含的条件关联
    private List<TriggerVo> triggers; //规则包含的触发条
    private List<ActuatorVo> actuators; //规则包含的执行器

    private String userName;

    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

	public String getIftttType() {
		return iftttType;
	}

	public void setIftttType(String iftttType) {
		this.iftttType = iftttType;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

    @Override
    public String toString() {
        return "RuleResp{" +
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
                ", createTime=" + createTime +
                ", productId=" + productId +
                ", templateFlag=" + templateFlag +
                ", ruleType=" + ruleType +
                ", securityType='" + securityType + '\'' +
                ", delay=" + delay +
                ", executeTime='" + executeTime + '\'' +
                ", executeDate='" + executeDate + '\'' +
                ", iftttType='" + iftttType + '\'' +
                ", templateId=" + templateId +
                ", sensors=" + sensors +
                ", relations=" + relations +
                ", triggers=" + triggers +
                ", actuators=" + actuators +
                ", userName='" + userName + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
