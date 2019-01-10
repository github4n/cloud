package com.iot.boss.vo.tenant.req;

public class TenantAuditReq {
	
    private Integer pageNum;
    
    private Integer pageSize;
    
    /**
     * 审核状态
     */
    private Integer auditStatus;

	/**
	 * 联系人
	 */
	private String contacts;
	/**
	 * 企业名
	 */
	private String tenantName;

	/**
	 * 账号
	 */
    private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}



}
