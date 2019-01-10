package com.iot.product.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.BusinessExceptionEnum;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.req.ProductInfoResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.UserDeviceProductResp;
import com.iot.file.api.FileApi;
import com.iot.product.service.ProductService;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.AppProductResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "产品接口")
@RestController
@RequestMapping("/deviceManage")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private FileApi fileApi;


    @RequestMapping(value = "/getProductList", method = RequestMethod.GET)
    @ApiOperation(value = "获取产品list")
    public CommonResponse getProductList(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize,
                                         @RequestParam("userId") Long userId) {
        LOGGER.debug("getProductList({}, {} ,{})", offset, pageSize, userId);
        CommonResponse<Map<String, Object>> result = null;
        try {
            Page<UserDeviceProductResp> productPage = productService.getProductPageByUserId(offset, pageSize, userId);
            Map<String, Object> paramMap = new HashMap<>();
            List<String> strList = new ArrayList<>();
            if (productPage.getResult() != null) {
                for (UserDeviceProductResp product : productPage.getResult()) {
                    if (product.getProductId() != null) {
                        strList.add(product.getProductId().toString());
                    }
                }
            }
            paramMap.put("totalCount ", productPage.getTotal());
            paramMap.put("productId", strList);
            result = CommonResponse.success(paramMap);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }

        return result;
    }

    @RequestMapping(value = "/getProductInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取产品信息")
    public CommonResponse getProductInfo(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        ProductInfoResp productInfo = productApi.getProductInfoByProductId(productId);
        return CommonResponse.success(productInfo);

    }

    @ApiOperation("获取关联产品")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/productByAppIdAndTenantId", method = RequestMethod.GET)
    public CommonResponse productByAppIdAndTenantId(@RequestParam("appId") Long appId) {
        Map map = appApi.appExecPackageByProduct(appId, SaaSContextHolder.currentTenantId());
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", map);
    }

}
