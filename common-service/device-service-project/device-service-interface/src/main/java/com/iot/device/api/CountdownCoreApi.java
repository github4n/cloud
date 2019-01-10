package com.iot.device.api;

import com.iot.device.api.fallback.CountdownCoreApiFallbackFactory;
import com.iot.device.vo.req.CountDownReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: lucky
 * @Descrpiton: 倒计时api
 * @Date: 17:38 2018/10/18
 * @Modify by:
 */
@FeignClient(value = "device-service", fallbackFactory = CountdownCoreApiFallbackFactory.class)
@RequestMapping("/countdownCore")
public interface CountdownCoreApi {

    @ApiOperation(value = "添加倒计时设置")
    @RequestMapping(value = "addCountDown", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addCountDown(@RequestBody CountDownReq countDownReq);
}
