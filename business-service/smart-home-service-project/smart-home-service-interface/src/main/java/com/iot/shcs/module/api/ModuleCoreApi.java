package com.iot.shcs.module.api;

import com.iot.common.beans.CommonResponse;
import com.iot.shcs.module.vo.resp.GetProductModuleResp;
import com.iot.shcs.module.vo.resp.ListProductByTenantResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:12 2018/11/12
 * @Modify by:
 */
@Api("功能组聚合接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/smartHome/moduleCoreApi")
public interface ModuleCoreApi {

    @ApiOperation("获取产品对应的功能组定义列表")
    @RequestMapping(value = "/v1/findModuleListByProductId", method = RequestMethod.GET)
    GetProductModuleResp findServiceModuleListByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId);

    @ApiOperation("获取租户/app对应的功能组定义列表")
    @RequestMapping(value = "/v1/findServiceModuleList", method = RequestMethod.GET)
    List<GetProductModuleResp> findServiceModuleList(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId);

    @ApiOperation("获取租户/app对应的产品定义列表")
    @RequestMapping(value = "/v1/findModuleProductListByTenantId", method = RequestMethod.GET)
    List<ListProductByTenantResp> findModuleProductListByTenantId(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId);
}
