package com.iot.device.vo.req;

public class GetProductReq {

	private Integer pageNum = 1;
	private Integer pageSize = 10;
    private String name;

	/**
	 * 检索租户
	 */
	private Long tenantId;

	/**
	 * 检索类型
	 */
	private Long deviceTypeId;

	/**
	 * 是否套包产品
	 */
	private Integer isKit;
	/**
	 * 是否直连设备
	 */
	private Integer isDirectDevice;
	/**
	 * 检索分类
	 */
	private Long catalogId;

	/**
	 * 可以检索 产品名称 、model 是否为套包 是否为直连设备
	 */
	private String searchValues;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public Integer getIsKit() {
		return isKit;
	}

	public void setIsKit(Integer isKit) {
		this.isKit = isKit;
	}

	public Integer getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(Integer isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public String getSearchValues() {
		return searchValues;
	}

	public void setSearchValues(String searchValues) {
		this.searchValues = searchValues;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
}
