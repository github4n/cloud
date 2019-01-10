package com.iot.module.controller;

import com.google.common.collect.Lists;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.module.api.ModuleCoreApi;
import com.iot.shcs.module.vo.resp.GetProductModuleResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:21 2018/10/22
 * @Modify by:
 */
@Slf4j
@RestController
@RequestMapping("/module")
public class ModuleController {

    @Autowired
    private ModuleCoreApi moduleCoreApi;


    /**
     * 获取产品对应的功能组定义列表 [新增 弹窗列表]---列表去重
     *
     * @param productId
     * @return
     * @author lucky
     * @date 2018/7/2 9:27
     */
    @LoginRequired(Action.Normal)
    @ApiOperation("获取产品对应的功能组定义列表")
    @RequestMapping(value = "/v1/findModuleListByProductId", method = RequestMethod.GET)
    public CommonResponse<GetProductModuleResp> findServiceModuleListByProductId(@RequestParam("productId") Long productId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        AssertUtils.notNull(productId, "productId.notnull");
        GetProductModuleResp resultData = moduleCoreApi.findServiceModuleListByProductId(tenantId, productId);
        return CommonResponse.success(resultData);
    }

    @LoginRequired(Action.Skip)
    @ApiOperation("获取租户/app对应的功能组定义列表")
    @RequestMapping(value = "/v1/findServiceModuleList", method = RequestMethod.GET)
    public CommonResponse<List<GetProductModuleResp>> findServiceModuleList(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId) {
        List<GetProductModuleResp> resultDataList = Lists.newArrayList();

        resultDataList = moduleCoreApi.findServiceModuleList(tenantId, appId);

        return CommonResponse.success(resultDataList);
    }

}
