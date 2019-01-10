package com.iot.device.api;

import com.iot.device.api.fallback.ModuleEventToPropertyApiFallbackFactory;
import com.iot.device.api.fallback.ServiceToActionApiFallbackFactory;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.req.ServiceToActionReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * @Author: zhangyue
 * @Descrpiton:
 * @Modify by:
 */
@Api(tags = "功能事件对属性管理接口")
@FeignClient(value = "device-service", fallbackFactory = ServiceToActionApiFallbackFactory.class)
@RequestMapping("/serviceToAction")
public interface ServiceToActionApi {

    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody ServiceToActionReq serviceToActionReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody ServiceToActionReq serviceToActionReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("更新")
    @RequestMapping(value = "/update" , method = RequestMethod.GET)
    void update(@RequestParam("id") Long id,@RequestParam("status") Integer status);
}
