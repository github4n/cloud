package com.iot.design.dict.controller;

import com.iot.design.dict.vo.resp.DictResp;
import com.iot.design.project.vo.resp.ProjectPageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/21
 */
@Api("字典祥光")
@FeignClient(value = "design-service")
@RequestMapping("dict")
public interface DictApi {

    @RequestMapping(method = RequestMethod.GET, value = "/getDict")
    @ApiOperation("获取字典")
    List<DictResp> getDict(@RequestParam(required = false,name = "tenantId") Long tenantId,@RequestParam(required = true,name = "dict") String dict);
}
