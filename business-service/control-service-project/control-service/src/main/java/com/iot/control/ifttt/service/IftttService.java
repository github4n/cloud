package com.iot.control.ifttt.service;

import com.iot.common.exception.BusinessException;
import com.iot.control.ifttt.entity.Rule;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.device.api.DeviceApi;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
            SaaSContextHolder.setCurrentTenantId(0l);
            Rule rule = ruleService.getCache(id);
            res = BeanCopyUtil.getRuleResp(rule);
            res.setTenantId(rule.getTenantId());
            LOGGER.debug("get rule success");

            res.setSensors(sensorService.selectByRuleId(id));
            res.setRelations(relationService.selectByRuleId(id));
            res.setTriggers(triggerService.selectByRuleId(id));
            res.setActuators(actuatorService.selectByRuleId(id));
        } catch (Exception e) {
            //获取单条规则失败
            LOGGER.error("get ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_ERROR, e);
        }
        return res;
    }

}