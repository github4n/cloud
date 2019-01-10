package com.iot.building.space.vo;

import java.io.Serializable;
import java.util.Map;


public class SpaceExcelReq implements Serializable{

    private Long locationId;

    private Long userId;

    private Long tenantId;
    
    private Long orgId;

    private Map<String, Object> result;

    public SpaceExcelReq(Long locationId, Long userId, Long tenantId,Long orgId, Map<String, Object> result) {
        this.locationId = locationId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.result = result;
        this.orgId = orgId;
    }
    
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public SpaceExcelReq() {
        super();
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
