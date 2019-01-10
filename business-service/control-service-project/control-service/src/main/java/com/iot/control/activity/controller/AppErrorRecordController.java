package com.iot.control.activity.controller;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.api.AppErrorRecordApi;
import com.iot.control.activity.service.AppErrorRecordService;
import com.iot.control.activity.vo.req.AppErrorRecordReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppErrorRecordController implements AppErrorRecordApi {

    @Autowired
    private AppErrorRecordService appErrorRecordService;

    @Override
    public void saveAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq) {
        appErrorRecordService.saveAppErrorRecord(appErrorRecordReq);
    }

    @Override
    public int delAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq) {
        return appErrorRecordService.delAppErrorRecord(appErrorRecordReq);
    }

    @Override
    public PageInfo queryAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq) {
        return null;
    }




}
