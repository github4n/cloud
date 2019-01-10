package com.iot.scene.vo;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：模板
 * 创建人： wujianlong
 * 创建时间：2017年11月16日 上午11:29:53
 * 修改人： wujianlong
 * 修改时间：2017年11月16日 上午11:29:53
 */
public class TemplateVO implements Serializable {

	private static final long serialVersionUID = -7276209311915920905L;

	/** 模板id*/
	private String id;

	/** 模板名*/
	private String name;

	/** 情景状态 0-离线，1-在线*/
	private int sceneStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSceneStatus() {
		return sceneStatus;
	}

	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

}
