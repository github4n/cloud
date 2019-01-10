package com.iot.device.vo.req;

public class CustUuidManageReq {

	/**
     * 批次号
     */
	private String batchNumId;
	
    /**
     * 客户代码
     */
	private String custCode;
	
    /**
     * 客户名称
     */
	private String custName;
	
    /**
     * uuid 类型
     */
	private String uuidType;

	/**
     * uuid标识：0-生成队列（未下载），1-生成历史（已下载）
     */
	private String uuidMark;

	public String getBatchNumId() {
		return batchNumId;
	}

	public void setBatchNumId(String batchNumId) {
		this.batchNumId = batchNumId;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUuidType() {
		return uuidType;
	}

	public void setUuidType(String uuidType) {
		this.uuidType = uuidType;
	}

	public String getUuidMark() {
		return uuidMark;
	}

	public void setUuidMark(String uuidMark) {
		this.uuidMark = uuidMark;
	}
	


}
