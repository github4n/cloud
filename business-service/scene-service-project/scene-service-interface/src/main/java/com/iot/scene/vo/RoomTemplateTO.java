package com.iot.scene.vo;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：房间情景
 * 功能描述：
 * 创建人： wujianlong
 * 创建时间：2017年11月16日 下午2:27:08
 * 修改人： wujianlong
 * 修改时间：2017年11月16日 下午2:27:08
 */
public class RoomTemplateTO implements Serializable {

	private static final long serialVersionUID = -7122805278265385954L;

	/** 房间id*/
	private String spaceId;

	/** 模板id,多个以逗号分隔*/
	private String templateIds;

	private String username;

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
