package com.iot.control.activity.controller;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.service.ActivityRecordMongoService;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private ActivityRecordMongoService activityRecordMongoService;


    /*@Override
    public int saveActivityRecord(String type, String icon, String activity, String foreignId, Long createBy) {
        return activityRecordService.saveActivityRecord(type, icon, activity, foreignId, createBy);
    }*/

    @Override
    public void saveActivityRecord(@RequestBody List<ActivityRecordReq> activityRecordReq) {
        activityRecordMongoService.saveActivityRecord(activityRecordReq);
    }

    @Override
    public int delActivityRecord(@RequestBody ActivityRecordReq activityRecordReq) {
        return activityRecordMongoService.delActivityRecord(activityRecordReq);
    }

    @Override
    public PageInfo<ActivityRecordResp> queryActivityRecord(@RequestBody ActivityRecordReq activityRecordReq) {
        return activityRecordMongoService.queryActivityRecord(activityRecordReq);
    }

	@Override
	public List<ActivityRecordResp> queryActivityRecordByCondition(@RequestBody ActivityRecordReq activityRecordReq) {
		return activityRecordMongoService.queryActivityRecordByCondition(activityRecordReq);
	}

    @Override
    public List<ActivityRecordResp> queryScheduleLog(@RequestBody ActivityRecordReq req) {
        return activityRecordMongoService.queryScheduleLog(req);
    }
}
