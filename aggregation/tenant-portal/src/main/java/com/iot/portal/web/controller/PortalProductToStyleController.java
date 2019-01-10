package com.iot.portal.web.controller;


import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ProductToStyleApi;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.rsp.ProductToStyleResp;
import com.iot.portal.service.FileService;
import com.iot.portal.web.vo.FileResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "portal-产品样式管理", description = "portal-产品样式管理")
@RestController
@RequestMapping("/portal/product/style")
public class PortalProductToStyleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PortalProductToStyleController.class);

    @Autowired
    private ProductToStyleApi productToStyleApi;

    @Autowired
    private FileService fileService;

    @ApiOperation("添加或者修改")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveOrUpdate(@RequestBody ProductToStyleReq productToStyleReq){
        AssertUtils.notNull(productToStyleReq, "productToStyleReq.notnull");
        productToStyleReq.setTenantId(SaaSContextHolder.currentTenantId());
        productToStyleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        productToStyleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        Long result = productToStyleApi.saveOrUpdate(productToStyleReq);
        return CommonResponse.success(result);
    }

    @ApiOperation("删除")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public CommonResponse delete(@RequestParam("id") Long id){
        AssertUtils.notNull(id, "id.notnull");
        productToStyleApi.delete(id);
        return CommonResponse.success();
    }

    @ApiOperation("根据productId获取")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResponse list(@RequestParam("productId") Long productId){
        AssertUtils.notNull(productId, "productId.notnull");
        List<ProductToStyleResp> list = productToStyleApi.list(productId);
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                try {
                    FileResp fileResp = fileService.getUrl(m.getImg());
                    m.setImg(fileResp.getUrl());
                } catch (Exception e) {
                    LOGGER.warn("get product style img error", e);
                }
            }
        });
        return CommonResponse.success(list);
    }

}
