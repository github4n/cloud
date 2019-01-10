package com.iot.building.ifttt.api;

import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.common.helper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ifttt接口--TOB使用")
@FeignClient(value = "building-control-service")
@RequestMapping(value = "/iftttTob")
public interface AutoTobApi {

   // @ApiOperation("获取联动列表")
   // @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
   // List<AutoListResp> list();

    /**
     * 综合查找build_tob_rule集合
     * @param ruleVO
     * @return
     */
    @ApiOperation("综合查找build_tob_rule集合")
    @RequestMapping(value = "/getRuleList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RuleResp> getRuleList(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("保存/修改ifttt记录（保存/修改build_tob_rule表）")
    @RequestMapping(value = "/saveBuildTobRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveBuildTobRule(@RequestBody SaveIftttReq ruleVO, @RequestParam(value = "appletId") Long appletId);

    @ApiOperation("保存/联动信息（sensor、actuator、or或and）")
    @RequestMapping(value = "/saveAuto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveAuto(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("通过名字查找build_tob_rule集合")
    @RequestMapping(value = "/getRuleListByName",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RuleResp> getRuleListByName(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("保存点、线，表为tob_trigger")
    @RequestMapping(value = "/saveTobTrigger", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveTobTrigger(@RequestBody SaveIftttReq ruleVO,@RequestParam(value = "buildToRuleId") Long buildToRuleId);

    @ApiOperation("获取单个 build_tob_rule 的详情")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    RuleResp get(@RequestParam("orgId") Long orgId,@RequestParam("tenantId") Long tenantId, @PathVariable("id") Long id);

    @ApiOperation("删除ifttt")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    List<Integer> delete(@RequestParam("orgId") Long orgId,@RequestParam("tenantId") Long tenantId, @PathVariable("id") Long id,@RequestParam("flag") boolean flag);

    @ApiOperation("列表  表build_tob_rule")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<RuleResp> list(@RequestBody RuleListReq req);

    @ApiOperation("启用或停止联动监听")
    @RequestMapping(value = "/run/{id}", method = RequestMethod.GET)
    Boolean run(@RequestParam("orgId") Long orgId,@RequestParam("tenantId") Long tenantId, 
    		    @PathVariable("id") Long id,
                @RequestParam("start") Boolean start);

    @ApiOperation("保存relation")
    @RequestMapping(value = "/saveTobRelation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveTobRelation(@RequestBody SaveIftttReq ruleVO,@RequestParam(value = "buildToRuleId")  Long buildToRuleId);

    @ApiOperation("通过名字查找tob_trigger集合")
    @RequestMapping(value = "/getTriggerTobListByDeviceId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<TriggerVo> getTriggerTobListByDeviceId(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("启用或停止联动监听")
    @RequestMapping(value = "/runApplet/{id}", method = RequestMethod.GET)
    void runApplet(@RequestParam("orgId") Long orgId,@RequestParam("tenantId") Long tenantId,
                      @PathVariable("id") Long id,
                      @RequestParam("start") Boolean start);

    @ApiOperation("是否跨网关")
    @RequestMapping(value = "/isSingleGateway", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean isSingleGateway(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("创建下发到网关")
    @RequestMapping(value = "/createLowerHair", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void createLowerHair(@RequestBody SaveIftttReq ruleVO);

    @ApiOperation("删除下发到网关")
    @RequestMapping(value = "/deleteLowerHair/{id}", method = RequestMethod.GET)
    void deleteLowerHair(@PathVariable("id") Long id,@RequestParam("clientId") String clientId);

    @ApiOperation("网关开启/关闭联动")
    @RequestMapping(value = "/runLowerHair/{id}", method = RequestMethod.GET)
    void runLowerHair(@PathVariable("id") Long id,@RequestParam("clientId") String clientId,@RequestParam("start") Boolean start);

    @ApiOperation("删除a")
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    void deleteAll(@RequestParam("orgId") Long orgId,@RequestParam("appletId") Long appletId,@RequestParam("buildToRuleId")  Long buildToRuleId,@RequestParam("tenantId")  Long tenantId);
}
