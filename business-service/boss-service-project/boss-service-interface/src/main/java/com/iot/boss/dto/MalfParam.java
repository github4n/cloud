package com.iot.boss.dto;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;

@ApiModel
public class MalfParam extends SearchParam {
	
	/**执行周期*/
	private int executionCycle;

	public int getExecutionCycle() {
		return executionCycle;
	}

	public void setExecutionCycle(int executionCycle) {
		this.executionCycle = executionCycle;
	}
	
}
