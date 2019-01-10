package com.iot.building.ifttt.service;

import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.req.SaveRuleReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@Service
public class IftttService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IftttService.class);

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ActuatorService actuatorService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private TriggerService triggerService;
    @Autowired
    private SpaceApi spaceApi;

    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    /**
     * 保存rule
     *
     * @param req
     * @return
     */
    public Long saveRule(@RequestBody SaveRuleReq req) {
        LOGGER.info("=== receive save rule request ===" + req.toString());
        Rule rule = BeanCopyUtil.getRule(req);
        return ruleService.save(rule);
    }


    /**
     * 保存联动
     *
     * @param req
     * @return
     */
    public Long save(SaveIftttReq req) {
        LOGGER.info("=== receive save ifttt request ===" + req.toString());
        try {
        	 //保存rule
            Rule rule = BeanCopyUtil.getRule(req);
            SaaSContextHolder.setCurrentTenantId(rule.getTenantId());
            SaaSContextHolder.setCurrentOrgId(rule.getOrgId());
            SaaSContextHolder.setCurrentUserId(rule.getUserId());
            //如果有主键则先删除旧规则
            if (req.getId() != null && req.getId() != 0) {
                delete(req.getId(), false);
            }
            Long ruleId = ruleService.save(rule);
            req.setId(ruleId);
            //保存传感器
            sensorService.saveList(req);
            //保存关联
            relationService.saveList(req);
            //保存触发器
            triggerService.saveList(req);
            //保存执行器
            actuatorService.saveList(req);
            return ruleId;
        } catch (Exception e) {
            //保存IFTTT规则失败
            LOGGER.error("save ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.SAVE_IFTTT_ERROR, e);
        }
    }

    /**
     * 获取规则
     *
     * @param id
     * @return
     */
    public RuleResp get(Long id) {
        LOGGER.info("=== receive get ifttt request ===" + id);
        RuleResp res = new RuleResp();
        try {
            Rule rule = ruleService.getCache(id);
            DeploymentResp deploymentResp = spaceApi.findDeploymentById(rule.getTenantId(),res.getOrgId(),rule.getDeployMentId());
            rule.setDeployMentName(deploymentResp.getDeployName());
            res = BeanCopyUtil.getRuleResp(rule);
            res.setTenantId(getTenantId());
            LOGGER.debug("get rule success");

            res.setSensors(sensorService.selectByRuleId(id));
            res.setRelations(relationService.selectByRuleId(id));
            res.setActuators(actuatorService.selectByRuleId(id));
            res.setTriggers(triggerService.selectByRuleId(id));
        } catch (Exception e) {
            //获取单条规则失败
            LOGGER.error("get ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_ERROR, e);
        }
        return res;
    }

    /**
     * 删除规则
     *
     * @param id
     * @param delAll true 删除全部
     * @return
     */
    public List<Integer> delete(Long id, Boolean delAll) {
        LOGGER.info("=== receive delete ifttt request ===" + id);
        List<Integer> idxList = new ArrayList<>();
        try {
            //删除sensor
            sensorService.deleteByRuleId(id, idxList);
            // 删除 actuator
            actuatorService.deleteByRuleId(id, idxList);
            //删除relation
            relationService.deleteByRuleId(id);
            //删除trigger
            triggerService.deleteByRuleId(id);
            //删除rule
            if (delAll) {
                ruleService.delete(id);
            }
        } catch (Exception e) {
            //删除IFTTT规则失败
            LOGGER.error("delete ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.DELETE_IFTTT_ERROR, e);
        }
        return idxList;
    }

    /**
     * 删除模板下ifttt（暂用于测试）
     *
     * @param templateId
     */
    public void deleteByTemplateId(Long templateId) {
        List<Long> ruleIds = ruleService.selectRuleIdByTemplateId(templateId);
        for (Long ruleId : ruleIds) {
            delete(ruleId, true);
        }
    }

    /**
     * 根据设备执行联动
     *
     * @param deviceId
     * @param type
     */
    public void execIftttByDevice(String deviceId, String type, Map<String, Object> attrMap) {
        long st = System.currentTimeMillis();
        // 查询当前设备关联的IFTTT
        Set<Long> iftttList = getDeviceRuleCache(deviceId);
        LOGGER.debug("=== receive execIftttByDevice request ===" + deviceId + "," + type + ",cache：" + iftttList.toString());
        if (!CollectionUtils.isEmpty(iftttList)) {
            for (Long ruleId : iftttList) {
                //如果ruleId不存在，则不执行
                Rule rule = ruleService.getCache(ruleId);
                if (rule != null) {
                    attrMap.put("devId", deviceId);
                    execIftttByRule(rule, type, attrMap);
                }
            }
        }
        long et = System.currentTimeMillis();
        LOGGER.debug("execIftttByDevice execute time:" + (et - st) + "ms!");
    }

    /**
     * 获取设备关联的rule
     * @param deviceId
     */
    public Set<Long> getDeviceRuleCache(String deviceId) {
        String key = RedisKeyUtil.getDeviceRuleKey(deviceId);
        String ruleIds = RedisCacheUtil.valueGet(key);


        if (StringUtils.isEmpty(ruleIds)) {
            //缓存不存在，从数据库中取
            List<Long> list = sensorService.getRuleIdByDeviceId(deviceId);
            String value = "";
            if (CollectionUtils.isNotEmpty(list)) {
                for (Long vo : list) {
                    value += vo + ",";
                }
            } else {
                value = "-1";
            }
            //更新缓存，时效7天
            RedisCacheUtil.valueSet(key, value, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
            ruleIds = value;
        }


        Set<Long> set = new HashSet<>();
        if (!"-1".equals(ruleIds)) {
            //有数据
            String[] ids = ruleIds.split(",");
            for (String vo : ids) {
                set.add(Long.parseLong(vo));
            }
        }

        return set;
    }

    /**
     * 根据规则执行联动(定时任务)
     *
     * @param ruleId
     * @param tenantId
     * @param type
     */
    public void execCronIftttByRule(Long ruleId, Long tenantId, String type) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        //先判断任务是否启用
        Rule rule=ruleService.getCache(ruleId);
        if(rule.getStatus()==1){
            //直接执行
            actuatorService.execIfttt(tenantId,ruleId, type);
        }
    }

    /**
     * 根据规则执行联动
     *
     * @param rule
     * @param type
     */
    public void execIftttByRule(Rule rule, String type, Map<String, Object> attrMap) {
        Long ruleId = rule.getId();
        LOGGER.debug("=== receive execIftttByRule request ===" + "ruleId:" + ruleId + ",type:" + type+"attrMap:"+attrMap.toString());
        Long st = System.currentTimeMillis();
        try {
            //跨网关，且规则为启动状态
            if (rule != null && rule.getStatus() == 1 && rule.getIsMulti() == 1) {

                //LOGGER.debug("跨直连或2B业务，云端处理！");
                // isMulti:1 跨直连设备
                //多网关的触发，云上执行；单个网关，网关自己触发执行

                // 是否执行
                if (sensorService.checkTrigger(ruleId, attrMap)) {
                    LOGGER.debug("exec cross direct ifttt " + ruleId);
                    actuatorService.execIfttt(rule.getTenantId(),ruleId, type);
                } else {
                    LOGGER.debug("没达到条件，跨直连不执行 " + ruleId);
                }
            } else {
                LOGGER.debug("非跨直连，不执行 " + ruleId);
            }
        } catch (Exception e) {
            LOGGER.error("exec ifttt by rule error", e);
        }
        Long et = System.currentTimeMillis();
        LOGGER.debug("根据规则执行联动，用时：" + (et - st) + "毫秒！");
    }

    /**
     * 根据 直连设备id 删除 ifttt_rule
     *
     * @param directDeviceId
     */
    public void deleteByDirectDeviceId(String directDeviceId) {
        if (StringUtil.isBlank(directDeviceId)) {
            return;
        }
        List<Rule> ruleList = ruleService.getByDirectDeviceId(directDeviceId);
        if (CollectionUtils.isNotEmpty(ruleList)) {
            for (Rule rule : ruleList) {
                this.delete(rule.getId(), true);
            }
        }
    }

    /**
     * 根据设备id删除相应数据
     *
     * @param id
     */
    public void delByDeviceId(String id) {
        LOGGER.info("=== receive delByDeviceId request ===" + id);
        sensorService.deleteByDeviceId(id);
        actuatorService.deleteByDeviceId(id);
    }

    public  List<RuleResp> getRuleListByName(SaveIftttReq ruleVO) {
        List<RuleResp> list = ruleService.getRuleListByName(ruleVO);
        return  list;
    }

    public boolean findTemplateListByName(SaveIftttTemplateReq iftttReq) {
        return ruleService.findTemplateListByName(iftttReq);
    }
}