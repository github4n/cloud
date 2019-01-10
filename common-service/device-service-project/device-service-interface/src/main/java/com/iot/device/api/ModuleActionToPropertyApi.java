package com.iot.device.api;

import com.iot.device.api.fallback.ModuleActionToPropertyApiFallbackFactory;
import com.iot.device.api.fallback.ServiceModuleApiFallbackFactory;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import io.swagger.annotations.Api;
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
 * @Date: 8:58 2018/7/2
 * @Modify by:
 */
@Api(tags = "功能方法对属性管理接口")
@FeignClient(value = "device-service", fallbackFactory = ModuleActionToPropertyApiFallbackFactory.class)
@RequestMapping("/moduleActionToProperty")
public interface ModuleActionToPropertyApi {

    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);


    @ApiOperation("根据ModuleIdAndPropertyId获取")
    @RequestMapping(value = "/listByModuleActionIdAndModulePropertyId" , method = RequestMethod.GET)
    List<ModuleActionToPropertyRsp> listByModuleActionIdAndModulePropertyId(@RequestParam("moduleActionId") Long moduleActionId, @RequestParam("modulePropertyId") Long modulePropertyId);


}
