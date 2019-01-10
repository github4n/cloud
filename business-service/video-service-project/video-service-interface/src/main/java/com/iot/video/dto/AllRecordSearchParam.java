package com.iot.video.dto;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;

@ApiModel
public class AllRecordSearchParam extends SearchParam {

	/** 查询Key */
	private String key;

	/** 订单状态 */
	private String recordState;

	/** 时间类型 */
	private String timeType;

	/** 开始时间 */
	private String startTime;
	
	/** 结束时间 */
	private String endTime;
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	

}
