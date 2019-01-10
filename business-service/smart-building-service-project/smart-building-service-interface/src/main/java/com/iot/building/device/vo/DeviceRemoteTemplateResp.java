package com.iot.building.device.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
public class DeviceRemoteTemplateResp {
    private Long id;//
    private Long tenantId;//'租户ID',
    private String type;//
    private String name;// '遥控器模板名称',
    private Long businessTypeId;//
    private String createBy;// '创建人',
    private String updateBy;// '修改人',
    private List<DeviceRemoteControlTemplateResp> deviceRemoteControlTemplateReqs=new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getBusinessTypeId() {
		return businessTypeId;
	}
	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public List<DeviceRemoteControlTemplateResp> getDeviceRemoteControlTemplateReqs() {
		return deviceRemoteControlTemplateReqs;
	}
	public void setDeviceRemoteControlTemplateReqs(List<DeviceRemoteControlTemplateResp> deviceRemoteControlTemplateReqs) {
		this.deviceRemoteControlTemplateReqs = deviceRemoteControlTemplateReqs;
	}

}
