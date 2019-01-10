package com.iot.control.activity.service;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;

import java.util.List;

public interface ActivityRecordMongoService {

	/*int saveActivityRecord(String type, String icon, String activity, String foreignId, Long createBy);*/

	void saveActivityRecord(List<ActivityRecordReq> resut);

	int delActivityRecord(ActivityRecordReq activityRecordReq);

	PageInfo<ActivityRecordResp> queryActivityRecord(ActivityRecordReq activityRecordReq);

	List<ActivityRecordResp> queryActivityRecordByCondition(ActivityRecordReq activityRecordReq);

	List<ActivityRecordResp> queryScheduleLog(ActivityRecordReq req);
}
