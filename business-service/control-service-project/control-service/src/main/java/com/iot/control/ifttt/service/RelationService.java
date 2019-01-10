package com.iot.control.ifttt.service;

import com.iot.common.exception.BusinessException;
import com.iot.control.ifttt.entity.Relation;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import com.iot.control.ifttt.mapper.RelationMapper;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.util.RedisKeyUtil;
import com.iot.control.ifttt.vo.RelationVo;
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
 * 描述：Relation业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:01
 */
@Service
public class RelationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private RelationMapper relationMapper;

    /**
     * 根据ruleId获取列表
     *
     * @param ruleId
     * @return
     * @throws BusinessException
     */
    public List<RelationVo> selectByRuleId(Long ruleId) throws BusinessException {
        List<RelationVo> relationVOs = new ArrayList<>();
        String key = RedisKeyUtil.getIftttRelationKey(ruleId);
        List<Relation> relations = RedisCacheUtil.listGetAll(key, Relation.class);
        if (CollectionUtils.isEmpty(relations)) {
            relations = relationMapper.selectByRuleId(ruleId, null);
            //添加缓存
            RedisCacheUtil.listSet(key, relations);
        }

        if (CollectionUtils.isNotEmpty(relations)) {
            for (Relation relation : relations) {
                RelationVo relationVO = BeanCopyUtil.getRelationVo(relation);
                relationVOs.add(relationVO);
            }
        }

        return relationVOs;
    }
}
