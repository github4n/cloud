package com.iot.device.api;

import com.iot.device.api.fallback.ProductToServiceModuleApiFallbackFactory;
import com.iot.device.api.fallback.ServiceToActionApiFallbackFactory;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * @Author: zhangyue
 * @Descrpiton:
 * @Modify by:
 */
@Api(tags = "产品关联功能组")
@FeignClient(value = "device-service", fallbackFactory = ProductToServiceModuleApiFallbackFactory.class)
@RequestMapping("/productServiceModule")
public interface ProductToServiceModuleApi {

    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody ProductToServiceModuleReq productToServiceModuleReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody ProductToServiceModuleReq productToServiceModuleReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);


    /**
     * @despriction：校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    @ApiOperation(value = "校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件", notes = "校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件")
    @RequestMapping(value = "/checkProductHadIftttType", method = RequestMethod.GET)
    boolean checkProductHadIftttType(@RequestParam("productId") Long productId);

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    @ApiOperation(value = "根据ifttt类型找到对应的模组信息", notes = "根据ifttt类型找到对应的模组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if；then：支持then）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/queryServiceModuleDetailByIfttt", method = RequestMethod.GET)
    PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(@RequestParam("productId") Long productId,
                                                                   @RequestParam("iftttType") String iftttType);
}
