package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 李帅
 * 创建时间：2018年9月12日 下午6:04:42
 * 修改人：李帅
 * 修改时间：2018年9月12日 下午6:04:42
 */
public class ProductPublishHistoryReq implements Serializable {

	/***/
	private static final long serialVersionUID = 8251248850579415069L;

    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 产品ID
     */
	private Long productId;
	
	/**
	 * 产品发布状态
	 */
	private String publishStatus;
	
	/**
	 * 发布失败原因
	 */
	private String failureReason;
	
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

}
