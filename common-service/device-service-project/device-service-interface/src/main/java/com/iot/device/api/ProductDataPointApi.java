package com.iot.device.api;

import com.iot.device.api.fallback.ProductDataPointApiFallbackFactory;
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "产品功能接口")
@FeignClient(value = "device-service", fallbackFactory = ProductDataPointApiFallbackFactory.class)
@RequestMapping(value = "/productdataPoint")
public interface ProductDataPointApi {


}
