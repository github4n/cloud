package com.iot.video.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：事件
 * 创建人： wujianlong
 * 创建时间：2017年9月6日 下午6:22:47
 * 修改人： wujianlong
 * 修改时间：2017年9月6日 下午6:22:47
 */
public class UntieTaskVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 执行状态 0：未执行  1：执行中*/
	private int status;

	/** 执行时间 */
	private Date execTime;

	/** 设备id */
	private String deviceId;
	
	/** 计划id */
	private String planId;

	/** 重试次数*/
	private Integer retry;

	/** 最近失败原因*/
	private String failedDesc;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getExecTime() {
		return execTime;
	}

	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public String getFailedDesc() {
		return failedDesc;
	}

	public void setFailedDesc(String failedDesc) {
		this.failedDesc = failedDesc;
	}
}
