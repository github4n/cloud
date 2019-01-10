package com.iot.center.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

@ApiModel
public class SpaceEhiVO implements Serializable {

	private static final long serialVersionUID = -8017858441156118689L;

	@ApiModelProperty(example = "[ehi:0,TempStatus:0,HumStatus:0,Illuminance:0,pm:0,noise:0,co:0,formaldehyde:0.0]")
	private Map<String, Object> properties;

	public SpaceEhiVO() {
		this.properties = Maps.newHashMap();
		Random random = new Random();
		properties.put("ehi", 80 + random.nextInt(20));
		properties.put("Temp", 15 + random.nextInt(5));
		properties.put("Hum", 50 + random.nextInt(20));
		properties.put("Illuminance", 400 + random.nextInt(50));
		properties.put("pm", 40 + random.nextInt(5));
		properties.put("noise", 50 + random.nextInt(10));
		properties.put("co", 50 + random.nextInt(20));
		properties.put("formaldehyde", random.nextInt(10) * 0.01);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
