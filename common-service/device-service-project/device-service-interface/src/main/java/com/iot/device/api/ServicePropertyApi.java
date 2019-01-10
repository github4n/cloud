package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.ServicePropertyApiFallbackFactory;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
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
@FeignClient(value = "device-service", fallbackFactory = ServicePropertyApiFallbackFactory.class)
@RequestMapping("/module/property")
public interface ServicePropertyApi {

    @RequestMapping(value = "/findPropertyListByServiceModuleId", method = RequestMethod.GET)
    List<ServiceModulePropertyResp> findPropertyListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);


    @RequestMapping(value = "/findPropertyListByServiceModuleIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ServiceModulePropertyResp> findPropertyListByServiceModuleIds(@RequestBody List<Long> serviceModuleIds);


    @RequestMapping(value = "/findPropertyListByActionId", method = RequestMethod.GET)
    List<ServiceModulePropertyResp> findPropertyListByActionId(@RequestParam("actionId") Long actionId);

    @RequestMapping(value = "/findPropertyListByEventId", method = RequestMethod.GET)
    List<ServiceModulePropertyResp> findPropertyListByEventId(@RequestParam("eventId") Long eventId);

    @RequestMapping(value = "/getPropertyInfoByPropertyId", method = RequestMethod.GET)
    ServiceModulePropertyResp getPropertyInfoByPropertyId(@RequestParam("propertyId") Long propertyId);

    @RequestMapping(value = "/listPropertyInfoByPropertyIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ServiceModulePropertyResp> listPropertyInfoByPropertyIds(@RequestBody List<Long> ids);

    @ApiOperation("增加或者更新")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq);

    @ApiOperation("批量增加或者更新")
    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Long> saveOrUpdateBatch(@RequestBody List<ServiceModulePropertyReq> serviceModulePropertyReqs);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo list(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq);

    @ApiOperation("根据moduleId获取数据")
    @RequestMapping(value = "listByServiceModuleId" , method = RequestMethod.GET)
    List<ServiceModulePropertyResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    @ApiOperation("根据actionModuleId获取数据")
    @RequestMapping(value = "listByActionModuleId" , method = RequestMethod.GET)
    List<ServiceModulePropertyResp> listByActionModuleId(@RequestParam("actionModuleId") Long actionModuleId);

    @ApiOperation("根据eventModuleId获取数据")
    @RequestMapping(value = "listByEventModuleId" , method = RequestMethod.GET)
    List<ServiceModulePropertyResp> listByEventModuleId(@RequestParam("eventModuleId") Long eventModuleId);

    /**
     * @despriction：查找支持联动配置的属性
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @ApiOperation(value = "查找支持联动配置的属性", notes = "查找支持联动配置的属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceModuleIds", value = "模组id", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findSupportIftttProperties", method = RequestMethod.GET)
    ModuleSupportIftttResp findSupportIftttProperties(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId);

    /**
     * @despriction：修改模组属性联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @ApiOperation(value = "修改模组属性联动属性（portal使用）", notes = "修改模组属性联动属性（portal使用）")
    @RequestMapping(value = "/updatePropertySupportIfttt" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    int updatePropertySupportIfttt(@RequestBody UpdateModuleSupportIftttReq req);
}
