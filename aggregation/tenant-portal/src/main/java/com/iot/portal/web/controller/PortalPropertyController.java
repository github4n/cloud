package com.iot.portal.web.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 18:09 2018/7/2
 * @Modify by:
 */
@Api(value = "portal-功能定义-属性管理", description = "portal-功能定义-属性管理")
@RestController
@RequestMapping("/portal/property")
public class PortalPropertyController {

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ServiceModuleApi serviceModuleApi;

    /**
     * 根据功能组id 获取属性集合
     *
     * @param serviceModuleId
     * @return
     * @author lucky
     * @date 2018/7/3 10:53
     */
    @ApiOperation("根据功能组id 获取属性集合")
    @RequestMapping(value = "/findPropertyListByServiceModuleId", method = RequestMethod.GET)
    public CommonResponse<List<ServiceModulePropertyResp>> findPropertyListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByServiceModuleId(serviceModuleId);
        return CommonResponse.success(propertyRespList);
    }

    /**
     * 获取属性明细
     *
     * @param propertyId
     * @return
     * @author lucky
     * @date 2018/7/3 11:09
     */
    @ApiOperation("获取属性明细")
    @RequestMapping(value = "/getPropertyInfoByPropertyId", method = RequestMethod.GET)
    public CommonResponse<ServiceModulePropertyResp> getPropertyInfoByPropertyId(@RequestParam("propertyId") Long propertyId) {
        AssertUtils.notNull(propertyId, "propertyId.notnull");
        ServiceModulePropertyResp propertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyId);
        return CommonResponse.success(propertyResp);
    }
}
