package com.iot.control.packagemanager.api;

import com.iot.control.packagemanager.vo.req.SearchTemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.TemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "模板规则接口")
@FeignClient(value = "control-service")
@RequestMapping("/templateRule")
public interface TemplateRuleApi {
    @ApiOperation(value = "添加模板规则")
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean insert(@RequestBody TemplateRuleReq req);

    @ApiOperation(value = "批量添加模板规则")
    @RequestMapping(value = "/batchInsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean batchInsert(@RequestBody List<TemplateRuleReq> req);

    @ApiOperation(value = "根据id批量删除")
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    boolean deleteByIds(@RequestParam("ids") List<Long> ids);

    @ApiOperation(value = "根据id删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    boolean deleteById(@RequestParam("id") Long id);

    @ApiOperation(value = "根据条件获取模板规则数据(条件)")
    @RequestMapping(value = "/getTemplateRuleBy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<TemplateRuleResp> getTemplateRuleBy(@RequestBody SearchTemplateRuleReq req);

    @ApiOperation(value = "根据套包id和租户id，获取当前租户下套包绑定的策略总数量")
    @RequestMapping(value = "/getTotalNumber", method = RequestMethod.POST)
    int getTotalNumber(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId);

    /**
     *@description 修改策略的json、ruleName、updateTime
     *@author wucheng
     *@params [t]
     *@create 2018/12/11 15:41
     *@return int
     */
    @ApiOperation(value = "根据套包id和租户id，获取当前租户下套包绑定的策略总数量")
    @RequestMapping(value = "/updateTemplateRuleById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateTemplateRuleById(@RequestBody UpdateTemplateRuleReq t);

    /**
      * @despriction：通过id获取策略信息
      * @author  yeshiyuan
      * @created 2018/12/11 13:46
      */
    @ApiOperation(value = "通过id获取策略信息")
    @RequestMapping(value = "/getRuleInfoById", method = RequestMethod.GET)
    TemplateRuleResp getRuleInfoById(@RequestParam("id") Long id);

    /**
     *@description 通过id和租户id获取策略信息
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/12 16:15
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleResp
     */
    @ApiOperation(value = "通过id和租户id获取策略信息")
    @RequestMapping(value = "/getTemplateRuleById", method = RequestMethod.GET)
    TemplateRuleResp getTemplateRuleById(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId);
}