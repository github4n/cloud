package com.iot.building.device.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
public class DeviceRemoteTemplateReq {
    private Long id;//
    private Long tenantId;//'租户ID',
    private String name;// '遥控器模板名称',
    private String type;// '遥控器模板名称',
    private Long businessTypeId;//
    private String createBy;// '创建人',
    private String updateBy;// '修改人',
    private List<DeviceRemoteControlTemplateReq> deviceRemoteControlTemplateReqs=new ArrayList<>();

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

    public List<DeviceRemoteControlTemplateReq> getDeviceRemoteControlTemplateReqs() {
        return deviceRemoteControlTemplateReqs;
    }

    public void setDeviceRemoteControlTemplateReqs(List<DeviceRemoteControlTemplateReq> deviceRemoteControlTemplateReqs) {
        this.deviceRemoteControlTemplateReqs = deviceRemoteControlTemplateReqs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
