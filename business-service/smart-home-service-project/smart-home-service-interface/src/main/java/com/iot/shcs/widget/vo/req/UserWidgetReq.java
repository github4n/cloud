package com.iot.shcs.widget.vo.req;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/4 8:46
 * 修改人:
 * 修改时间：
 */
public class UserWidgetReq implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;

    private Long userId;

    // 组件类型(security/device/scene)
    private String type;

    // 记录值(安防规则类型/设备id/情景id)
    private String value;

    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
