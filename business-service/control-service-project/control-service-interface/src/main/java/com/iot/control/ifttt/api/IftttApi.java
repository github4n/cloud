package com.iot.control.ifttt.api;

import com.iot.common.helper.Page;
import com.iot.control.ifttt.vo.req.RuleListReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "联动接口")
@FeignClient(value = "control-service")
@RequestMapping(value = "/ifttt")
public interface IftttApi {

    @ApiOperation("分页查询联动设置")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<RuleResp> list(@RequestBody RuleListReq req);

    @ApiOperation("获取联动详细信息")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    RuleResp get(@PathVariable("id") Long id);
}
