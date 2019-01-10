package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.device.service.IAirSwitchEventService;
import com.iot.device.vo.req.AirSwitchDailyEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.req.AirSwitchHourEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchMonthEventStatisticsReq;
import com.iot.device.vo.rsp.AirSwitchDailyEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.AirSwitchHourEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchMonthEventStatisticsResp;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.QueryOperators;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@Service
public class AirSwitchEventServiceImpl implements IAirSwitchEventService {

    private Logger log = LoggerFactory.getLogger(AirSwitchEventServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Boolean insertOrUpdateBatch(List<AirSwitchEventReq> list) {
        log.info("air switch event info = " + JSON.toJSONString(list));
        DBCollection collection = mongoTemplate.getCollection("air_switch_event_record");
        BasicDBObject basicDBObject  = new BasicDBObject();
        list.forEach(m->{
            basicDBObject.put("deviceId", m.getDeviceId());
            basicDBObject.put("time", m.getTime());
            basicDBObject.put("type", m.getType());
            basicDBObject.put("alarmType", m.getAlarmType());
            basicDBObject.put("userId", m.getUserId());
            basicDBObject.put("tenantId", m.getTenantId());
            BasicDBObject update = new BasicDBObject();
            update.put("deviceId", m.getDeviceId());
            update.put("type", m.getType());
            update.put("alarmType", m.getAlarmType());
            update.put("alarmDesc", m.getAlarmDesc());
            update.put("time", m.getTime());
            update.put("day", m.getDay());
            update.put("hour", m.getHour());
            update.put("userId", m.getUserId());
            update.put("tenantId", m.getTenantId());

            collection.update(basicDBObject,update,true,false);
        });
        return true;
    }

    @Override
    public List<AirSwitchEventRsp> queryList(AirSwitchEventReq req) {
        List<AirSwitchEventRsp> list = Lists.newArrayList();
        DBCollection collection = mongoTemplate.getCollection("air_switch_event_record");
        BasicDBObject query  = new BasicDBObject();

        if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
            query.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
        }
        if (!Strings.isNullOrEmpty(req.getDeviceId())) {
            query.put("deviceId", req.getDeviceId());
        }
        if (!Strings.isNullOrEmpty(req.getDay())) {
            query.put("day", req.getDay());
        }
        if (!Strings.isNullOrEmpty(req.getHour())) {
            query.put("hour", req.getHour());
        }
        if (!Strings.isNullOrEmpty(req.getType())) {
            query.put("type", req.getType());
        }
        if (req.getAlarmType() != null) {
            query.put("alarmType", req.getAlarmType());
        }
        if (req.getTenantId() != null) {
            query.put("tenantId", req.getTenantId());
        }
        if (req.getUserId() != null) {
            query.put("userId", req.getUserId());
        }

        DBCursor cursor = collection.find(query);
        cursor.forEach(m-> {
            AirSwitchEventRsp event = new AirSwitchEventRsp();
            event.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);
            event.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
            event.setUserId(m.get("userId") != null ? Long.parseLong(m.get("userId").toString()) : null);
            event.setDay(m.get("day") != null ? m.get("day").toString() : null);
            event.setHour(m.get("hour") != null ? m.get("hour").toString() : null);
            event.setType(m.get("type") != null ? m.get("type").toString() : null);
            event.setAlarmDesc(m.get("alarmDesc") != null ? m.get("alarmDesc").toString() : null);
            event.setAlarmType(m.get("alarmType") != null ? Integer.parseInt(m.get("alarmType").toString()) : null);
            event.setTime(m.get("time") != null ? Long.parseLong(m.get("time").toString()) : null);

            list.add(event);
        });
        return list;
    }

    @Override
    public List<AirSwitchHourEventStatisticsResp> queryHourEventList(AirSwitchHourEventStatisticsReq req) {
        List<AirSwitchHourEventStatisticsResp> list = Lists.newArrayList();
        DBCollection collection = mongoTemplate.getCollection("air_switch_hour_event_statistics");
        BasicDBObject query  = new BasicDBObject();
        if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
            query.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
        }
        if (!Strings.isNullOrEmpty(req.getDeviceId())) {
            query.put("deviceId", req.getDeviceId());
        }
        if (req.getTenantId() != null) {
            query.put("tenantId", req.getTenantId());
        }
        if (!Strings.isNullOrEmpty(req.getType())) {
            query.put("type", req.getType());
        }
        if (req.getAlarmType() != null) {
            query.put("alarmType", req.getAlarmType());
        }
        if (!Strings.isNullOrEmpty(req.getDay())) {
            query.put("day", req.getDay());
        }
        if (req.getHour() != null) {
            query.put("hour", req.getHour());
        }
        DBCursor cursor = collection.find(query);
        cursor.forEach(m-> {
            AirSwitchHourEventStatisticsResp statistics = new AirSwitchHourEventStatisticsResp();
            statistics.setDay(m.get("day") != null ? m.get("day").toString() : null);
            statistics.setType(m.get("type") != null ? m.get("type").toString() : null);
            statistics.setAlarmType(m.get("alarmType") != null ? Integer.parseInt(m.get("alarmType").toString()) : null);
            statistics.setHour(m.get("hour") != null ? Integer.parseInt(m.get("hour").toString()) : null);
            statistics.setCount(m.get("count") != null ? Integer.parseInt(m.get("count").toString()) : null);
            statistics.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
            statistics.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);

            list.add(statistics);
        });

        return list;
    }


    @Override
    public Boolean insertOrUpdateHourEventBatch(List<AirSwitchHourEventStatisticsReq> list) {

        DBCollection collection = mongoTemplate.getCollection("air_switch_hour_event_statistics");
        BasicDBObject basicDBObject  = new BasicDBObject();
        list.forEach(e-> {
            basicDBObject.put("day", e.getDay());
            basicDBObject.put("hour", e.getHour());
            basicDBObject.put("type", e.getType());
            basicDBObject.put("alarmType", e.getAlarmType());
            basicDBObject.put("tenantId", e.getTenantId());
            basicDBObject.put("deviceId", e.getDeviceId());
            BasicDBObject update = new BasicDBObject();
            update.put("day", e.getDay());
            update.put("hour", e.getHour());
            update.put("type", e.getType());
            update.put("alarmType", e.getAlarmType());
            update.put("count", e.getCount());
            update.put("tenantId", e.getTenantId());
            update.put("deviceId", e.getDeviceId());
            update.put("time", e.getTime());

            collection.update(basicDBObject,update,true,false);
        });
        return true;
    }

    @Override
    public List<AirSwitchDailyEventStatisticsResp> queryDailyEventList(AirSwitchDailyEventStatisticsReq req) {
        List<AirSwitchDailyEventStatisticsResp> list = Lists.newArrayList();
        DBCollection collection = mongoTemplate.getCollection("air_switch_daily_event_statistics");
        BasicDBObject query  = new BasicDBObject();
        if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
            query.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
        }
        if (!Strings.isNullOrEmpty(req.getDeviceId())) {
            query.put("deviceId", req.getDeviceId());
        }
        if (req.getTenantId() != null) {
            query.put("tenantId", req.getTenantId());
        }
        if (!Strings.isNullOrEmpty(req.getType())) {
            query.put("type", req.getType());
        }
        if (req.getAlarmType() != null) {
            query.put("alarmType", req.getAlarmType());
        }
        if (!Strings.isNullOrEmpty(req.getDay())) {
            query.put("day", req.getDay());
        }
        if (!Strings.isNullOrEmpty(req.getYearMonth())) {
            query.put("yearMonth", req.getYearMonth());
        }
        DBCursor cursor = collection.find(query);
        cursor.forEach(m-> {
            AirSwitchDailyEventStatisticsResp statistics = new AirSwitchDailyEventStatisticsResp();
            statistics.setDay(m.get("day") != null ? m.get("day").toString() : null);
            statistics.setType(m.get("type") != null ? m.get("type").toString() : null);
            statistics.setAlarmType(m.get("alarmType") != null ? Integer.parseInt(m.get("alarmType").toString()) : null);
            statistics.setYearMonth(m.get("yearMonth") != null ? m.get("yearMonth").toString() : null);
            statistics.setCount(m.get("count") != null ? Integer.parseInt(m.get("count").toString()) : null);
            statistics.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
            statistics.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);

            list.add(statistics);
        });

        return list;
    }

    @Override
    public Boolean insertOrUpdateDailyEventBatch(List<AirSwitchDailyEventStatisticsReq> list) {
        DBCollection collection = mongoTemplate.getCollection("air_switch_daily_event_statistics");
        BasicDBObject basicDBObject  = new BasicDBObject();
        list.forEach(e-> {
            basicDBObject.put("day", e.getDay());
            basicDBObject.put("type", e.getType());
            basicDBObject.put("alarmType", e.getAlarmType());
            basicDBObject.put("tenantId", e.getTenantId());
            basicDBObject.put("deviceId", e.getDeviceId());
            basicDBObject.put("yearMonth", e.getYearMonth());
            BasicDBObject update = new BasicDBObject();
            update.put("day", e.getDay());
            update.put("type", e.getType());
            update.put("alarmType", e.getAlarmType());
            update.put("count", e.getCount());
            update.put("tenantId", e.getTenantId());
            update.put("deviceId", e.getDeviceId());
            update.put("yearMonth", e.getYearMonth());
            update.put("time", e.getTime());

            collection.update(basicDBObject,update,true,false);
        });
        return true;
    }

    @Override
    public Boolean insertOrUpdateMonthEventBatch(List<AirSwitchMonthEventStatisticsReq> list) {
        DBCollection collection = mongoTemplate.getCollection("air_switch_month_event_statistics");
        BasicDBObject basicDBObject  = new BasicDBObject();
        list.forEach(e-> {
            basicDBObject.put("day", e.getDay());
            basicDBObject.put("year", e.getYear());
            basicDBObject.put("month", e.getMonth());
            basicDBObject.put("type", e.getType());
            basicDBObject.put("alarmType", e.getAlarmType());
            basicDBObject.put("tenantId", e.getTenantId());
            basicDBObject.put("deviceId", e.getDeviceId());
            BasicDBObject update = new BasicDBObject();
            update.put("day", e.getDay());
            update.put("type", e.getType());
            update.put("alarmType", e.getAlarmType());
            update.put("year", e.getYear());
            update.put("month", e.getMonth());
            update.put("count", e.getCount());
            update.put("deviceId", e.getDeviceId());
            update.put("tenantId", e.getTenantId());
            update.put("time", e.getTime());

            collection.update(basicDBObject,update,true,false);
        });
        return true;
    }

    @Override
    public List<AirSwitchMonthEventStatisticsResp> queryMonthEventList(AirSwitchMonthEventStatisticsReq req) {
        List<AirSwitchMonthEventStatisticsResp> list = Lists.newArrayList();
        DBCollection collection = mongoTemplate.getCollection("air_switch_month_event_statistics");
        BasicDBObject query  = new BasicDBObject();
        if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
            query.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
        }
        if (!Strings.isNullOrEmpty(req.getDeviceId())) {
            query.put("deviceId", req.getDeviceId());
        }
        if (req.getTenantId() != null) {
            query.put("tenantId", req.getTenantId());
        }
        if (!Strings.isNullOrEmpty(req.getType())) {
            query.put("type", req.getType());
        }
        if (req.getAlarmType() != null) {
            query.put("alarmType", req.getAlarmType());
        }
        if (!Strings.isNullOrEmpty(req.getMonth())) {
            query.put("month", req.getMonth());
        }
        if (req.getYear() != null) {
            query.put("year", req.getYear());
        }
        DBCursor cursor = collection.find(query);
        cursor.forEach(m-> {
            AirSwitchMonthEventStatisticsResp statistics = new AirSwitchMonthEventStatisticsResp();
            statistics.setDay(m.get("day") != null ? m.get("day").toString() : null);
            statistics.setType(m.get("type") != null ? m.get("type").toString() : null);
            statistics.setAlarmType(m.get("alarmType") != null ? Integer.parseInt(m.get("alarmType").toString()) : null);
            statistics.setYear(m.get("year") != null ? Integer.parseInt(m.get("year").toString()) : null);
            statistics.setMonth(m.get("month") != null ? m.get("month").toString() : null);
            statistics.setCount(m.get("count") != null ? Integer.parseInt(m.get("count").toString()) : null);
            statistics.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
            statistics.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);

            list.add(statistics);
        });

        return list;
    }
}
