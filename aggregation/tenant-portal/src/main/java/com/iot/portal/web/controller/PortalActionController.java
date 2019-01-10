package com.iot.portal.web.controller;

import com.google.common.collect.Lists;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.portal.web.utils.ActionPropertyUtils;
import com.iot.portal.web.vo.PortalActionListResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
@Api(value = "portal-功能定义-方法管理", description = "portal-功能定义-方法管理")
@RestController
@RequestMapping("/portal/action")
public class PortalActionController {

    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    /**
     * 获取功能下的所有方法
     *
     * @param serviceModuleId
     * @return
     * @author lucky
     * @date 2018/7/3 11:08
     */
    @ApiOperation("根据模组id-获取所有的方法列表")
    @RequestMapping(value = "/findActionListByServiceModuleId", method = RequestMethod.GET)
    public CommonResponse<List<PortalActionListResp>> findActionListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<PortalActionListResp> returnList = Lists.newArrayList();
        List<ServiceModuleActionResp> actionRespList = serviceActionApi.findActionListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(actionRespList)) {
            return CommonResponse.success(returnList);
        }
        for (ServiceModuleActionResp action : actionRespList) {
            PortalActionListResp target = new PortalActionListResp();
            Long actionId = action.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByActionId(actionId);
            target.setActionInfo(action);
            target.setParamPropertyList(ActionPropertyUtils.parseParamProperties(propertyRespList));//请求参数
            target.setReturnPropertyList(ActionPropertyUtils.parseReturnProperties(propertyRespList));//返回参数
            returnList.add(target);
        }
        return CommonResponse.success(returnList);
    }


    /**
     * 获取方法明细
     *
     * @param actionId
     * @return
     * @author lucky
     * @date 2018/7/3 11:08
     */
    @ApiOperation("根据方法id-获取详细")
    @RequestMapping(value = "/getActionInfoByActionId", method = RequestMethod.GET)
    public CommonResponse<PortalActionListResp> getActionInfoByActionId(@RequestParam("actionId") Long actionId) {
        AssertUtils.notNull(actionId, "actionId.notnull");
        ServiceModuleActionResp action = serviceActionApi.getActionInfoByActionId(actionId);
        if (action == null) {
            return CommonResponse.success();
        }
        PortalActionListResp target = new PortalActionListResp();
        List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByActionId(actionId);
        target.setActionInfo(action);
        target.setParamPropertyList(ActionPropertyUtils.parseParamProperties(propertyRespList));
        target.setParamPropertyList(ActionPropertyUtils.parseParamProperties(propertyRespList));
        return CommonResponse.success(target);
    }

}
