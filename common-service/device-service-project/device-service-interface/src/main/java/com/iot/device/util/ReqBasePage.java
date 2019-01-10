package com.iot.device.util;

import java.io.Serializable;

public class ReqBasePage extends BaseRequest implements Serializable{

	private String searchValues;

	private String sortProperties;

	private String sortOrder;//asc desc

	/**
	 * //边界值id  用于瀑布流刷新用   根据不用业务呈现什么  可以为空
	 */
	private String boundId;


	public String getBoundId() {
		return boundId;
	}

	public void setBoundId(String boundId) {
		this.boundId = boundId;
	}

	public String getSearchValues() {
		return searchValues;
	}

	public void setSearchValues(String searchValues) {
		this.searchValues = searchValues;
	}


	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortProperties() {
		return sortProperties;
	}

	public void setSortProperties(String sortProperties) {
		this.sortProperties = sortProperties;
	}

	@Override
	public String toString() {
		return "ReqBasePage{" +
				"searchValues='" + searchValues + '\'' +
				", pageNum=" + getPageNum() +
				", pageSize=" + getPageSize() +
				", sortProperties='" + sortProperties + '\'' +
				", sortOrder='" + sortOrder + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ReqBasePage that = (ReqBasePage) o;

		if (searchValues != null ? !searchValues.equals(that.searchValues) : that.searchValues != null) return false;
		if (sortProperties != null ? !sortProperties.equals(that.sortProperties) : that.sortProperties != null)
			return false;
		if (sortOrder != null ? !sortOrder.equals(that.sortOrder) : that.sortOrder != null) return false;
		return boundId != null ? boundId.equals(that.boundId) : that.boundId == null;

	}

	@Override
	public int hashCode() {
		int result = searchValues != null ? searchValues.hashCode() : 0;
		result = 31 * result + (sortProperties != null ? sortProperties.hashCode() : 0);
		result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
		result = 31 * result + (boundId != null ? boundId.hashCode() : 0);
		return result;
	}
}