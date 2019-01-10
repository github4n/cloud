package com.iot.control.activity.domain;

import com.iot.common.annotation.AutoMongoId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "活动日志", description = "设备、情景、联动活动日志")
@Document(collection = "activity_record")
public class ActivityRecord implements Serializable {

    private static final long serialVersionUID = -4120697549382646533L;

    @Id
    @AutoMongoId
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

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("组织机构id")
    private Long orgId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("结果 0=正常 1=异常")
    private Integer result;

    @ApiModelProperty("模板设置的执行时间")
    private Date setTime;

    private Long userId;

    private String userName;

    @ApiModelProperty("区域")
    private Long locationId;

    private String templateName;
    
    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
