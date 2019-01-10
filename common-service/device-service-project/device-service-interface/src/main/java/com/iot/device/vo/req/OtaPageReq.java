package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: linjihuang
 * @Descrpiton:
 * @Date: 13:51 2018/9/12
 * @Modify by:
 */
public class OtaPageReq implements Serializable {

    private Integer pageNum =1;

    private Integer pageSize =10;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    private Long productId;
    private Long locationId;
    private Long orgId;

    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
