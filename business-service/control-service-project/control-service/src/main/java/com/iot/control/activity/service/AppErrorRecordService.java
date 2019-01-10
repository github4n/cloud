package com.iot.control.activity.service;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.vo.req.AppErrorRecordReq;


public interface AppErrorRecordService {

	void saveAppErrorRecord(AppErrorRecordReq appErrorRecordReq);
	
	int delAppErrorRecord(AppErrorRecordReq appErrorRecordReq) ;

	PageInfo queryAppErrorRecordByUser(AppErrorRecordReq appErrorRecordReq);

}
