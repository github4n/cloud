package com.iot.boss.vo.technicalrelate;

import java.io.Serializable;
import java.util.List;

public class BossNetworkTypeInfoResp implements Serializable {

	/***/
	private static final long serialVersionUID = 6079553293481121802L;

	private Long id;
	/**
	 * 配网模式名称
	 */
	private String networkName;

	/**
	 * 配网模式code
	 */
	private String typeCode;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 支持的技术方案名
	 */
   private List<String> technicalNameList;



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

	public List<String> getTechnicalNameList() {
		return technicalNameList;
	}

	public void setTechnicalNameList(List<String> technicalNameList) {
		this.technicalNameList = technicalNameList;
	}

	public BossNetworkTypeInfoResp() {
	}

	public BossNetworkTypeInfoResp(Long id, String networkName, String description, String typeCode) {
		this.id = id;
		this.networkName = networkName;
		this.description = description;
		this.typeCode = typeCode;
	}
}
