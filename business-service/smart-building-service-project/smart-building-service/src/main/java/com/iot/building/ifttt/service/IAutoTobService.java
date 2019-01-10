package com.iot.building.ifttt.service;

import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.common.helper.Page;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;

import java.util.List;
import java.util.Map;

/**
 * 描述：联动服务
 * 创建人： huangxu
 * 创建时间： 2018/10/12
 */
public interface IAutoTobService {

    /**
     * 设备上报判断触发
     *
     * @param deviceId
     * @param attrMap
     */
    void checkByDevice(String deviceId, Map<String, Object> attrMap);


    /**
     * 保存/联动信息（sensor、actuator、or或and）
     *
     * @param ruleVO
     */
    Long saveAuto(SaveIftttReq ruleVO);


    /**
     * 保存/修改ifttt记录（保存/修改build_tob_rule表）
     * @param ruleVO
     * @return
     */
    Long saveBuildTobRule(SaveIftttReq ruleVO);

    /**
     * 综合查找build_tob_rule集合
     * @param ruleVO
     * @return
     */
    List<RuleResp> getRuleList(SaveIftttReq ruleVO);

    /**
     * 通过名字查找build_tob_rule集合
     * @param ruleVO
     * @return
     */
    List<RuleResp> getRuleListByName(SaveIftttReq ruleVO);

    /**
     * 保存点、线，表为tob_trigger
     * @param ruleVO
     * @return
     */
    Long saveTobTrigger(SaveIftttReq ruleVO);

    /**
     * 获取单个 build_tob_rule 的详情
     * @param id
     * @return
     */
    RuleResp get(Long tenantId,Long orgId,Long id);

    /**
     * 删除ifttt
     * @param id
     * @return
     */
    List<Integer> delete(Long tenantId,Long orgId,Long id,boolean flag);

    /**
     * 列表  表build_tob_rule
     * @param req
     * @return
     */
    Page<RuleResp> list(RuleListReq req);

    /**
     * 启用或停止联动监听
     * @param id
     * @param start
     * @return
     */
    boolean run(Long id, Boolean start);

    /**
     * 保存relation
     * @param ruleVO
     * @return
     */
    void saveTobRelation(SaveIftttReq ruleVO);

    /**
     * 下发的
     */
    void saveLowerHair(SaveIftttReq ruleVO);

    List<TriggerVo> getTriggerTobListByDeviceId(SaveIftttReq ruleVO);

    /**
     * 创建下发到网关
     * @param ruleVO
     */
    void createLowerHair(SaveIftttReq ruleVO);

    /**
     * 删除下发到网关
     * @param id
     */
    void deleteLowerHair(Long id,String clientId);

    /**
     * 网关开启/关闭联动
     * @param id
     * @param clientId
     * @param start
     */
    void runLowerHair(Long id, String clientId, Boolean start);

    void setIftttOnOff(Boolean flag, RuleResp rule);

    void deleteAll(Long orgId,Long appletId, Long buildToRuleId, Long tenantId);
}
