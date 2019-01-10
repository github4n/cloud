package com.iot.control.ifttt.vo.req;

/**
 * 描述：获取传感器请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/24 17:53
 */
public class GetSensorReq {
    private String type;
    private String properties;
    private Long spaceId;
    private Long tenantId;
    private Long id;
    private Byte status;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GetSensorReq{" +
                "type='" + type + '\'' +
                ", properties='" + properties + '\'' +
                ", spaceId=" + spaceId +
                ", tenantId=" + tenantId +
                ", id=" + id +
                '}';
    }
}
