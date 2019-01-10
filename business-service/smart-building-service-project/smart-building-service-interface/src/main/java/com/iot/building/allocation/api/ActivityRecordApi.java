package com.iot.building.allocation.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.common.helper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@FeignClient(value = "building-control-service")
@RequestMapping("/activityRecord")
public interface ActivityRecordApi {

	@ApiOperation("保存活动日志")
	@RequestMapping(value = "/saveActivityRecordToB", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveActivityRecordToB(@RequestBody ActivityRecordReq activityRecordReq);

	@ApiOperation("查询活动日志")
    @RequestMapping(value = "/queryActivityRecordToB", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<ActivityRecordResp> queryActivityRecordToB(@RequestBody ActivityRecordReq activityRecordReq);
    
    @ApiOperation("根据查询活动日志")
    @RequestMapping(value = "/queryScheduleLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ActivityRecordResp> queryScheduleLog(@RequestBody ActivityRecordReq req);
}
