package com.iot.building.ifttt.service;

import com.iot.building.helper.Constants;
import com.iot.building.ifttt.entity.Relation;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.mapper.RelationMapper;
import com.iot.building.ifttt.mapper.RelationTobMapper;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.vo.RelationVo;
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
 * 描述：Relation业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:01
 */
@Service
public class RelationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private RelationTobMapper relationTobMapper;

    @Autowired
    private RelationMapper relationMapper;

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
     *
     * @param req
     */
    public void saveList(SaveIftttReq req) {
        List<RelationVo> relationVos = req.getRelations();
        if (CollectionUtils.isNotEmpty(relationVos)) {
            for (RelationVo relationVo : relationVos) {
                Relation relation = BeanCopyUtil.getRelation(relationVo);
                relation.setRuleId(req.getId());
                relation.setTenantId(SaaSContextHolder.currentTenantId());
                //int res = relationTobMapper.insertSelective(relation);
                int res = relationMapper.insertSelective(relation);
                if (res == 0) {
                    throw new BusinessException(IftttExceptionEnum.SAVE_RELATION_FAILED);
                }
            }
        }
    }

    /**
     * 保存对象
     *
     * @param relation
     */
    public void save(Relation relation) {
        if (relation.getId() != null && relation.getId() != 0) {
            //relationTobMapper.updateByPrimaryKeySelective(relation);
            relationMapper.updateByPrimaryKeySelective(relation);
        } else {
            //relationTobMapper.insertSelective(relation);
            relationMapper.insertSelective(relation);
        }
    }

    /**
     * 根据ruleId获取列表
     *
     * @param ruleId
     * @return
     * @throws BusinessException
     */
    public List<RelationVo> selectByRuleId(Long ruleId) throws BusinessException {
        List<RelationVo> relationVOs = new ArrayList<>();
        Long tenantId = SaaSContextHolder.currentTenantId();
        String key = RedisKeyUtil.getIftttRelationKey(ruleId);
        List<Relation> relations = RedisCacheUtil.listGetAll(key, Relation.class);
        if (CollectionUtils.isEmpty(relations)) {
            //relations = relationTobMapper.selectByRuleId(ruleId, tenantId);
            relations = relationMapper.selectByRuleId(ruleId, tenantId);
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

    /**
     * 根据ruleId删除
     *
     * @param id
     */
    public void deleteByRuleId(Long id) {
        //relationTobMapper.deleteByRuleId(id, getTenantId());
        relationMapper.deleteByRuleId(id, getTenantId());
        RedisCacheUtil.delete(RedisKeyUtil.getIftttRelationKey(id));
        LOGGER.debug("delete rule's relations success");
    }

    /**
     * 获取relation type
     *
     * @param ruleId
     * @return
     */
    public String getRelationType(Long ruleId) {
        List<Relation> relationList = relationTobMapper.selectByRuleId(ruleId, getTenantId());
        String type = Constants.IFTTT_RELATION_AND;
        if (!relationList.isEmpty()) {
            Relation relation = relationList.get(0);
            if (relation != null) {
                type = relation.getType();
            }
        }
        type = type.toUpperCase();
        if (!Constants.IFTTT_RELATION_AND.equals(type) && !Constants.IFTTT_RELATION_OR.equals(type)) {
            // 不是OR，也不是AND关系 则设为AND
            type = Constants.IFTTT_RELATION_AND;
        }
        return type;
    }
}
