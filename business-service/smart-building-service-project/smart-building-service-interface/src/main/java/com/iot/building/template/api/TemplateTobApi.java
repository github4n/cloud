package com.iot.building.template.api;

import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.template.vo.req.*;
import com.iot.building.template.vo.rsp.*;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("情景/ifttt的模板接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/template")
public interface TemplateTobApi {

    /**
     * 查询产品类型目标值
     *
     * @param templateId
     * @return
     */
    @ApiOperation("查询产品类型目标值")
    @RequestMapping(value = "/findDeviceTarValueList", method = RequestMethod.GET)
    public List<DeviceTarValueResp> findDeviceTarValueList(@RequestParam("templateId") Long templateId);

    /**
     * 查询单个情景模板
     *
     * @param templateId
     * @return
     */
    @ApiOperation("查询单个情景模板")
    @RequestMapping(value = "/getSceneTemplate", method = RequestMethod.GET)
    public SceneTemplateResp getSceneTemplate(@RequestParam("templateId") Long templateId) ;

    /**
     * 删除情景
     *
     * @param templateId
     */
    @ApiOperation("删除情景")
    @RequestMapping(value = "/deleteSceneTemplate", method = RequestMethod.GET)
    public void deleteSceneTemplate(@RequestParam("templateId") Long templateId) throws BusinessException;


    /**
     * 根据 模板名、模板类型 分页获取 TemplateVO列表
     *
     * @param remplateReq
     * @return
     */
    @ApiOperation("查询模板")
    @RequestMapping(value = "/findTemplateList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<TemplateResp> findTemplateList(@RequestBody TemplateReq remplateReq);


    @ApiOperation("根据 情景模板 创建对应的 情景(scene、sceneDetail)")
    @RequestMapping(value = "/createSceneFromTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long createSceneFromTemplate(@RequestBody CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException;

    @ApiOperation("根据 情景模板 删除对应的 情景(scene、sceneDetail)")
    @RequestMapping(value = "/delSceneFromTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delSceneFromTemplate(@RequestBody SpaceTemplateReq spaceTemplateReq) throws BusinessException;

    @ApiOperation("保存 模板主表、情景模板(2B 业务)")
    @RequestMapping(value = "/buildSceneTemplate2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void buildSceneTemplate2B(@RequestBody BuildTemplateReq buildTemplateReq) throws Exception;

    @ApiOperation("查询 空间情景模板 列表")
    @RequestMapping(value = "/findSceneSpaceTemplateList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<TemplateResp> findSceneSpaceTemplateList(@RequestBody TemplateReq templateReq);


    @ApiOperation("查询 Scene模板 列表")
    @RequestMapping(value = "/findSceneTemplateList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<TemplateResp> findSceneTemplateList(@RequestBody TemplateReq templateReq);

    ////////////////////////////////////////////////IFTTT模板///////////////////////////////////////////////////////////

    @ApiOperation("保存联动模板")
    @RequestMapping(value = "/saveIftttTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveIftttTemplate(@RequestBody SaveIftttTemplateReq req);

    @ApiOperation("删除联动模板")
    @RequestMapping(value = "/deleteIftttTemplate", method = RequestMethod.GET)
    void deleteIftttTemplate(@RequestParam("templateId") Long templateId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("查询联动模板列表根据套包主键")
    @RequestMapping(value = "/getIftttTemplateList", method = RequestMethod.GET)
    List<RuleResp> getIftttTemplateList(@RequestParam("templateId") Long templateId);

    @ApiOperation("查询单个联动模板")
    @RequestMapping(value = "/getIftttTemplateByRuleId", method = RequestMethod.GET)
    RuleResp getIftttTemplateByRuleId(@RequestParam("ruleId") Long ruleId);

    @ApiOperation("生成联动规则(针对2B)")
    @RequestMapping(value = "/buildIfttt2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long buildIfttt2B(@RequestBody BuildIftttReq req);

    @ApiOperation("获取模板")
    @RequestMapping(value = "/getTempaltes", method = RequestMethod.GET)
    public TemplateVoResp getTempaltes(@RequestParam("productModel") String productModel,@RequestParam("orgId") Long orgId);

  /*  @ApiOperation("根据templateId、productId获取TemplateDetail")
    @RequestMapping(value = "/getTempaltes", method = RequestMethod.GET)
    TemplateDetailResp getByTemplateIdAndProductId(@RequestParam("templateId") Long templateId, @RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId);
*/
}
