package com.iot.device.vo.rsp;

import java.util.List;

public class NetworkTypeInfoResp extends NetworkTypeResp {

	/**
	 * 技术方案ids
	 */
	private List<Long> technicalIds;

	public List<Long> getTechnicalIds() {
		return technicalIds;
	}

	public void setTechnicalIds(List<Long> technicalIds) {
		this.technicalIds = technicalIds;
	}
}
