package com.iot.device.util;

import java.io.Serializable;

public class BaseRequest  implements Serializable {



	private int pageNum = 1;

	private int pageSize = 10;
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * 获取起始页的位置
	 *
	 * @return
	 */
	public int getPageStartIndex() {

		return (pageNum - 1) * this.pageSize;
	}

	public void setPageNum(int pageNum) {
		if (pageNum <= 0) {
			this.pageNum = 1;
		} else {
			this.pageNum = pageNum;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize <= 0) {
			this.pageSize = 10;
		} else {
			this.pageSize = pageSize;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BaseRequest that = (BaseRequest) o;

		if (pageNum != that.pageNum) return false;
		return pageSize == that.pageSize;

	}

	@Override
	public int hashCode() {
		int result = pageNum;
		result = 31 * result + pageSize;
		return result;
	}

	@Override
	public String toString() {
		return "BaseRequest{" +
				"pageNum=" + pageNum +
				", pageSize=" + pageSize +
				'}';
	}
}