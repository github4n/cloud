package com.iot.control.ifttt.service;

import com.iot.common.exception.BusinessException;
import com.iot.control.ifttt.entity.Trigger;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import com.iot.control.ifttt.mapper.TriggerMapper;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.util.RedisKeyUtil;
import com.iot.control.ifttt.vo.TriggerVo;
import com.iot.control.ifttt.vo.req.SaveIftttReq;
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

    @Autowired
    private TriggerMapper triggerMapper;

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
}
