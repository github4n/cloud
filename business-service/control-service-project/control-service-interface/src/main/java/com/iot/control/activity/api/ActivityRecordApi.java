package com.iot.control.activity.api;

import com.github.pagehelper.PageInfo;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 14:02
 * 修改人:
 * 修改时间：
 */

@Api("活动日志接口")
@FeignClient(value = "control-service")
@RequestMapping("/activityRecord")
public interface ActivityRecordApi {
    
	@ApiOperation("保存活动日志")
    @RequestMapping(value = "/saveActivityRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveActivityRecord(@RequestBody List<ActivityRecordReq> activityRecordReq);

    @ApiOperation("删除活动日志")
    @RequestMapping(value = "/delActivityRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public int delActivityRecord(@RequestBody ActivityRecordReq activityRecordReq);

    @ApiOperation("查询活动日志")
    @RequestMapping(value = "/queryActivityRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<ActivityRecordResp> queryActivityRecord(@RequestBody ActivityRecordReq activityRecordReq);
    

    @ApiOperation("根据查询活动日志")
    @RequestMapping(value = "/queryActivityRecordByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ActivityRecordResp> queryActivityRecordByCondition(@RequestBody ActivityRecordReq activityRecordReq);


    @ApiOperation("根据查询活动日志")
    @RequestMapping(value = "/queryScheduleLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ActivityRecordResp> queryScheduleLog(@RequestBody ActivityRecordReq activityRecordReq);
}
