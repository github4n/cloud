package com.iot.control.activity.api;

import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
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
 * 创建时间: 2018/4/27 14:57
 * 修改人:
 * 修改时间：
 */

@Api("设备上下线状态接口")
@FeignClient(value = "control-service")
@RequestMapping("/onlineStatusRecord")
public interface OnlineStatusRecordApi {

    @ApiOperation("保存记录")
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveOnlineStatusRecord(@RequestBody OnlineStatusRecordReq record);

    @ApiOperation("保存记录[批量]")
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveBatchOnlineStatusRecord(@RequestBody List<OnlineStatusRecordReq> recordReqList);
}
