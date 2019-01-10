package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.ServiceEventApiFallbackFactory;
import com.iot.device.vo.req.ServiceModuleEventReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:53 2018/7/2
 * @Modify by:
 */
@Api(tags = "事件管理接口")
@FeignClient(value = "device-service", fallbackFactory = ServiceEventApiFallbackFactory.class)
@RequestMapping("/module/event")
public interface ServiceEventApi {

    @RequestMapping(value = "/findEventListByServiceModuleId", method = RequestMethod.GET)
    List<ServiceModuleEventResp> findEventListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    @RequestMapping(value = "/getEventInfoByEventId", method = RequestMethod.GET)
    ServiceModuleEventResp getEventInfoByEventId(@RequestParam("eventId") Long eventId);

    @RequestMapping(value = "/listEventInfoByEventIds", method = RequestMethod.GET)
    List<ServiceModuleEventResp> listEventInfoByEventIds(@RequestParam("eventIds") List<Long> eventIds);

    @ApiOperation("增加或者更新")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceModuleEventReq serviceModuleEventReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo list(@RequestBody ServiceModuleEventReq serviceModuleEventReq);

    @ApiOperation("根据moduleId获取数据")
    @RequestMapping(value = "/listByServiceModuleId" , method = RequestMethod.GET)
    List<ServiceModuleEventResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    /**
     * @despriction：查找支持联动配置的事件
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @ApiOperation(value = "查找支持联动配置的事件", notes = "查找支持联动配置的事件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceModuleIds", value = "模组id", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findSupportIftttEvents", method = RequestMethod.GET)
    ModuleSupportIftttResp findSupportIftttEvents(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId);

    /**
     * @despriction：修改模组事件联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @ApiOperation(value = "修改模组事件联动属性（portal使用）", notes = "修改模组事件联动属性（portal使用）")
    @RequestMapping(value = "/updateEventSupportIfttt" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateEventSupportIfttt(@RequestBody UpdateModuleSupportIftttReq req);
}
