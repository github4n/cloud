package com.iot.portal.web.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.device.api.DeviceCatalogApi;
import com.iot.device.vo.rsp.DeviceCatalogListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:27 2018/6/29
 * @Modify by:
 */
@Api(value = "portal-产品目录管理", description = "portal-产品目录管理")
@RestController
@RequestMapping("/portal/catalog")
public class PortalCatalogController {

    @Autowired
    private DeviceCatalogApi deviceCatalogApi;

    @ApiOperation("获取所有的目录列表")
    @RequestMapping(value = "/findAllCatalogList", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<List<DeviceCatalogListResp>> findAllCatalogList() {
        List<DeviceCatalogListResp> catalogListRespList = deviceCatalogApi.findAllCatalogList();
        return CommonResponse.success(catalogListRespList);

    }
}
