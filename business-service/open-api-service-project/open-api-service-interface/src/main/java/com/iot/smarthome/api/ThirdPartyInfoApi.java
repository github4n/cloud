package com.iot.smarthome.api;

import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Descrpiton:
 *      第三方接入信息接口
 *
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:30
 * @Modify by:
 */

@Api(tags = "第三方接入信息接口")
@FeignClient(value = "open-api-service")
@RequestMapping("/thirdPartyInfo")
public interface ThirdPartyInfoApi {

    @ApiOperation("根据clientId获取第三方信息")
    @RequestMapping(value = "/getByClientId", method = RequestMethod.GET)
    ThirdPartyInfoResp getByClientId(@RequestParam(value = "clientId", required = true) String clientId);

    @ApiOperation("根据id获取第三方信息")
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    ThirdPartyInfoResp getById(@RequestParam(value = "id", required = true) Long id);
}
