package com.iot.device.api;


import com.iot.device.api.fallback.ServicePropertyApiFallbackFactory;
import com.iot.device.api.fallback.ServiceStyleToTemplateApiFallbackFactory;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
import com.iot.device.vo.rsp.ServiceStyleToTemplateResp;
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

@Api("服务样式模板接口")
@FeignClient(value = "device-service" , fallbackFactory = ServiceStyleToTemplateApiFallbackFactory.class)
@RequestMapping("/service/template/style")
public interface ServiceStyleToTemplateApi {

    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq);


    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("根据moduleStyleId查询")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ServiceStyleToTemplateResp> list(@RequestBody ArrayList<Long> moduleStyleId);

}
