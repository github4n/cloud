package com.iot.boss.vo.tenant.req;

/**
 * 描述：保存多语言配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 11:17
 */
public class TenantAuditRecordReq {
	
	/**
     * 租户主键
     */
    private Long tenantId;
    
    /**
     * 操作描述（提交审核，审核通过，审核不通过原因） 
     */
    private String operateDesc;
    
    /**
     * 创建人
     */
    private Long createBy;
    
    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 租户唯一code
     */
    private String code;
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
    
}
