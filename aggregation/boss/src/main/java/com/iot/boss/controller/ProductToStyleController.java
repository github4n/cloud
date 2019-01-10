package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ProductToStyleApi;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "产品关联样式",value = "产品关联样式")
@RequestMapping("/api/product/style")

public class ProductToStyleController {

    private Logger log = LoggerFactory.getLogger(ProductToStyleController.class);

    @Autowired
    private ProductToStyleApi productToStyleApi;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ProductToStyleReq productToStyleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(productToStyleReq));
        productToStyleReq.setTenantId(SaaSContextHolder.currentTenantId());
        productToStyleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        productToStyleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,productToStyleApi.saveOrUpdate(productToStyleReq));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public CommonResponse delete(@PathVariable(value = "id",required = true) Long id) {
        log.debug("根据id删除",id);
        productToStyleApi.delete(id);
        return CommonResponse.success();
    }

    @ApiOperation("根据productId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list/{productId}", method = RequestMethod.GET)
    public CommonResponse list(@PathVariable(value = "productId",required = true) Long productId) {
        log.debug("根据productId获取列表",productId);
        List list = productToStyleApi.list(productId);
        return CommonResponse.success(list);
    }
}
