package com.iot.device.api;

import com.iot.device.api.fallback.ProductCoreApiFallbackFactory;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.req.device.UpdateProductReq;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "device-service", fallbackFactory = ProductCoreApiFallbackFactory.class)
@RequestMapping("/productCore")
public interface ProductCoreApi {

    @RequestMapping(value = "/listProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListProductRespVo> listProducts(@RequestBody @Validated ListProductInfoReq params);

    @RequestMapping(value = "/getByProductId", method = RequestMethod.GET)
    GetProductInfoRespVo getByProductId(@RequestParam(value = "productId", required = true) Long productId);

    @RequestMapping(value = "/getByProductModel", method = RequestMethod.GET)
    GetProductInfoRespVo getByProductModel(@RequestParam(value = "productModel", required = true) String productModel);


    @RequestMapping(value = "/listByProductModel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GetProductInfoRespVo> listByProductModel(@RequestBody Collection<String> productModelList);

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdate(@RequestBody UpdateProductReq params);

    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateBatch(@RequestBody List<UpdateProductReq> paramsList);

    @RequestMapping(value = "/listProductAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListProductRespVo> listProductAll();
}
