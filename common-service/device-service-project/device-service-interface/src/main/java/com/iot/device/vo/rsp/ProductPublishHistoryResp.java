package com.iot.device.vo.rsp;

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
public class ProductPublishHistoryResp implements Serializable {

	/***/
	private static final long serialVersionUID = 7695733581161315233L;

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户主键
     */
    private Long tenantId;

    /**
	 * 产品发布名称
	 */
	private String productName;
	
    /**
     * 产品ID
     */
	private Long productId;
	
	/**
	 * 产品发布时间
	 */
	private String publishTime;
	
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
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 创建人
     */
    private Long updateBy;
    
    /**
     * 创建时间
     */
    private String updateTime;

	/**
	 * 数据有效性
	 */
    private String isDeleted;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
}
