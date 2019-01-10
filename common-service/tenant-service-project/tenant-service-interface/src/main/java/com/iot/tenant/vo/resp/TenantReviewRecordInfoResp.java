package com.iot.tenant.vo.resp;

import java.io.Serializable;
import java.util.Date;

public class TenantReviewRecordInfoResp implements Serializable {
	/***/
	private static final long serialVersionUID = 7865118724724172456L;

	/**
     * 操作时间（申请时间，审核时间）
     */
    private Date operateTime;

    /**
     * 0:未审核 1:审核未通过 2:审核通过
     */
    private Integer processStatus;
    
    /**
     * 操作描述（提交审核，审核通过，审核不通过原因） 
     */
    private String operateDesc;

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
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
    
    
}
