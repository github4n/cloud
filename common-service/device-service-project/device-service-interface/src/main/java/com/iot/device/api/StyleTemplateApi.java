package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.ServicePropertyApiFallbackFactory;
import com.iot.device.api.fallback.StyleTemplateApiFallbackFactory;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;
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


@Api("样式模板接口")
@FeignClient(value = "device-service" , fallbackFactory = StyleTemplateApiFallbackFactory.class)
@RequestMapping("/style/template")
public interface StyleTemplateApi {

    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody StyleTemplateReq styleTemplateReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("获取")
    @RequestMapping(value = "/list", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo<StyleTemplateResp> list(@RequestBody StyleTemplateReq styleTemplateReq);

    @ApiOperation("根据deviceTypeId获取模板")
    @RequestMapping(value = "/listByDeviceTypeId" , method = RequestMethod.GET)
    List<StyleTemplateResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId);

    @ApiOperation("根据moduleStyleId获取模板")
    @RequestMapping(value = "/listByModuleStyleId" , method = RequestMethod.GET)
    List<StyleTemplateResp> listByModuleStyleId(@RequestParam("moduleStyleId") Long moduleStyleId);

    @ApiOperation("根据productId获取模板")
    @RequestMapping(value = "/listByProductId" , method = RequestMethod.GET)
    List<StyleTemplateResp> listByProductId(@RequestParam("productId") Long productId);

    @ApiOperation("根据id获取模块对象")
    @RequestMapping(value = "/infoById" , method = RequestMethod.GET)
    StyleTemplateResp infoById(@RequestParam("id") Long id);

}
