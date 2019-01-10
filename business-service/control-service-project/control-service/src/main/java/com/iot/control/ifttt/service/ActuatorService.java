package com.iot.control.ifttt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.control.ifttt.entity.Actuator;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import com.iot.control.ifttt.mapper.ActuatorMapper;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.util.RedisKeyUtil;
import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.req.SaveIftttReq;
import com.iot.control.scene.service.SceneService;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：Actuator业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:01
 */
@Service
public class ActuatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private ActuatorMapper actuatorMapper;

    @Autowired
    private SceneService sceneService;

    /**
     * 根据ruleId获取列表
     *
     * @param ruleId
     * @return
     * @throws BusinessException
     */
    public List<ActuatorVo> selectByRuleId(Long ruleId) throws BusinessException {
        List<ActuatorVo> actuatorVOs = new ArrayList<>();
        String key = RedisKeyUtil.getIftttActuatorKey(ruleId);
        List<Actuator> actuators = RedisCacheUtil.listGetAll(key, Actuator.class);

        //没值从数据获取
        if (CollectionUtils.isEmpty(actuators)) {
            //从数据库取
            actuators = actuatorMapper.selectByRuleId(ruleId, null);
            if(CollectionUtils.isEmpty(actuators)){
                //数据库取出为空
                actuators.add(new Actuator());
            }
            RedisCacheUtil.listSet(key, actuators);
        }

        //有值则转换
        if (CollectionUtils.isNotEmpty(actuators)) {
            if (actuators.get(0).getId() == null) {
                //没有值，返回null
                return actuatorVOs;
            }

            for (Actuator actuator : actuators) {
                ActuatorVo actuatorVO = BeanCopyUtil.getActuatorVo(actuator);
                actuatorVOs.add(actuatorVO);
            }
            return actuatorVOs;
        } else {
            return actuatorVOs;
        }
    }
}
