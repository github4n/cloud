package com.iot.device.api;

import com.iot.device.api.fallback.ProductConfigNetmodeApiFallbackFactory;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "产品接口")
@FeignClient(value = "device-service", fallbackFactory = ProductConfigNetmodeApiFallbackFactory.class)
@RequestMapping("/product")
public interface ProductConfigNetmodeApi {

    @ApiOperation(value = "保存", notes = "保存")
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long insert(@RequestBody ProductConfigNetmodeReq productConfigNetmodeReq);

    @ApiOperation(value = "批量保存", notes = "批量保存")
    @RequestMapping(value = "/insertMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void insertMore(@RequestBody List<ProductConfigNetmodeReq> productConfigNetmodeReqs);

    @ApiOperation(value = "批量保存", notes = "批量保存")
    @RequestMapping(value = "/listByProductId", method = RequestMethod.GET)
    List<ProductConfigNetmodeRsp> listByProductId(@RequestParam("productId") Long productId);

    @ApiOperation(value = "批量删除", notes = "批量删除")
    @RequestMapping(value = "/deleteMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteMore(@RequestBody List ids);

}
