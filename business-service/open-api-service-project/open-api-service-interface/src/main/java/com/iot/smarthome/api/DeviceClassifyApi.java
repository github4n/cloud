package com.iot.smarthome.api;

import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Descrpiton:
 *      设备分类
 *
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:30
 * @Modify by:
 */

@Api(tags = "设备分类接口")
@FeignClient(value = "open-api-service")
@RequestMapping("/deviceClassify")
public interface DeviceClassifyApi {


    @ApiOperation("获取产品归属的分类")
    @RequestMapping(value = "/getDeviceClassifyByProductId", method = RequestMethod.GET)
    DeviceClassifyResp getDeviceClassifyByProductId(@RequestParam("productId") Long productId);

    @ApiOperation("获取指定编码的分类")
    @RequestMapping(value = "/getByTypeCode", method = RequestMethod.GET)
    DeviceClassifyResp getByTypeCode(String typeCode);
}
