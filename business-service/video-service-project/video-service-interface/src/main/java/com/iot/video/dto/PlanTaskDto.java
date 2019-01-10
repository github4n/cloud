package com.iot.video.dto;

import java.io.Serializable;


public class PlanTaskDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private String planId;

	/** taskDate */
	private String taskDateStr;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTaskDateStr() {
		return taskDateStr;
	}

	public void setTaskDateStr(String taskDateStr) {
		this.taskDateStr = taskDateStr;
	}
}
