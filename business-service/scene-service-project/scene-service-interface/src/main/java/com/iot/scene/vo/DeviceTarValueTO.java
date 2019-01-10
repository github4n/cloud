package com.iot.scene.vo;

import com.lds.iot.scene.domain.DeviceTarValue;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景模板
 * 创建人： wujianlong
 * 创建时间：2017年11月15日 上午11:34:41
 * 修改人： wujianlong
 * 修改时间：2017年11月15日 上午11:34:41
 */
public class DeviceTarValueTO extends DeviceTarValue {

	/** 模板详情id*/
	private String templateDetailId;

	public String getTemplateDetailId() {
		return templateDetailId;
	}

	public void setTemplateDetailId(String templateDetailId) {
		this.templateDetailId = templateDetailId;
	}

}
