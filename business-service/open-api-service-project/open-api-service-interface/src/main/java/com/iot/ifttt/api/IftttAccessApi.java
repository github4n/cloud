package com.iot.ifttt.api;

import com.iot.ifttt.vo.CheckTriggerReq;
import com.iot.ifttt.vo.CheckNotifyReq;
import com.iot.ifttt.vo.ActionReq;
import com.iot.ifttt.vo.GetProductReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 描述：IFTTT接入接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/29 10:36
 */

@Api(tags = "IFTTT接入接口")
@FeignClient(value = "open-api-service")
@RequestMapping("/ifttt")
public interface IftttAccessApi {

    @ApiOperation("程序校验")
    @RequestMapping(value = "/checkApplet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int checkApplet(@RequestBody CheckTriggerReq req);

    @ApiOperation("通知校验（实时API）")
    @RequestMapping(value = "/checkNotify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void checkNotify(@RequestBody CheckNotifyReq req);

    @ApiOperation("获取产品列表")
    @RequestMapping(value = "/getProductList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Long> getProductList(@RequestBody GetProductReq req);

    @ApiOperation("控制设备")
    @RequestMapping(value = "/controlDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doAction(@RequestBody ActionReq req);

    @ApiOperation("获取配置信息")
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    Map<String,Object> getConfig();
}
