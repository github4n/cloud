package com.iot.portal.web.controller;

import com.google.common.collect.Lists;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.portal.web.vo.PortalEventListResp;
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
@Api(value = "portal-功能定义-事件管理", description = "portal-功能定义-事件管理")
@RestController
@RequestMapping("/portal/event")
public class PortalEventController {

    @Autowired
    private ServiceEventApi serviceEventApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    /**
     * 获取功能下的所有事件
     *
     * @param serviceModuleId
     * @return
     * @author lucky
     * @date 2018/7/3 11:08
     */
    @ApiOperation("获取功能下的所有事件")
    @RequestMapping(value = "/findEventListByServiceModuleId", method = RequestMethod.GET)
    public CommonResponse<List<PortalEventListResp>> findEventListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<PortalEventListResp> returnList = Lists.newArrayList();
        List<ServiceModuleEventResp> eventList = serviceEventApi.findEventListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(eventList)) {
            return CommonResponse.success(returnList);
        }
        for (ServiceModuleEventResp event : eventList) {
            Long eventId = event.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByEventId(eventId);
            PortalEventListResp target = new PortalEventListResp();
            target.setEventInfo(event);
            target.setPropertyList(propertyRespList);
            returnList.add(target);
        }
        return CommonResponse.success(returnList);
    }


    /**
     * 获取事件明细
     *
     * @param eventId
     * @return
     * @author lucky
     * @date 2018/7/3 11:09
     */
    @ApiOperation("根据事件id-获取事件明细")
    @RequestMapping(value = "/getEventInfoByEventId", method = RequestMethod.GET)
    public CommonResponse<PortalEventListResp> getEventInfoByEventId(@RequestParam("eventId") Long eventId) {
        AssertUtils.notNull(eventId, "eventId.notnull");
        ServiceModuleEventResp event = serviceEventApi.getEventInfoByEventId(eventId);
        if (event == null) {
            return CommonResponse.success();
        }
        List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByEventId(eventId);
        PortalEventListResp target = new PortalEventListResp();
        target.setEventInfo(event);
        target.setPropertyList(propertyRespList);

        return CommonResponse.success(target);
    }

}
