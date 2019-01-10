package com.iot.video.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 项目名称：IOT云平台 模块名称：录影计划 功能描述： 创建人： wujianlong 创建时间：2017年8月10日 上午10:37:46
 * 修改人： wujianlong 修改时间：2017年8月10日 上午10:37:46
 */
public class LapseIdDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private List<String> LapseFileId;

	/** 套餐id */
	private List<Long> LapseEventId;

	public List<String> getLapseFileId() {
		return LapseFileId;
	}

	public void setLapseFileId(List<String> lapseFileId) {
		LapseFileId = lapseFileId;
	}

	public List<Long> getLapseEventId() {
		return LapseEventId;
	}

	public void setLapseEventId(List<Long> lapseEventId) {
		LapseEventId = lapseEventId;
	}

}
