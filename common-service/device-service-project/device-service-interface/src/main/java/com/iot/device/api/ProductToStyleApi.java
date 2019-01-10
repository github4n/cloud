package com.iot.device.api;

import com.iot.device.api.fallback.ProductToStyleApiFallbackFactory;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.rsp.ProductToStyleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("产品样式接口")
@FeignClient(value = "device-service" , fallbackFactory = ProductToStyleApiFallbackFactory.class)
@RequestMapping("/product/style")
public interface ProductToStyleApi {

    @ApiOperation("保存或修改")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ProductToStyleReq productToStyleReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    void delete(@RequestParam("id") Long id);

    @ApiOperation("获取")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    List<ProductToStyleResp> list(@RequestParam("productId") Long productId);
}
