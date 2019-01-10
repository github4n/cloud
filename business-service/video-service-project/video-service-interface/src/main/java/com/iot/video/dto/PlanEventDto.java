package com.iot.video.dto;

import java.io.Serializable;


public class PlanEventDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private String planId;

	/** 套餐使用量 */
	private Integer useQuantity;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Integer getUseQuantity() {
		return useQuantity;
	}

	public void setUseQuantity(Integer useQuantity) {
		this.useQuantity = useQuantity;
	}
}
