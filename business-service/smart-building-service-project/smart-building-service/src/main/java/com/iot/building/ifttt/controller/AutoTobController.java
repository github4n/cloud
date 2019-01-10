package com.iot.building.ifttt.controller;

import com.google.common.collect.Lists;
import com.iot.building.ifttt.service.IAutoTobService;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.service.SceneService;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.vo.SetEnableReq;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.iot.common.helper.Page;

@RestController
public class AutoTobController implements AutoTobApi {
    private final Logger logger = LoggerFactory.getLogger(AutoTobController.class);

    @Autowired
    private IAutoTobService autoTobService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private IftttApi iftttApi;

    @Override
    public List<RuleResp> getRuleList(@RequestBody SaveIftttReq ruleVO) {
        List<RuleResp> list = autoTobService.getRuleList(ruleVO);
        return list;
    }

    /**
     * 保存/修改ifttt记录（保存/修改build_tob_rule表）
     *
     * @return
     */
    @Override
    public Long saveBuildTobRule(@RequestBody SaveIftttReq ruleVO,@RequestParam(value = "appletId") Long appletId) {
        ruleVO.setAppletId(appletId);
        Long id = autoTobService.saveBuildTobRule(ruleVO);
        return id;
    }

    /**
     * 保存/联动信息（sensor、actuator、or或and）
     *
     * @param ruleVO
     * @return
     */
    @Override
    public Long saveAuto(@RequestBody SaveIftttReq ruleVO) {
        Long appletId = autoTobService.saveAuto(ruleVO);
        return appletId;
    }

    /**
     * 通过名字查找build_tob_rule集合
     * @param ruleVO
     * @return
     */
    @Override
    public List<RuleResp> getRuleListByName(@RequestBody SaveIftttReq ruleVO) {
        List<RuleResp> list = autoTobService.getRuleListByName(ruleVO);
        return list;
    }

    /**
     * 保存点、线，表为tob_trigger
     * @param ruleVO
     * @param buildToRuleId
     * @return
     */
    @Override
    public Long saveTobTrigger(@RequestBody SaveIftttReq ruleVO,@RequestParam(value = "buildToRuleId") Long buildToRuleId) {
        //ruleVO.setAppletId(appletId);
        ruleVO.setId(buildToRuleId);
        Long id = autoTobService.saveTobTrigger(ruleVO);
        return id;
    }

    /**
     * 获取单个 build_tob_rule 的详情
     * @param id
     * @return
     */
    @Override
    public RuleResp get(@RequestParam("orgId")Long orgId,Long tenantId,@PathVariable("id") Long id) {
        RuleResp ruleResp =  autoTobService.get(tenantId,orgId,id);
        return ruleResp;
    }

    /**
     * 删除ifttt
     * @param id
     * @return
     */
    @Override
    public List<Integer> delete(Long orgId,Long tenantId,@PathVariable("id") Long id,@RequestParam("flag") boolean flag) {
        return autoTobService.delete(tenantId,orgId,id,flag);
    }

    /**
     * 列表  表build_tob_rule
     * @param req
     * @return
     */
    @Override
    public Page<RuleResp> list(@RequestBody RuleListReq req) {
        return autoTobService.list(req);
    }

    /**
     * 启用或停止联动监听
     * @param id
     * @param start
     * @return
     */
    @Override
    public Boolean run(Long orgid,Long tenantId,@PathVariable("id") Long id,@RequestParam("start") Boolean start) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        boolean flag = false;
        flag = autoTobService.run(id,start);
        return flag;
    }

    @Override
    public void runApplet(Long orgid,Long tenantId,@PathVariable("id") Long id,@RequestParam("start") Boolean start) {
        SetEnableReq req = new SetEnableReq();
        req.setId(id);
        if(start == true){
            req.setStatus("on");
        }else if(start == false){
            req.setStatus("off");
        }
        iftttApi.setEnable(req);
    }

    /**
     * 是否跨网关
     * @param ruleVO
     * @return
     */
    @Override
    public boolean isSingleGateway(@RequestBody SaveIftttReq ruleVO) {
        boolean isSingleGatewayFlag =sceneService.isSingleGateway(ruleVO.getDeviceIds(),ruleVO.getSceneIds(),ruleVO.getTenantId());
        return isSingleGatewayFlag;
    }

    /**
     * 创建下发到网关
     * @param ruleVO
     */
    @Override
    public void createLowerHair(@RequestBody SaveIftttReq ruleVO) {
        autoTobService.createLowerHair(ruleVO);
    }

    /**
     * 删除下发到网关
     * @param id
     */
    @Override
    public void deleteLowerHair(@PathVariable("id") Long id,@RequestParam("clientId") String clientId) {
        autoTobService.deleteLowerHair(id,clientId);
    }

    /**
     * 网关开启/关闭联动
     * @param id
     * @param clientId
     * @param start
     */
    @Override
    public void runLowerHair(@PathVariable("id") Long id,@RequestParam("clientId") String clientId,@RequestParam("start") Boolean start) {
        autoTobService.runLowerHair(id,clientId,start);
    }

    @Override
    public void deleteAll(@RequestParam("orgId")Long orgId,@RequestParam("appletId") Long appletId,@RequestParam("buildToRuleId") Long buildToRuleId,@RequestParam("tenantId") Long tenantId) {
        autoTobService.deleteAll(orgId,appletId,buildToRuleId,tenantId);
    }


    /**
     * 保存relation
     * @param ruleVO
     * @param buildToRuleId
     * @return
     */
    @Override
    public void saveTobRelation(@RequestBody SaveIftttReq ruleVO,@RequestParam(value = "buildToRuleId") Long buildToRuleId) {
        ruleVO.setId(buildToRuleId);
        autoTobService.saveTobRelation(ruleVO);
    }

    @Override
    public List<TriggerVo> getTriggerTobListByDeviceId(@RequestBody SaveIftttReq ruleVO) {
        List<TriggerVo> list = Lists.newArrayList();
        list = autoTobService.getTriggerTobListByDeviceId(ruleVO);
        return list;
    }

}
