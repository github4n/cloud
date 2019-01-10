package com.iot.control.activity.api;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.vo.req.AppErrorRecordReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Api("app错误日志接口")
@FeignClient(value = "control-service")
@RequestMapping("/appErrorRecord")
public interface AppErrorRecordApi {


	@ApiOperation("保存app错误日志")
    @RequestMapping(value = "/saveAppErrorRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq);

    @ApiOperation("删除app错误日志")
    @RequestMapping(value = "/delAppErrorRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public int delAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq);

    @ApiOperation("查询app错误日志")
    @RequestMapping(value = "/queryAppErrorRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PageInfo queryAppErrorRecord(@RequestBody AppErrorRecordReq appErrorRecordReq);

}
