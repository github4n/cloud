package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
@Api(description = "产品关联功能组",value = "产品关联功能组")
@RequestMapping("/api/product/to/service/module")
public class ProductToServiceModuleController {

    private Logger log = LoggerFactory.getLogger(ProductToServiceModuleController.class);

    @Autowired
    private ProductToServiceModuleApi productToServiceModuleApi;

    @ApiOperation("增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody ProductToServiceModuleReq productToServiceModuleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(productToServiceModuleReq));
        productToServiceModuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        productToServiceModuleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        productToServiceModuleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        productToServiceModuleApi.save(productToServiceModuleReq);
        return CommonResponse.success();
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody ProductToServiceModuleReq productToServiceModuleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(productToServiceModuleReq));
        productToServiceModuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        productToServiceModuleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        productToServiceModuleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        productToServiceModuleApi.saveMore(productToServiceModuleReq);
        return CommonResponse.success();
    }


    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        productToServiceModuleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        productToServiceModuleApi.delete(ids);
        return CommonResponse.success();
    }
}

