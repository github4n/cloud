package com.iot.building.camera.vo.resp;

import java.util.Date;

public class CameraPropertyResp {

	private Long id;

	private Long ch1;

	private Long ch2;

	private Long ch3;

	private Long ch4;

	private String time;

	private Long cameraRecordId;

	private Long sum;

	private Long maxSum;

	private Date createTime;

	private String timeFlag;

	public String getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(String timeFlag) {
		this.timeFlag = timeFlag;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getMaxSum() {
		return maxSum;
	}

	public void setMaxSum(Long maxSum) {
		this.maxSum = maxSum;
	}

	public Long getCameraRecordId() {
		return cameraRecordId;
	}

	public void setCameraRecordId(Long cameraRecordId) {
		this.cameraRecordId = cameraRecordId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getCh1() {
		return ch1;
	}

	public void setCh1(Long ch1) {
		this.ch1 = ch1;
	}

	public Long getCh2() {
		return ch2;
	}

	public void setCh2(Long ch2) {
		this.ch2 = ch2;
	}

	public Long getCh3() {
		return ch3;
	}

	public void setCh3(Long ch3) {
		this.ch3 = ch3;
	}

	public Long getCh4() {
		return ch4;
	}

	public void setCh4(Long ch4) {
		this.ch4 = ch4;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}
}
