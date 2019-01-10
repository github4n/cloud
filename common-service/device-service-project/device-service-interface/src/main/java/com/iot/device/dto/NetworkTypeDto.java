package com.iot.device.dto;

import java.io.Serializable;
import java.util.List;

public class NetworkTypeDto implements Serializable {

	/***/
	private static final long serialVersionUID = 6079553293481121802L;

	private Long id;
	
	/**
     * 创建人
     */
	private String networkName;

	/**
	 * 类型code
	 */
	private String typeCode;
	
	/**
     * 配网模式名称
     */
	private String description;
	
	/**
     * 技术方案ID
     */
    private List<Long> technicalSchemeIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getTechnicalSchemeIds() {
		return technicalSchemeIds;
	}

	public void setTechnicalSchemeIds(List<Long> technicalSchemeIds) {
		this.technicalSchemeIds = technicalSchemeIds;
	}

}
