package com.iot.common.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.control.activity.api.AppErrorRecordApi;
import com.iot.control.activity.vo.req.AppErrorRecordReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: app error log upload
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/15 10:03
 **/
@Api(description = "App错误日志接口")
@RestController
@RequestMapping("/appLogController")
public class AppLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLogController.class);

    @Autowired
    private AppErrorRecordApi appErrorRecordApi;

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "app错误日志记录", notes = "保存app错误日志记录")
    @RequestMapping(value = "/saveAppErrorRecord", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> saveAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq) {
//        LOGGER.info("upload_app_error_log message {}",JSON.toJSONString(appErrorRecordReq));
//        appErrorRecordApi.saveAppErrorRecord(appErrorRecordReq);
        return new CommonResponse<>(ResultMsg.SUCCESS);
    }

}
