package com.iot.shcs.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.security.exception.SecurityExceptionEnum;
import com.iot.shcs.security.mapper.SecurityRuleMapper;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.util.RedisKeyUtil;
import com.iot.shcs.security.vo.SecurityRule;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SecurityRuleServiceImpl implements SecurityRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityRuleServiceImpl.class);


    @Autowired
    private SecurityRuleMapper securityRuleMapper;

    @Override
    public int deleteByPrimaryKey(Long tenantId,Long securityId, String type) {
        RedisCacheUtil.delete(RedisKeyUtil.getSecurityRuleKey(tenantId,securityId,type));
        return securityRuleMapper.deleteByPrimaryKey(securityId,type);
    }

    @Override
    public SecurityRule selectBySecurityIdAndType(Long tenantId,Long securityId,String type) {
        SecurityRule securityRule=RedisCacheUtil.valueObjGet(RedisKeyUtil.getSecurityRuleKey(tenantId,securityId,type),SecurityRule.class);
        LOGGER.info("*****selectBySecurityIdAndType获取的缓存：{},tenantId={},securityId={},type={}",JSON.toJSONString(securityRule),tenantId,securityId,tenantId);
        if(securityRule==null){
            securityRule=securityRuleMapper.selectBySecurityIdAndType(securityId,type);
            LOGGER.info("*******selectBySecurityIdAndType从数据库中获取为："+JSON.toJSONString(securityRule));
            if(securityRule!=null){
                RedisCacheUtil.valueObjSet(RedisKeyUtil.getSecurityRuleKey(tenantId,securityId,type),securityRule);
                LOGGER.info("*******selectBySecurityIdAndType存入缓存的数据为："+JSON.toJSONString(securityRule));
            }
        }
        return securityRule;
    }

    /**
     * 修改rule
     *
     * @param rule
     */
    public Long saveSecurityRule(SecurityRule rule) {
        try {

            //rule数据库中没有数据
            boolean flag=RedisCacheUtil.hasKey(RedisKeyUtil.getSecurityRuleKey(rule.getTenantId(),rule.getSecurityId(),rule.getType()));
            LOGGER.info("saveSecurityRule是否获取到缓存："+JSON.toJSONString(rule)+"flag is:"+flag);
            //如果缓存没有则查找数据库
            if(!flag){
                SecurityRule securityRule=securityRuleMapper.selectBySecurityIdAndType(rule.getSecurityId(),rule.getType());
                if(securityRule!=null){
                    flag=true;
                }
                LOGGER.info("查找数据库后的结果："+JSON.toJSONString(rule)+"database flag is:"+flag);
            }
            if(!flag){
                //新增规则
                rule.setCreateTime(new Date());
                rule.setUpdateTime(new Date());
                LOGGER.info("******saveSecurityRule insertNewRule"+JSON.toJSONString(rule));
                securityRuleMapper.insertNewRule(rule);
                RedisCacheUtil.delete(RedisKeyUtil.getSecurityRuleKey(rule.getTenantId(),rule.getSecurityId(),rule.getType()));
            }else {
                //修改
                rule.setUpdateTime(new Date());
                LOGGER.info("******saveSecurityRule updateBySecurityIdAndType"+JSON.toJSONString(rule));
                securityRuleMapper.updateBySecurityIdAndType(rule);
                // 删除用户的security_rule列表缓存
                RedisCacheUtil.delete(RedisKeyUtil.getSecurityRuleKey(rule.getTenantId(),rule.getSecurityId(),rule.getType()));
            }
            LOGGER.info("****saveSecurityRule 缓存"+JSON.toJSONString(rule));
            //加入缓存
            RedisCacheUtil.valueObjSet(RedisKeyUtil.getSecurityRuleKey(rule.getTenantId(),rule.getSecurityId(),rule.getType()), rule, RedisKeyUtil.DEFAULT_CACHE_TIME);
        } catch (Exception e) {
            //保存Rule失败
            LOGGER.error("save rule error", e);
            throw new BusinessException(SecurityExceptionEnum.SAVE_RULE_ERROR, e);
        }finally {
            return rule.getSecurityId();
        }
    }
    //用于数据迁移，因为租户id在迁移时候没办法通过getTenantId()获取
    @Override
    public Long saveSecurityRuleForMoveData(SecurityRule rule, boolean isNoData) {
        try {
            //rule数据库中没有数据
            if(isNoData){
                //新增规则
                rule.setCreateTime(new Date());
                rule.setUpdateTime(new Date());
                //还未加缓存
                securityRuleMapper.insertNewRule(rule);
            }else {
                //修改
                rule.setUpdateTime(new Date());
                securityRuleMapper.updateBySecurityIdAndType(rule);
            }
        } catch (Exception e) {
            //保存Rule失败
            LOGGER.error("save rule error", e);
            throw new BusinessException(SecurityExceptionEnum.SAVE_RULE_ERROR, e);
        }finally {
            return rule.getSecurityId();
        }
    }

    @Override
    public SecurityRule getRuleBean(MqttMsg message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        SecurityRule securityRule = new SecurityRule();
        //取SecurityId
        Object securityIdObj = payload.get("securityId");
        if (securityIdObj != null) {
            Long securityId = Long.parseLong(securityIdObj.toString());
            securityRule.setSecurityId(securityId);
        }
        Object securityTypeObj = payload.get("securityType");
        if (securityTypeObj != null) {
            String securityType= securityTypeObj.toString();
            securityRule.setType(securityType);
        }
        //enable
        Integer enable = 0;
        if (payload.containsKey("enabled")) {
            Object enable_ = payload.get("enabled");
            enable = enable_ != null ? Integer.parseInt(enable_.toString()) : 0;
            securityRule.setEnabled(enable);
        }

        // 延时生效(单位 秒)
        Integer defer = 0;
        if (payload.containsKey("defer")) {
            Object defer_ = payload.get("defer");
            defer = defer_ != null ? Integer.parseInt(String.valueOf(defer_)) : 0;
            securityRule.setDefer(defer);
        }

        // 延时报警(单位 秒)
        Integer delay = 0;
        if (payload.containsKey("delay")) {
            Object delay_ = payload.get("delay");
            delay = delay_ != null ? Integer.parseInt(String.valueOf(delay_)) : 0;
            securityRule.setDelay(delay);
        }


        //if
        if (payload.containsKey("if")) {
            Map<String, Object> ifMap = (Map<String, Object>) payload.get("if");
            securityRule.setIfCondition(JSON.toJSONString(ifMap));
        }
        //then
        if (payload.containsKey("then")) {
            List<Map<String, Object>> thenList = (List<Map<String, Object>>) payload.get("then");
            securityRule.setThenCondition(JSON.toJSONString(thenList));
        }

        return securityRule;
    }

    @Override
    public List<SecurityRule> list(SecurityRule securityRule) {

        List<SecurityRule> ruleList=new ArrayList<>();
        if(securityRule==null){
            return null;
        }
        if(securityRule.getTenantId()==null ||securityRule.getSecurityId()==null){
            return null;
        }
        ruleList=securityRuleMapper.list(securityRule);
        return ruleList;
    }



    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }




}
