package com.iot.building.ifttt.service;

import com.iot.building.ifttt.entity.Trigger;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.mapper.TriggerMapper;
import com.iot.building.ifttt.mapper.TriggerTobMapper;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.common.exception.BusinessException;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：Trigger业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:02
 */
@Service
public class TriggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private TriggerTobMapper triggerTobMapper;

    @Autowired
    private TriggerMapper triggerMapper;

    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    /**
     * 保存列表
     * @param req
     */
    public void saveList(SaveIftttReq req){
        List<TriggerVo> triggerVos = req.getTriggers();
        if (CollectionUtils.isNotEmpty(triggerVos)) {
            for (TriggerVo triggerVo : triggerVos) {
                Trigger trigger = BeanCopyUtil.getTrigger(triggerVo);
                trigger.setRuleId(req.getId());
                trigger.setTenantId(SaaSContextHolder.currentTenantId());
                //int res = triggerTobMapper.insertSelective(trigger);
                int res = triggerMapper.insertSelective(trigger);
                if (res == 0) {
                    throw new BusinessException(IftttExceptionEnum.SAVE_TRIGGER_FAILED);
                }
            }
        }
    }

    /**
     * 根据ruleId查询
     * @param ruleId
     * @return
     * @throws BusinessException
     */
    public List<TriggerVo> selectByRuleId(Long ruleId) throws BusinessException {
        List<TriggerVo> triggerVOs = new ArrayList<>();
        List<Trigger> triggerCaches;
        Long tenantId = SaaSContextHolder.currentTenantId();
        String key = RedisKeyUtil.getIftttTriggerKey(ruleId);
        triggerCaches = RedisCacheUtil.listGetAll(key, Trigger.class);
        if (CollectionUtils.isEmpty(triggerCaches)) {
            //List<TriggerVo> triggers = triggerTobMapper.selectByRuleId(ruleId, tenantId);
            List<TriggerVo> triggers = triggerMapper.selectByRuleId(ruleId, tenantId);
            RedisCacheUtil.listSet(key,triggers);
            return  triggers;
        } else {
            for (Trigger trigger : triggerCaches) {
                triggerVOs.add(BeanCopyUtil.getTriggerVo(trigger));
            }
        }
        return triggerVOs;
    }

    /**
     * 根据ruleId删除
     * @param id
     */
    public void deleteByRuleId(Long id){
        Long tenantId = getTenantId();
        //triggerTobMapper.deleteByRuleId(id, tenantId);
        triggerMapper.deleteByRuleId(id, tenantId);
        RedisCacheUtil.delete(RedisKeyUtil.getIftttTriggerKey(id));
        LOGGER.debug("delete rule's triggers success");
    }
}
