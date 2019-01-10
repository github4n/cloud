package com.iot.shcs.ipc.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：MQTT中间服务
 * 功能描述：MQTT中间服务对外接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月16日 10:52
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月16日 10:52
 */
@Api(tags = "ipc接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/ipc")
public interface IpcApi {

    /**
     * 描述：计划绑定设备下发通知
     *
     * @param planId   计划Id
     * @param deviceId 设备id
     * @return
     * @author 490485964@qq.com
     * @date 2018/4/16 9:50
     */
    @ApiOperation(value = "计划绑定设备,通知设备", notes = "计划绑定设备,通知设备")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划Id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/subscribe/service/notifyDeviceBindingPlan", method = {RequestMethod.POST})
    void notifyDeviceRecordConfig(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId);

    /**
     * 描述：给设备下发录影任务列表\更改计划执行状态,通知设备
     *
     * @param planId 计划Id
     * @return
     * @author 490485964@qq.com
     * @date 2018/4/16 9:53
     */
    @ApiOperation(value = "给设备下发任务列表或录影开关,通知设备", notes = "给设备下发任务列表,通知设备")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划Id", paramType = "query", required = true, dataType = "String")})
    @RequestMapping(value = "/subscribe/service/notifyDevicePlanInfo", method = {RequestMethod.POST})
    void notifyDevicePlanInfo(@RequestParam("planId") String planId);

    /**
     * 描述：获取文件服务put预签名url
     *
     * @param deviceId
     * @param planId
     * @param fileType
     * @return
     * @author 李帅
     * @created 2018年7月16日 下午8:06:40
     * @since
     */
    @ApiOperation(value = "获取文件服务put预签名url", notes = "获取文件服务put预签名url")
    @ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备UUID", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "planId", value = "计划Id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "fileType", value = "文件类型", paramType = "query", required = true, dataType = "String")})
    @RequestMapping(value = "/subscribe/service/getFilePreSignUrls", method = {RequestMethod.GET})
    Map<String, Object> getFilePreSignUrls(@RequestParam("deviceId") String deviceId,
                                           @RequestParam("planId") String planId, @RequestParam("fileType") String fileType);

}
