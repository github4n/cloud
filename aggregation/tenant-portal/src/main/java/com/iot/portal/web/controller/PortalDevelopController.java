package com.iot.portal.web.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.common.helper.Page;
import com.iot.device.api.DevelopInfoApi;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:12 2018/6/29
 * @Modify by:
 */
@Api(value = "portal-功能组管理", description = "portal-功能组管理")
@RestController
@RequestMapping("/portal/develop")
public class PortalDevelopController {

    @Autowired
    private DevelopInfoApi developInfoApi;

    @Autowired
    private ProductApi productApi;

    @ApiOperation("添加或修改功能组")
    @RequestMapping(value = "/addOrUpdateDevelopInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addOrUpdateDevelopInfo(@RequestBody AddDevelopInfoReq infoReq) {
        AssertUtils.notNull(infoReq, "add.develop.info.notnull");
        AssertUtils.notEmpty(infoReq.getCode(), "develop.info.code.notnull");
        infoReq.setTenantId(SaaSContextHolder.currentTenantId());
        infoReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        developInfoApi.addOrUpdateDevelopInfo(infoReq);

        return CommonResponse.success();
    }

    @ApiOperation("根据产品添加功能组")
    @RequestMapping(value = "/addOrUpdateDevelopInfoByProductId", method = RequestMethod.GET)
    public CommonResponse addOrUpdateDevelopInfoByProductId(@RequestParam("enterpriseDevelopId") Long enterpriseDevelopId, @RequestParam("productId") Long productId) {
        AssertUtils.notNull(enterpriseDevelopId, "enterpriseDevelopId.notnull");
        AssertUtils.notNull(productId, "productId.notnull");
        productApi.updateDevelopInfoByProductId(enterpriseDevelopId, productId);
        return CommonResponse.success();
    }

    @ApiOperation("获取所有功能组列表")
    @RequestMapping(value = "/findDevelopInfoListAll", method = RequestMethod.GET)
    public CommonResponse<List<DevelopInfoListResp>> findDevelopInfoListAll() {
        List<DevelopInfoListResp> infoListRespList = developInfoApi.findDevelopInfoListAll();
        return CommonResponse.success(infoListRespList);
    }

    @ApiOperation("分页获取功能组列表")
    @RequestMapping(value = "/findDevelopInfoPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Page<DevelopInfoListResp>> findDevelopInfoPage(@RequestBody DevelopInfoPageReq pageReq) {
        Page<DevelopInfoListResp> infoListRespPage = developInfoApi.findDevelopInfoPage(pageReq);
        return CommonResponse.success(infoListRespPage);
    }
}
