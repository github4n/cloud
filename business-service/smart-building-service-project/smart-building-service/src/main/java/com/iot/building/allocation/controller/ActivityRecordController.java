package com.iot.building.allocation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.allocation.api.ActivityRecordApi;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.common.helper.Page;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 14:09
 * 修改人:
 * 修改时间：
 */

@RestController
public class ActivityRecordController implements ActivityRecordApi {

    @Autowired
    private com.iot.building.allocation.service.ActivityRecordService activityRecordService;
    
    @Autowired
    private com.iot.control.activity.api.ActivityRecordApi activityRecordApi;
    

	@Override
	public void saveActivityRecordToB(@RequestBody ActivityRecordReq activityRecordReq) {
		activityRecordService.saveActivityRecord(activityRecordReq);
	}

	@Override
	public Page<ActivityRecordResp> queryActivityRecordToB(ActivityRecordReq activityRecordReq) {
		return activityRecordService.queryActivityRecord(activityRecordReq);
	}

	@Override
	public List<ActivityRecordResp> queryScheduleLog(ActivityRecordReq req) {
		return activityRecordService.queryActivityRecordByCondition(req);
	}


}
