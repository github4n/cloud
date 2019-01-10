package com.iot.building.ifttt.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.service.impl.DeviceService;
import com.iot.building.helper.Constants;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.entity.Sensor;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.mapper.SensorMapper;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.util.TimeUtil;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.req.GetSensorReq;
import com.iot.building.ifttt.vo.req.RuleReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.SensorResp;
import com.iot.building.utils.ValueUtils;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;

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

    @Autowired
    private ScheduleApi scheduleApi;

    @Autowired
    private AstroClockService astroClockService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceStateCoreApi deviceStateServiceApi;
    @Autowired
    private DeviceCoreApi deviceCoreApi;

    /**
     * 保存列表
     *
     * @param req
     */
    public void saveList(SaveIftttReq req) {
        Long ruleId = req.getId();
        List<SensorVo> sensorVos = req.getSensors();
        if (CollectionUtils.isNotEmpty(sensorVos)) {
            List<Sensor> sensors = Lists.newArrayList();
            for (SensorVo sensorVo : sensorVos) {
                //设备关联rule缓存
                if (sensorVo.getType().contains("_2B") || IftttConstants.IFTTT_TRIGGER_DEVICE.equals(sensorVo.getType())) {
                    sensorVo.setType(sensorVo.getType().replace("_2B", ""));
                    String deviceId = sensorVo.getDeviceId();
                    List<Long> iftttList = RedisCacheUtil.listGetAll(RedisKeyUtil.getDeviceIftttKey(deviceId), Long.class);
                    LOGGER.debug("iftttList = {}", iftttList);
                    //避免重复添加
                    if (CollectionUtils.isEmpty(iftttList) || !iftttList.contains(ruleId)) {
                        LOGGER.debug("listPush deviceifttt {}", ruleId);
                        RedisCacheUtil.listPush(RedisKeyUtil.getDeviceIftttKey(deviceId), ruleId);
                    }

                    //删除device-rule缓存
                    RedisCacheUtil.delete(RedisKeyUtil.getDeviceRuleKey(deviceId));
                }
                Sensor sensor = BeanCopyUtil.getSensor(sensorVo);
                sensor.setRuleId(ruleId);
                sensor.setTenantId(SaaSContextHolder.currentTenantId());
                int res = sensorMapper.insertSelective(sensor);
                if (res == 0) {
                    throw new BusinessException(IftttExceptionEnum.SAVE_SENSOR_FAILED);
                }
                sensors.add(sensor);

                //取是否跨网关
                if (req.getIsMulti() == null) {
                    Rule rule = ruleService.getCache(req.getId());
                    req.setIsMulti(rule.getIsMulti());
                    req.setDirectId(rule.getDirectId());
                }

                //不为模板 添加定时任务
               /* if (req.getTemplateFlag() == null || req.getTemplateFlag() != 1) {
                    if (IftttConstants.IFTTT_TRIGGER_TIMER_2B.equals(sensor.getType())) {
                        //中控的需云端加定时任务
                        addCronJob(sensor);
                    } else if (IftttConstants.IFTTT_TRIGGER_TIMER.equals(sensor.getType()) && req.getIsMulti() == 1) {
                        //2C 跨网关时，需云端添加定时任务，不跨网关的ifttt，网关处理定时
                        addCronJob(sensor);
                    }
                    *//*else if (req.getIsMulti() == 1 && (IftttConstants.IFTTT_TRIGGER_SUNRISE.equals(sensor.getType()) ||
                            IftttConstants.IFTTT_TRIGGER_SUNSET.equals(sensor.getType()))) {
                        //天文定时任务
                        RuleReq rule = new RuleReq();
                        rule.setId(req.getId());
                        astroClockService.addSubAstroClockJob(rule);
                    }*//*
                }*/
            }
            //第一次加入缓存
            String key = RedisKeyUtil.getIftttSensorKey(ruleId);
            RedisCacheUtil.listSet(key, sensors);
        }
    }

    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    /**
     * 添加定时任务
     *
     * @param sensor
     *//*
    public void addCronJob(Sensor sensor) {
        if (IftttConstants.IFTTT_TRIGGER_TIMER_2B.equals(sensor.getType())) {
            //中控添加额外的定时任务适配
            String properties = sensor.getProperties();
            List<Map> propList = JSON.parseArray(properties, Map.class);
            if (!CollectionUtils.isEmpty(propList)) {
                Map<String, Object> prop = propList.get(0);
                String triggerValue = String.valueOf(prop.get("triggerValue"));
                List<Integer> repeat = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
                Map<String, Object> triggerMap = Maps.newHashMap();
                triggerMap.put("at", triggerValue);
                triggerMap.put("repeat", repeat);
                sensor.setProperties(JSON.toJSONString(triggerMap));
                addAdapterCronJob(sensor);
            }
        } else if (IftttConstants.IFTTT_TRIGGER_TIMER.equals(sensor.getType())) {
            addAdapterCronJob(sensor);
        }
    }

    *//**
     * 添加定时任务
     *
     * @param sensor
     *//*
    public void addAdapterCronJob(Sensor sensor) {
        Long ruleId = sensor.getRuleId();

        LOGGER.info("===receive addCronJob request===, ruleId={}, sensor.getId()={}", ruleId, sensor.getId());
        AddJobReq addJobReq = new AddJobReq();
        addJobReq.setJobName(sensor.getId().toString());
        addJobReq.setJobClass(ScheduleConstants.TFTTT_JOB);
        Map<String, Object> data = Maps.newHashMap();
        data.put("ruleId", ruleId);
        data.put("type", sensor.getType());
        data.put("tenantId", sensor.getTenantId());
        addJobReq.setData(data);

        // 获取地区
        String area = null;
        Rule rule = ruleService.getCache(ruleId);
        if (rule != null) {
            area = deviceService.getAreaByUserId(rule.getUserId());
        }
        ZoneId zoneId = TimeUtil.getZoneId(area);
        LOGGER.info("addAdapterCronJob(), final use zoneId={}", zoneId.getId());

        // 指定地区当前时间
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        // 偏移的秒数
        long totalSeconds = zdt.getOffset().getTotalSeconds();
        long totalMins = (totalSeconds) / (60);

        String cron = TimeUtil.getUTCCron(TimeUtil.getCron(sensor.getProperties()), Integer.parseInt(String.valueOf(totalMins)));
        addJobReq.setCron(cron);
        scheduleApi.addJob(addJobReq);
    }
*/
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
                sensors = getSensorsCache(ruleId);
            }

            //赋值
            for (Sensor sensor : sensors) {
                sensorVOs.add(BeanCopyUtil.getSensorVo(sensor));
            }
        }
        return sensorVOs;
    }

    /**
     * 获取关联的ruleId
     *
     * @param deviceId
     * @return
     */
    public List<Long> getRuleIdByDeviceId(String deviceId) {
        return sensorMapper.getRuleIdByDeviceId(deviceId);
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

    /**
     * 根据ruleId 删除
     *
     * @param id
     * @param idxList
     */
    public void deleteByRuleId(Long id, List<Integer> idxList) {
        Long tenantId = getTenantId();
        List<Sensor> sensors = sensorMapper.selectByRuleId(id, tenantId);
        if (CollectionUtils.isNotEmpty(sensors)) {
            for (Sensor sensor : sensors) {
                if (CollectionUtils.isNotEmpty(idxList)) {
                    idxList.add(sensor.getIdx());
                }
                String deviceId = sensor.getDeviceId();
                //删除设备与ifttt的关系缓存
                delDeviceRuleRelation(sensor.getRuleId(), deviceId);
                //删除定时任务
                if (IftttConstants.IFTTT_TRIGGER_TIMER.equals(sensor.getType()) ||
                        IftttConstants.IFTTT_TRIGGER_SUNRISE.equals(sensor.getType()) ||
                        IftttConstants.IFTTT_TRIGGER_SUNSET.equals(sensor.getType())) {
                    scheduleApi.delJob(sensor.getId().toString());
                }
            }
            RedisCacheUtil.delete(RedisKeyUtil.getIftttSensorKey(id));
            sensorMapper.deleteByRuleId(id, tenantId);
        }
        LOGGER.debug("delete rule's sensors success");
    }

    /**
     * 根据设备ID删除
     *
     * @param deviceId
     */
    public void deleteByDeviceId(String deviceId) {
        Long tenantId = getTenantId();
        List<Sensor> sensors = sensorMapper.findSensorsByDeviceId(deviceId, tenantId);
        sensorMapper.deleteByDeviceId(deviceId, tenantId);
        if (CollectionUtils.isNotEmpty(sensors)) {
            for (Sensor sensor : sensors) {
                //删除定时任务
                if (IftttConstants.IFTTT_TRIGGER_TIMER.equals(sensor.getType())) {
                    scheduleApi.delJob(sensor.getId().toString());
                }
                //删除缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttSensorKey(sensor.getRuleId()));
                //删除ifttt-device缓存
                RedisCacheUtil.delete(RedisKeyUtil.getDeviceIftttKey(sensor.getDeviceId()));
                //删除device-rule缓存
                RedisCacheUtil.delete(RedisKeyUtil.getDeviceRuleKey(deviceId));
            }
        }
    }

    /**
     * 删除设备与ifttt的缓存
     *
     * @param ruleId
     * @param deviceId
     */
    public void delDeviceRuleRelation(Long ruleId, String deviceId) {
        List<Long> iftttList = RedisCacheUtil.listGetAll(RedisKeyUtil.getDeviceIftttKey(deviceId), Long.class);
        List<Long> list = Lists.newArrayList();
        if (!iftttList.isEmpty()) {
            for (Long id : iftttList) {
                if (!list.contains(id) && id != ruleId) {
                    list.add(id);
                }
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            RedisCacheUtil.delete(RedisKeyUtil.getDeviceIftttKey(deviceId));
        } else {
            LOGGER.debug("delDeviceRuleRelation listSet({} {})", RedisKeyUtil.getDeviceIftttKey(deviceId), list);
            RedisCacheUtil.listSet(RedisKeyUtil.getDeviceIftttKey(deviceId), list);
        }

        //删除device-rule缓存
        RedisCacheUtil.delete(RedisKeyUtil.getDeviceRuleKey(deviceId));
    }

    /**
     * 条件获取列表
     *
     * @param req
     * @return
     */
    public List<SensorResp> getSensorByParams(GetSensorReq req) {
        LOGGER.info("=== receive getSensorByParams request ===" + req.toString());
        req.setTenantId(getTenantId());
        return sensorMapper.getSensorByParams(req);
    }

    /**
     * 判断 触发条件是否满足
     *
     * @return
     */
    public boolean checkTrigger(Long ruleId, Map<String, Object> attrMap) {
        String relationType = relationService.getRelationType(ruleId);
        boolean isTrigger = false;
        List<SensorVo> sensorList = selectByRuleId(ruleId);
        for (SensorVo sensor : sensorList) {
            String trigType = sensor.getType();
            // 触发类型为设备
            if (IftttConstants.IFTTT_TRIGGER_DEVICE.equals(trigType)) {
                //时间范围
                //boolean isTrigger1 = checkTimming(sensor.getTiming(), sensor.getDeviceId());
                //判断是否是当前状态字段
                Map<String, Object> map = JSON.parseObject(sensor.getProperties(), Map.class);
                String attr = String.valueOf(map.get("attr"));
                boolean isTrigger2 = attrMap.containsKey(attr);
                //状态值一致
                boolean isTrigger3 = checkDeviceTrigger(sensor.getDeviceId(), sensor.getProperties());

                //isTrigger = isTrigger1 && isTrigger2 && isTrigger3;
                isTrigger = isTrigger2 && isTrigger3;
                if (relationType.equals(Constants.IFTTT_RELATION_AND)) {
                    if (!isTrigger) {
                        // 如果没有符合触发条件，则直接退出循环
                        break;
                    }
                }

                if (relationType.equals(Constants.IFTTT_RELATION_OR)) {
                    //判断是否是当前设备触发
                    String devId = (String) attrMap.get("devId");
                    Boolean isTrigger4 = sensor.getDeviceId().equals(devId);
                    ListDeviceStateReq params=new ListDeviceStateReq();
                    Long tenantId=deviceCoreApi.get(sensor.getDeviceId()).getTenantId();
                    params.setTenantId(tenantId);
                    List<String> deviceIds=Lists.newArrayList();deviceIds.add(sensor.getDeviceId());
                    params.setDeviceIds(deviceIds);
                    Map<String, Object> redisProperties = deviceStateServiceApi.listStates(params).get(sensor.getDeviceId());

                    isTrigger = isTrigger && isTrigger4;
                    LOGGER.info("===设备状态结果：" + ruleId +
                            "，传入状态：" + attrMap.toString() +
                            "，properties:" + sensor.getProperties() +
                            "，缓存数据:" + redisProperties.toString() +
                           /* "，时间范围：" + isTrigger1 +*/
                            "，设备字段一致：" + isTrigger2 +
                            "，设备值一致：" + isTrigger3 +
                            "，设备主键一致：" + isTrigger4 +
                            ",最终结果：" + isTrigger);

                    if (isTrigger) {
                        // 如果有符合触发条件，则直接退出循环
                        break;
                    }
                }

            } else if (IftttConstants.IFTTT_TRIGGER_TIMER.equals(trigType) || IftttConstants.IFTTT_TRIGGER_TIMER_2B.equals(trigType)) {
                // 定时，直接返回 true(时间到了自动定时执行)
                isTrigger = true;
            } else if (IftttConstants.IFTTT_TRIGGER_SUNSET.equals(trigType)
                    || IftttConstants.IFTTT_TRIGGER_SUNRISE.equals(trigType)) {
                // 直接返回 true(时间到了自动定时执行)
                isTrigger = true;
                // sunrise/sunset，每天执行，执行完，删除定时任务
                scheduleApi.delJob(sensor.getId().toString());
            }
        }

        return isTrigger;
    }

   /* *//**
     * 判断 sensor 是否在指定时间内触发（triggerType 为 dev 有效）
     *
     * @param valideTime
     * @param deviceId
     * @return
     *//*
    public boolean checkTimming(String valideTime, String deviceId) {
        boolean isTrigger = false;
        try {
            String area = deviceService.getAreaByDeviceUuid(deviceId);
            ZoneId zoneId = TimeUtil.getZoneId(area);
            LOGGER.info("checkTimming(), final use zoneId={}", zoneId.getId());

            Map<Object, Object> timeMap = JSON.parseObject(valideTime, Map.class);
            String beginStr = (String) timeMap.get("begin");
            String endStr = (String) timeMap.get("end");
            List<Integer> repeat = (List<Integer>) timeMap.get("week");

            // 指定时区的当前时间
            LocalDateTime areaLocalDateTime = LocalDateTime.now(zoneId);
            LocalTime areaLocalTime = areaLocalDateTime.toLocalTime();
            int w = areaLocalDateTime.getDayOfWeek().getValue() - 1;

            // 设定的 开始、结束时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime begin = LocalTime.parse(beginStr, formatter);
            LocalTime end = LocalTime.parse(endStr, formatter);

            for (int i : repeat) {
                if (i == w) {
                    if (areaLocalTime.isAfter(begin) && areaLocalTime.isBefore(end)) {
                        isTrigger = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("check timing error", e);
        }

        LOGGER.info("checkTimming(), deviceId={}, isTrigger={}, valideTime={}", deviceId, isTrigger, valideTime);
        return isTrigger;
    }*/

    /**
     * 判断设备是否触发
     *
     * @param deviceId
     * @param properties
     * @return
     */
    public boolean checkDeviceTrigger(String deviceId, String properties) {
        boolean isTrigger = false;
        Map<String, Object> map = JSON.parseObject(properties, Map.class);
        String attr = String.valueOf(map.get("attr"));
        String compOp = String.valueOf(map.get("compOp"));
        String value = String.valueOf(map.get("value"));

        LOGGER.info("checkDeviceTrigger(), deviceId={}, properties={}", deviceId, properties);
        ListDeviceStateReq params=new ListDeviceStateReq();
        Long tenantId=deviceCoreApi.get(deviceId).getTenantId();
        params.setTenantId(tenantId);
        List<String> deviceIds=Lists.newArrayList();deviceIds.add(deviceId);
        params.setDeviceIds(deviceIds);
        Map<String, Object> redisProperties = deviceStateServiceApi.listStates(params).get(deviceId);
        //取出设备状态缓存
//        Map<String, Object> redisProperties = deviceStateServiceApi.findDeviceStateListByDeviceId(deviceId);
        LOGGER.info("checkDeviceTrigger(), deviceId={}, properties from redis={}", deviceId, redisProperties);
        if (redisProperties.containsKey(attr)) {
            Object redisValue = redisProperties.get(attr);
            switch (compOp) {
                case Constants.IFTTT_COMP_OP_EQUALS:
                    if (value != null && redisValue != null) {
                        isTrigger = value.equals(redisValue.toString());
                    } else {
                        isTrigger = false;
                    }
                    break;
                case Constants.IFTTT_COMP_OP_GREATER_EQUALS:
                    // >= 将值转换成浮点类型比较，字符串只有 ==
                    Float floatValue = ValueUtils.getFloatValue(value);
                    Float floatRedisValue = ValueUtils.getFloatValue(redisValue);
                    isTrigger = floatRedisValue >= floatValue;
                    break;
                case Constants.IFTTT_COMP_OP_LESS_EQUALS:
                    // <= 将值转换成浮点类型比较，字符串只有 ==
                    Float floatValue2 = ValueUtils.getFloatValue(value);
                    Float floatRedisValue2 = ValueUtils.getFloatValue(redisValue);
                    isTrigger = floatRedisValue2 <= floatValue2;
                    break;
                case Constants.IFTTT_COMP_OP_GREATER:
                    // > 将值转换成浮点类型比较，字符串只有 ==
                    Float floatValue3 = ValueUtils.getFloatValue(value);
                    Float floatRedisValue3 = ValueUtils.getFloatValue(redisValue);
                    isTrigger = floatRedisValue3 > floatValue3;
                    break;
                case Constants.IFTTT_COMP_OP_LESS:
                    // < 将值转换成浮点类型比较，字符串只有 ==
                    Float floatValue4 = ValueUtils.getFloatValue(value);
                    Float floatRedisValue4 = ValueUtils.getFloatValue(redisValue);
                    isTrigger = floatRedisValue4 < floatValue4;
                    break;
            }
        } else {
            isTrigger = false;
        }

        LOGGER.info("checkDeviceTrigger(), isTrigger={}", isTrigger);
        return isTrigger;
    }
}
