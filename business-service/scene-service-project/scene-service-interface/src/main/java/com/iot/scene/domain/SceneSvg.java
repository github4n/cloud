package com.iot.scene.domain;

import java.io.Serializable;

public class SceneSvg implements Serializable{
	
	private static final long serialVersionUID = -1285460533961122256L;

	private String id;
	
	private String spaceId;
	
	private String svgUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getSvgUrl() {
		return svgUrl;
	}

	public void setSvgUrl(String svgUrl) {
		this.svgUrl = svgUrl;
	}

}
