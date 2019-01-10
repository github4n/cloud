package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.ServiceActionApiFallbackFactory;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.UpdateActionInfoReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
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
 * @Date: 19:52 2018/7/2
 * @Modify by:
 */
@Api(tags = "方法管理接口")
@FeignClient(value = "device-service", fallbackFactory = ServiceActionApiFallbackFactory.class)
@RequestMapping("/module/action")
public interface ServiceActionApi {

    @RequestMapping(value = "/findActionListByServiceModuleId", method = RequestMethod.GET)
    List<ServiceModuleActionResp> findActionListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    @RequestMapping(value = "/getActionInfoByActionId", method = RequestMethod.GET)
    ServiceModuleActionResp getActionInfoByActionId(@RequestParam("actionId") Long actionId);

    @RequestMapping(value = "/listActionInfoByActionIds", method = RequestMethod.GET)
    List<ServiceModuleActionResp> listActionInfoByActionIds(@RequestParam("actionIds") List<Long> actionIds);

    @RequestMapping(value = "/updateActionInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateActionInfo(@RequestBody UpdateActionInfoReq actionInfoReq);


    @ApiOperation("增加或更新")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceModuleActionReq serviceModuleActionReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("分页获取全部数据")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo list(@RequestBody ServiceModuleActionReq serviceModuleActionReq);

    @ApiOperation("根据moduleId获取数据")
    @RequestMapping(value = "/listByServiceModuleId" , method = RequestMethod.GET)
    List<ServiceModuleActionResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    /**
     * @despriction：查找支持联动配置的方法
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @ApiOperation(value = "查找支持联动配置的方法", notes = "查找支持联动配置的方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceModuleIds", value = "模组id", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findSupportIftttActions", method = RequestMethod.GET)
    ModuleSupportIftttResp findSupportIftttActions(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：修改模组事件联动属性（portal使用）
      * @author  yeshiyuan
      * @created 2018/10/23 17:19
      * @return
      */
    @ApiOperation(value = "修改模组事件联动属性（portal使用）", notes = "修改模组事件联动属性（portal使用）")
    @RequestMapping(value = "/updateActionSupportIfttt" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateActionSupportIfttt(@RequestBody UpdateModuleSupportIftttReq req);
}
