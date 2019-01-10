package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 *
 * @author CHQ
 * @since 2018-05-10
 */

public class ConfigurationRsp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
     * 主键id
     */  
    private Long id;
    /**
     * 参数名称
     */
    private String param;
    /**
     * 参数内容
     */
    private String value;
    /**
     * 租户id
     */
    private Long tenantId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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

    public String toString() {
        return "Configuration{" +
        ", id=" + id +
        ", param=" + param +
        ", value=" + value +
        ", tenantId=" + tenantId +
        "}";
    }
}
