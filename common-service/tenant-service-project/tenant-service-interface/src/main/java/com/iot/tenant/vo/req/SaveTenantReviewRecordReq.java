package com.iot.tenant.vo.req;

/**
 * 描述：保存多语言配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 11:17
 */
public class SaveTenantReviewRecordReq {
	
	/**
     * 租户主键
     */
    private Long tenantId;
    
    /**
     * 0:未审核 1:审核未通过 2:审核通过
     */
    private Integer processStatus;
    
    /**
     * 操作描述（提交审核，审核通过，审核不通过原因） 
     */
    private String operateDesc;
    
    /**
     * 创建人
     */
    private Long createBy;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
    
}
