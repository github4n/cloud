package com.iot.building.device.vo;

import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
public class DeviceRemoteTemplateSimpleResp {
    private Long id;//
    private String name;// '遥控器模板名称',
    private String businessTypeName;//
    private Date createTime;// '创建时间',

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

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
