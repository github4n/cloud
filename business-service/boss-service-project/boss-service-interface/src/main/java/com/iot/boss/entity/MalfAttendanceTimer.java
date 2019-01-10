package com.iot.boss.entity;

import java.io.Serializable;

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
public class MalfAttendanceTimer implements Serializable {

	/***/
	private static final long serialVersionUID = -4318240459903842061L;

	/**定时器id*/
	private Long id;
	
	/**定时器名称*/
	private String timerName;
	
	/**执行周期*/
	private int executionCycle;
	
	/**开始时间*/
	private String startTime;
	
	/**结束时间*/
	private String endTime;
	
	/**管理员id*/
	private Long adminId;
	
	/**定时器状态*/
	private int timerStatus;
	

	/**结束时间*/
	private String adminName;
	
	/**权重*/
	private int weight;

	public MalfAttendanceTimer(Long id, String timerName, int executionCycle, String startTime,
			String endTime, Long adminId, int timerStatus, int weight, String adminName) {
		this.id = id;
		this.timerName = timerName;
		this.executionCycle = executionCycle;
		this.startTime = startTime;
		this.endTime = endTime;
		this.adminId = adminId;
		this.timerStatus = timerStatus;
		this.weight = weight;
		this.adminName = adminName;
	}
	
	public MalfAttendanceTimer() {
	}
	
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public int getTimerStatus() {
		return timerStatus;
	}

	public void setTimerStatus(int timerStatus) {
		this.timerStatus = timerStatus;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
		int result=getId().hashCode();
		//result=31*result*timerName.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("equals");
		if(this==obj)return true;
		if(obj==null  || getClass()!=obj.getClass())return false;
		MalfAttendanceTimer p=(MalfAttendanceTimer) obj;
		return getWeight()<p.getWeight();
	}
}
