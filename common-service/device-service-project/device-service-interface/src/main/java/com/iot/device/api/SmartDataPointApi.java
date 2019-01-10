package com.iot.device.api;

import com.iot.device.api.fallback.SmartDataPointApiFallbackFactory;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/29 11:01
 * @Modify by:
 */

@Api(tags = "音箱设备功能点接口")
@FeignClient(value = "device-service", fallbackFactory = SmartDataPointApiFallbackFactory.class)
@RequestMapping(value = "/smartDataPoint")
public interface SmartDataPointApi {


    @ApiOperation("根据tenantId、propertyId、smart获取一个SmartDataPoint")
    @RequestMapping(value = "/getSmartDataPoint", method = RequestMethod.GET)
    SmartDataPointResp getSmartDataPoint(@RequestParam(value = "tenantId") Long tenantId,
                                         @RequestParam(value = "propertyId") Long propertyId,
                                         @RequestParam(value = "smart") Integer smart);

}
