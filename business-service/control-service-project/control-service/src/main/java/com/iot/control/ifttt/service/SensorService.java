package com.iot.control.ifttt.service;

import com.iot.control.ifttt.entity.Sensor;
import com.iot.control.ifttt.mapper.SensorMapper;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.util.RedisKeyUtil;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述：Sensor业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:02
 */
@Service
public class SensorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private SensorMapper sensorMapper;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RelationService relationService;

    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }


    /**
     * 根据ruleId查询
     *
     * @param ruleId
     * @return
     */
    public List<SensorVo> selectByRuleId(Long ruleId) {
        List<SensorVo> sensorVOs = new ArrayList<>();
        List<Sensor> sensors = getSensorsCache(ruleId);
        //不为空，则赋值
        if (CollectionUtils.isNotEmpty(sensors)) {
            int s1 = sensors.size();
            Set<Long> set = new HashSet<>();
            for (Sensor sensor : sensors) {
                set.add(sensor.getId());
            }
            int s2 = set.size();
            //有重复值，删除缓存
            if (s1 > s2) {
                LOGGER.debug("有重复sensor缓存，删除旧缓存.");
                String key = RedisKeyUtil.getIftttSensorKey(ruleId);
                RedisCacheUtil.delete(key);

                //重新获取
                sensors=getSensorsCache(ruleId);
            }

            //赋值
            for (Sensor sensor : sensors) {
                sensorVOs.add(BeanCopyUtil.getSensorVo(sensor));
            }
        }
        return sensorVOs;
    }
    /**
     * 从缓存取数据
     *
     * @param ruleId
     * @return
     */
    public List<Sensor> getSensorsCache(Long ruleId) {
        String key = RedisKeyUtil.getIftttSensorKey(ruleId);
        List<Sensor> sensors = RedisCacheUtil.listGetAll(key, Sensor.class);
        if (CollectionUtils.isEmpty(sensors)) {
            sensors = sensorMapper.selectByRuleId(ruleId, getTenantId());
            //添加缓存
            RedisCacheUtil.listSet(key, sensors);
        }
        return sensors;
    }

}
