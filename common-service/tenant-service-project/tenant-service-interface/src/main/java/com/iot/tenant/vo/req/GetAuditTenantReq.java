package com.iot.tenant.vo.req;

/**
 * 描述：获取租户信息请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/23 13:55
 */
public class GetAuditTenantReq {
	
    private Integer pageNum;
    
    private Integer pageSize;
    
    /**
     * 审核状态
     */
    private Integer auditStatus;


    private Long tenantId;

    /**
     * 联系人
     */
    private String contacts;
    /**
     * 企业名
     */
    private String tenantName;

    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

}
