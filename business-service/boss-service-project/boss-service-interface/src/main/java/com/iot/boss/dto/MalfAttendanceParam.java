package com.iot.boss.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：管理员值班
 * 功能描述：管理员值班
 * 创建人： 李帅
 * 创建时间：2018年5月15日 下午5:58:34
 * 修改人：李帅
 * 修改时间：2018年5月15日 下午5:58:34
 */
@ApiModel
public class MalfAttendanceParam implements Serializable {

	/***/
	private static final long serialVersionUID = 4493593252526344118L;

	/**定时器id*/
	private String malfAttendanceId;
	
	/**定时器名称*/
	private String timerName;
	
	/**执行周期*/
	private int executionCycle;
	
	/**开始时间*/
	private String startTime;
	
	/**结束时间*/
	private String endTime;
	
	/**管理员id*/
	private String adminId;
	
	/**结束时间*/
	private String operateType;
	
	public MalfAttendanceParam() {
	}

	public String getMalfAttendanceId() {
		return malfAttendanceId;
	}

	public void setMalfAttendanceId(String malfAttendanceId) {
		this.malfAttendanceId = malfAttendanceId;
	}

	public String getTimerName() {
		return timerName;
	}

	public void setTimerName(String timerName) {
		this.timerName = timerName;
	}

	public int getExecutionCycle() {
		return executionCycle;
	}

	public void setExecutionCycle(int executionCycle) {
		this.executionCycle = executionCycle;
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

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
}
