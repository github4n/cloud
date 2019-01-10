package com.iot.shcs.template.api;

import com.iot.shcs.template.vo.TemplateRuleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 描述：套包接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/12/20 16:32
 */

@Api(tags = "套包接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/smarthome/package")
public interface PackageApi {

    @ApiOperation("根据model获取套包规则")
    @RequestMapping(value = "/getRuleByModel", method = RequestMethod.GET)
    List<TemplateRuleResp> getRuleByModel(@RequestParam("model") String model);
}
