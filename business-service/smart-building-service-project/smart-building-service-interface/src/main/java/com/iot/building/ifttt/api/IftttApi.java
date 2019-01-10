package com.iot.building.ifttt.api;

import com.iot.building.ifttt.vo.req.*;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.ifttt.vo.res.SensorResp;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.common.helper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "联动接口")
@FeignClient(value = "building-control-service")
@RequestMapping(value = "/ifttt")
public interface IftttApi {

    @ApiOperation("分页查询联动设置")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<RuleResp> list(@RequestBody RuleListReq req);

    @ApiOperation("查询联动设置")
    @RequestMapping(value = "/listNoPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RuleResp> listNoPage(@RequestBody RuleListReq req);

    @ApiOperation("保存联动设置")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long save(@RequestBody SaveIftttReq req);

    @ApiOperation("保存联动信息")
    @RequestMapping(value = "/saveRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveRule(@RequestBody SaveRuleReq req);

    @ApiOperation("获取联动详细信息")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    RuleResp get(@RequestParam("tenantId") Long tenantId, @PathVariable("id") Long id);

    @ApiOperation("删除联动")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    List<Integer> delete(@RequestParam("tenantId") Long tenantId, @PathVariable("id") Long id);

    @ApiOperation("获取传感器列表")
    @RequestMapping(value = "/getSensorByParams", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SensorResp> getSensorByParams(@RequestBody GetSensorReq req);

    /*@ApiOperation("执行IFTTT")
    @RequestMapping(value = "/doActuator", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doActuator(@RequestParam("objectId") String objectId,
                    @RequestBody ExecIftttReq execIftttReq);*/

    ///////////////////////////////////中控使用///////////////////////////////////////////

    @ApiOperation("启用或停止联动监听")
    @RequestMapping(value = "/run/{id}", method = RequestMethod.GET)
    Boolean run(@RequestParam("tenantId") Long tenantId, 
    		    @PathVariable("id") Long id,
                @RequestParam("start") Boolean start);

    ////////////////////////////////smart-home使用/////////////////////////////////////////

    @ApiOperation("IFTTT 执行根据ruleId(定时使用)")
    @RequestMapping(value = "/execCronIftttByRule", method = RequestMethod.GET)
    void execCronIftttByRule(@RequestParam("ruleId") Long ruleId,
                             @RequestParam("tenantId") Long tenantId,
                             @RequestParam("type") String type);

    @ApiOperation("检测IFTTT名称")
    @RequestMapping(value = "/checkIftttName", method = RequestMethod.GET)
    Boolean checkName(@RequestParam("tenantId") Long tenantId, 
    		          @RequestParam(value = "ruleId", defaultValue = "0") Long ruleId,
                      @RequestParam("ruleName") String ruleName,
                      @RequestParam("userId") Long userId);

   /* @ApiOperation("根据ruleId添加天文定时")
    @RequestMapping(value = "/addAstroClockJob", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAstroClockJob(@RequestParam("tenantId") Long tenantId);*/

    @RequestMapping(value = "/getRuleListByName",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RuleResp> getRuleListByName(@RequestBody SaveIftttReq ruleVO);

    @RequestMapping(value = "/findTemplateListByName",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean findTemplateListByName(@RequestBody SaveIftttTemplateReq iftttReq);
}
