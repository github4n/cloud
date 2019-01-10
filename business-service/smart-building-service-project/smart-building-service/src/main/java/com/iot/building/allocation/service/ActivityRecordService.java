package com.iot.building.allocation.service;

import java.util.Date;
import java.util.List;

import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.common.helper.Page;

public interface ActivityRecordService {
	
	int saveActivityRecord(ActivityRecordReq activityRecordReq);
	
	int delActivityRecord(ActivityRecordReq activityRecordReq);
	
	Page<ActivityRecordResp> queryActivityRecord(ActivityRecordReq activityRecordReq);
	
	public List<ActivityRecordResp> queryActivityRecordByCondition(ActivityRecordReq activityRecordReq);

	List<ActivityRecordResp> queryByValidityDate(Long spaceId,String templateId,String type,Long startDate,Long endDate,Long spaceTemplateId);
	 
}
