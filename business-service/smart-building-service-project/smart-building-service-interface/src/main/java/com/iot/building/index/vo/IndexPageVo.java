package com.iot.building.index.vo;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;

public class IndexPageVo {
	
	private Pagination pagination;
	
	private IndexReq indexReq;
	
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	public IndexReq getIndexReq() {
		return indexReq;
	}
	public void setIndexReq(IndexReq indexReq) {
		this.indexReq = indexReq;
	}
}
