package com.iot.device.vo.rsp;

import com.iot.common.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkTypeResp implements Serializable {

	/***/
	private static final long serialVersionUID = 6079553293481121802L;

	private Long id;
	/**
	 * 配网模式名称
	 */
	private String networkName;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 技术方案id字符串
	 */
	private String technicalIdsStr;

	/**
	 * 配网编码
	 */
	private String typeCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNetworkName() {
		return networkName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTechnicalIdsStr() {
		return technicalIdsStr;
	}

	public void setTechnicalIdsStr(String technicalIdsStr) {
		this.technicalIdsStr = technicalIdsStr;
	}

	public List<Long> getTechnicalIds() {
		if (StringUtil.isBlank(technicalIdsStr)) {
			return Collections.emptyList();
		}
		String []strs = technicalIdsStr.split(",");
		List<Long> ids = new ArrayList<>();
		for (String id : strs) {
			ids.add(Long.valueOf(id));
		}
		return ids;
	}
}
