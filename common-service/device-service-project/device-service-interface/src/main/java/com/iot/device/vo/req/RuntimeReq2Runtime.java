package com.iot.device.vo.req;

import javax.validation.constraints.NotNull;

public class RuntimeReq2Runtime {
	/**
	 * 用户Id
	 */
	private String cookieUserId;
	
	/**
	 * 会话Token
	 */
	private String cookieUserToken;
	
	/**
	 * 设备ID
	 */
	@NotNull(message = "deviceId.notnull")
	private String devId;
	
	/**
	 * 电量统计类型
	 */
	private String statisticsType;
	
	/**
	 * 分页大小
	 */
	private int pageSize;
	
	/**
	 * 偏移量
	 */
	private int offset;
	
	private Long orgId;
	
	private Long tenantId;

	private Long userId;

	private int start;

	private int end;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getCookieUserId() {
		return cookieUserId;
	}

	public void setCookieUserId(String cookieUserId) {
		this.cookieUserId = cookieUserId;
	}

	public String getCookieUserToken() {
		return cookieUserToken;
	}

	public void setCookieUserToken(String cookieUserToken) {
		this.cookieUserToken = cookieUserToken;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
