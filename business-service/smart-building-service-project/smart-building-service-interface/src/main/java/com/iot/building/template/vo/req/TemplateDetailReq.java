package com.iot.building.template.vo.req;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：情景
 * 功能描述：
 * 创建人： wujianlong
 * 创建时间：2017年11月15日 下午2:44:14
 * 修改人： wujianlong
 * 修改时间：2017年11月15日 下午2:44:14
 */
public class TemplateDetailReq implements Serializable{
	
	private static final long serialVersionUID = 840590056656341922L;

	private Long id;

	/** 模板id*/
	private Long templateId;

	/** 模板名*/
	private String name;

	/** 产品表id*/
	private Long productId;

	/** 产品类型目标值json*/
	private String deviceTarValueTo;

	/** 设备分类id*/
	private Long deviceCategoryId;

	/** 设备类型id*/
	private Long deviceTypeId;

	/** 目标值json格式*/
	private String targetValue;
	
	/** 租户ID*/
	private Long tenantId;
	
	/** 业务类型ID*/
	private Long businessTypeId;
	
	/** 业务类型*/
	private String businessType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(Long deviceCategoryId) {
		this.deviceCategoryId = deviceCategoryId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceTarValueTo() {
		return deviceTarValueTo;
	}

	public void setDeviceTarValueTo(String deviceTarValueTo) {
		this.deviceTarValueTo = deviceTarValueTo;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
