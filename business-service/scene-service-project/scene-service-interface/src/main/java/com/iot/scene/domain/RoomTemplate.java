package com.iot.scene.domain;

import com.iot.common.beans.CommonBean;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：房间情景
 * 创建人： wujianlong
 * 创建时间：2017年11月16日 上午11:52:34
 * 修改人： wujianlong
 * 修改时间：2017年11月16日 上午11:52:34
 */
public class RoomTemplate extends CommonBean {

	private static final long serialVersionUID = -4616442021374659311L;

	/** 模板id*/
	private String templateId;

	/** 房间id*/
	private String spaceId;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

}
