package com.iot.device.api;

import com.iot.device.api.fallback.ServiceModuleStyleApiFallbackFactory;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
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

@Api("服务样式接口")
@FeignClient(value = "device-service" , fallbackFactory = ServiceModuleStyleApiFallbackFactory.class)
@RequestMapping("/service/module/style")
public interface ServiceModuleStyleApi {

    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceModuleStyleReq serviceModuleStyleReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);


    @ApiOperation("跟据serviceModuleId获取style")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ServiceModuleStyleResp> list(@RequestBody ArrayList<Long> serviceModuleId);

    @ApiOperation("跟据serviceModuleId获取style")
    @RequestMapping(value = "/listByStyleTemplateId" , method = RequestMethod.GET)
    List<ServiceModuleStyleResp> listByStyleTemplateId(@RequestParam("styleTemplateId") Long styleTemplateId);




}
