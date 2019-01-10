package com.iot.control.activity.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "活动日志", description = "设备、情景、联动活动日志")
public class ActivityRecordResp implements Serializable {

    private static final long serialVersionUID = -4120697549382646533L;

    @ApiModelProperty("主键id，保存时不用传")
    private Long id;

    @ApiModelProperty(value = "日志类型，区分是设备、情景还是联动", allowableValues = "IFTTT,SCENE,DEVICE,ALL,SINGLE")
    private String type;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("活动日志描述")
    private String activity;

    @ApiModelProperty("日志时间")
    private Long time;

    @ApiModelProperty("创建人，可不传")
    private Long createBy;

    @ApiModelProperty("外部关联主键ID")
    private String foreignId;

    @ApiModelProperty("假删除标识")
    private Integer delFlag;

    private String deviceName; //设备名称

    private Integer result; //执行结果 0:正常 1:异常

    private Date setTime;   // 模板设置的执行时间

    private Long userId;

    private String userName;

    private String tenantId;

    private String orgId;

    private Long templateId;
    private String templateName;
    private String templateType;
    private String properties;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
