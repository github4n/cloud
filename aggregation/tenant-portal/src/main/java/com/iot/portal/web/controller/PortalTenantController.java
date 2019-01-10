package com.iot.portal.web.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.resp.TenantInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:26 2018/7/9
 * @Modify by:
 */
@Api(value = "portal-租户管理", description = "portal-租户管理")
@RestController
@RequestMapping("/portal/tenant")
public class PortalTenantController {

    @Autowired
    private TenantApi tenantApi;

    @ApiOperation("获取租户信息")
    @RequestMapping(value = "/getTenantInfoByTenantId", method = RequestMethod.GET)
    public CommonResponse<TenantInfoResp> getTenantInfoByTenantId(@RequestParam("tenantId") Long tenantId) {
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(tenantId);
        return CommonResponse.success(tenantInfoResp);
    }
}
