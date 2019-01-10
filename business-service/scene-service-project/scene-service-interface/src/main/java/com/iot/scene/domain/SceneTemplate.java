package com.iot.scene.domain;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景模板
 * 创建人： wujianlong
 * 创建时间：2017年11月14日 下午3:26:25
 * 修改人： wujianlong
 * 修改时间：2017年11月14日 下午3:26:25
 */
public class SceneTemplate implements Serializable {

	private static final long serialVersionUID = -5886429646671137263L;

	private String username;

	/** 模板名*/
	private String name;

	/** deviceTarValue的json形式*/
	private String deviceTarValue;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceTarValue() {
		return deviceTarValue;
	}

	public void setDeviceTarValue(String deviceTarValue) {
		this.deviceTarValue = deviceTarValue;
	}

}
