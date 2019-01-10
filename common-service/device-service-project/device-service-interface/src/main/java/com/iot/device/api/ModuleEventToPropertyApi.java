package com.iot.device.api;

import com.iot.device.api.fallback.ModuleActionToPropertyApiFallbackFactory;
import com.iot.device.api.fallback.ModuleEventToPropertyApiFallbackFactory;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
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
 * @Author: zhangyue
 * @Descrpiton:
 * @Modify by:
 */
@Api(tags = "功能事件对属性管理接口")
@FeignClient(value = "device-service", fallbackFactory = ModuleEventToPropertyApiFallbackFactory.class)
@RequestMapping("/moduleEventToProperty")
public interface ModuleEventToPropertyApi {

    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody ModuleEventToPropertyReq moduleEventToPropertyReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody ModuleEventToPropertyReq moduleEventToPropertyReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);
}
