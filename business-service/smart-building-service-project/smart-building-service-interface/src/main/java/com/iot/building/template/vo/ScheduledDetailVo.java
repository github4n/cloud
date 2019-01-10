package com.iot.building.template.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：空间-模板
 * 创建人： fenglijian
 * 创建时间：2018年05月03日 下午20:20:53
 */
public class ScheduledDetailVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SpaceTemplateResp spaceTemplateResp;
	
	private List<ActivityRecordResp> activeList;

	public SpaceTemplateResp getSpaceTemplateResp() {
		return spaceTemplateResp;
	}

	public void setSpaceTemplateResp(SpaceTemplateResp spaceTemplateResp) {
		this.spaceTemplateResp = spaceTemplateResp;
	}

	public List<ActivityRecordResp> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<ActivityRecordResp> activeList) {
		this.activeList = activeList;
	}

}
