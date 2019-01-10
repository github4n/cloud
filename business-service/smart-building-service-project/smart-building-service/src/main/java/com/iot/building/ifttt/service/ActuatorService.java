package com.iot.building.ifttt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.helper.Constants;
import com.iot.building.ifttt.entity.Actuator;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.mapper.ActuatorMapper;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.scene.service.SceneService;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.utils.ValueUtils;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
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
    private IBuildingSpaceService iBuildingSpaceService;

    @Autowired
    private SceneService sceneService;

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
        List<ActuatorVo> actuatorVos = req.getActuators();
        String key = RedisKeyUtil.getIftttActuatorKey(req.getId());
        List<Actuator> cacheList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(actuatorVos)) {
            for (ActuatorVo actuatorVo : actuatorVos) {
                Actuator actuator = BeanCopyUtil.getActuator(actuatorVo);
                setIftttActuator(actuator);
                Long[] position = actuatorVo.getPosition();
                if (position != null && position.length > 0) {
                    actuator.setPosition(ValueUtils.join(actuatorVo.getPosition(), ","));
                }
                actuator.setRuleId(req.getId());
                actuator.setTenantId(SaaSContextHolder.currentTenantId());
                int res = actuatorMapper.insertSelective(actuator);
                if (res == 0) {
                    throw new BusinessException(IftttExceptionEnum.SAVE_ACTUATOR_FAILED);
                }
                cacheList.add(actuator);
            }
        }

        //空值缓存
        if (cacheList.size() == 0) {
            cacheList.add(new Actuator());
        }
        //添加缓存
        RedisCacheUtil.listSet(key, cacheList);

    }

    /**
     * 保存对象
     *
     * @param actuator
     */
    public void save(Actuator actuator) {
        if (actuator.getId() != null && actuator.getId() != 0) {
            actuatorMapper.updateByPrimaryKeySelective(actuator);
        } else {
            actuatorMapper.insertSelective(actuator);
        }
    }

    /**
     * 设置属性
     *
     * @param actuator
     */
    private void setIftttActuator(Actuator actuator) {
        String type = actuator.getType();
        Map<String, String> plugMap = Constants.getPlugTypeMap();
        for (Map.Entry<String, String> entry : plugMap.entrySet()) {
            if (entry.getValue().equals(type)) {
                Map<String, Object> map = (Map<String, Object>) JSON.parse(actuator.getProperties());
                Map<String, Object> deviceMap = (Map<String, Object>) map.get("device");
                if (deviceMap != null && deviceMap.get("OnOff") != null) {
                    actuator.setProperties("{\"device\":{\"OnOff\":" + deviceMap.get("OnOff").toString() + "}," + "\"senceId\":\"\"}");
                }
            }
        }
    }

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
            actuators = actuatorMapper.selectByRuleId(ruleId, getTenantId());
            if (CollectionUtils.isEmpty(actuators)) {
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

    /**
     * 根据ruleId删除
     *
     * @param id
     * @param idxList
     */
    public void deleteByRuleId(Long id, List<Integer> idxList) {
        Long tenantId = getTenantId();
        List<Actuator> actuatorList = actuatorMapper.selectByRuleId(id, tenantId);
        if (actuatorList != null && actuatorList.size() > 0) {
            for (Actuator actuator : actuatorList) {
                idxList.add(actuator.getIdx());
            }
            RedisCacheUtil.delete(RedisKeyUtil.getIftttActuatorKey(id));
            actuatorMapper.deleteByRuleId(id, tenantId);
        }
        LOGGER.debug("delete rule's actuators success");
    }

    /**
     * 根据设备删除对象
     *
     * @param deviceId
     */
    public void deleteByDeviceId(String deviceId) {
        Long tenantId = getTenantId();
        List<Actuator> actuators = actuatorMapper.selectActuatorsByDeviceId(deviceId, tenantId);
        actuatorMapper.deleteByDeviceId(deviceId, tenantId);
        if (CollectionUtils.isNotEmpty(actuators)) {
            for (Actuator actuator : actuators) {
                //删除缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttActuatorKey(actuator.getRuleId()));
            }
        }
    }

    /**
     * 根据objectId删除对象
     *
     * @param objectId
     * @param type
     */
    public void deleteByObjectId(String objectId, String type) {
        if (StringUtil.isBlank(objectId) || StringUtil.isBlank(type)) {
            return;
        }

        Long tenantId = getTenantId();
        List<Actuator> actuators = actuatorMapper.selectActuatorsByObjectId(objectId, type, tenantId);
        if (CollectionUtils.isNotEmpty(actuators)) {
            actuatorMapper.deleteByObjectId(objectId, type, tenantId);

            for (Actuator actuator : actuators) {
                //删除缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttActuatorKey(actuator.getRuleId()));
            }
        }
    }

    /**
     * 执行ifttt
     *
     * @param ruleId
     */
    public void execIfttt(Long tenantId,Long ruleId, String type) {
        LOGGER.info("begin execIfttt..." + ruleId);
        List<ActuatorVo> actuatorList = selectByRuleId(ruleId);
        if (actuatorList != null && actuatorList.size() > 0) {
            for (ActuatorVo actuator : actuatorList) {
                String deviceId = actuator.getDeviceId();
                String thenType = actuator.getType();
                String properties = actuator.getProperties();
                Map<Object, Object> propMap = JSON.parseObject(properties, Map.class);
                if (Constants.IFTTT_THEN_DEVICE.equals(thenType) ||
                        (!Constants.IFTTT_THEN_SCENE.equals(thenType) && !Constants.IFTTT_THEN_AUTO.equals(thenType))) {
                    Map<String, Object> attr =  (Map<String, Object>) propMap.get("attr");
                    //设置设备属性（单控）
                    //centerDeviceMQTTService.setDevAttr(deviceId, attr);
                    iBuildingSpaceService.control(deviceId,attr);
                } else if (Constants.IFTTT_THEN_SCENE.equals(thenType)) {
                    String sceneId = actuator.getObjectId();
                    //执行情景
                    sceneService.sceneExecute(tenantId,Long.parseLong(sceneId));
                } else if (Constants.IFTTT_THEN_AUTO.equals(thenType)) {
                    // 触发IFTTT使能，保留
                }
            }
        }
    }
}
