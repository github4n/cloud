package com.iot.control.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.control.activity.domain.ActivityRecord;
import com.iot.control.activity.exception.BusinessExceptionEnum;
import com.iot.control.activity.mapper.ActivityRecordMapper;
import com.iot.control.activity.service.ActivityRecordMongoService;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import com.iot.saas.SaaSContextHolder;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ActivityRecordMongoServiceImpl implements ActivityRecordMongoService {

    private final static Logger logger = LoggerFactory.getLogger(ActivityRecordMongoServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ActivityRecordMapper activityRecordMapper;


    public void saveActivityRecord(List<ActivityRecordReq> activityRecordReqList) {
        if (CollectionUtils.isEmpty(activityRecordReqList)) {
            logger.error("----saveActivityRecord() error! activityRecordReq is null");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
        logger.debug("saveActivityRecord.is.start {} "+ JSON.toJSONString(activityRecordReqList));
//        MongoClient mongoClient = new MongoClient("192.168.6.109",27017);
//        DB mongoDatabase = mongoClient.getDB("iot_db_control");
//        DBCollection collection = mongoTemplate.getCollection("activity_record")
        CompletableFuture<String> completableFuture=CompletableFuture.supplyAsync(()->{
            try {
                //模拟执行耗时任务
                List<ActivityRecord> list = new ArrayList<>();
                activityRecordReqList.forEach(m->{
                    ActivityRecord ar = new ActivityRecord();
                    BeanUtils.copyProperties(m, ar);
                    if (m.getTenantId() == null) {
                        ar.setTenantId(SaaSContextHolder.currentTenantId());
                    }
                    ar.setTime(new Date().getTime());
                    ar.setDelFlag(0);
                    if (StringUtils.isEmpty(m.getDeviceName())){
                        ar.setDeviceName("");
                    }
                    list.add(ar);
                });
                mongoTemplate.insertAll(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //返回结果
            return "200";
        });
    }

    @Override
    public int delActivityRecord(ActivityRecordReq activityRecordReq) {
        DBCollection collection = mongoTemplate.getCollection("activity_record");
        BasicDBObject basicDBObject = new BasicDBObject();
        if (StringUtils.isNotEmpty(activityRecordReq.getType())){
            basicDBObject.put("type",activityRecordReq.getType());
        }
        if (StringUtils.isNotBlank(activityRecordReq.getForeignId())){
            basicDBObject.put("foreignId",activityRecordReq.getForeignId());
        }
        if (activityRecordReq.getCreateBy()!=null){
            basicDBObject.put("createBy",activityRecordReq.getCreateBy());
        }
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("delFlag",1);
        collection.update(basicDBObject,new BasicDBObject("$set",updateObject),false,true);
        return 200;
    }

    @Override
    public PageInfo<ActivityRecordResp> queryActivityRecord(ActivityRecordReq activityRecordReq) {
        DBCollection collection = mongoTemplate.getCollection("activity_record");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("delFlag",0);
        if (activityRecordReq.getTime()!=null){
            basicDBObject.put("time",new BasicDBObject("$gte",activityRecordReq.getTime()));
        }
        if (StringUtils.isNotBlank(activityRecordReq.getStartTime()) && StringUtils.isNotBlank(activityRecordReq.getEndTime())) {
            Map<String, Object> timeMap = Maps.newHashMap();
            timeMap.put(QueryOperators.GTE, Long.parseLong(activityRecordReq.getStartTime()));
            timeMap.put(QueryOperators.LTE, Long.parseLong(activityRecordReq.getEndTime()));
            basicDBObject.put("time", new BasicDBObject(timeMap));
        }
        if (activityRecordReq.getCreateBy()!=null){
            basicDBObject.put("createBy",activityRecordReq.getCreateBy());
        }
        if (StringUtils.isNotBlank(activityRecordReq.getForeignId())){
            basicDBObject.put("foreignId",activityRecordReq.getForeignId());
        }
        if (StringUtils.isNotBlank(activityRecordReq.getType())){
            basicDBObject.put("type",activityRecordReq.getType());
        }
        if (activityRecordReq.getTenantId()!=null){
            basicDBObject.put("tenantId",activityRecordReq.getTenantId());
        }
        DBCursor cursor = collection.find(basicDBObject).skip((activityRecordReq.getPageNum()-1)*activityRecordReq.getPageSize()).limit(activityRecordReq.getPageSize()).sort(new BasicDBObject("_id",-1));
        List list = new ArrayList();
        cursor.forEach(m->{
            ActivityRecordResp activityRecordResp =  new ActivityRecordResp();
            activityRecordResp.setId(m.get("_id")!=null?Long.valueOf(m.get("_id").toString()):null);
            activityRecordResp.setType(m.get("type")!=null?m.get("type").toString():null);
            activityRecordResp.setIcon(m.get("icon")!=null?m.get("icon").toString():null);
            activityRecordResp.setActivity(m.get("activity")!=null?m.get("activity").toString():null);
            activityRecordResp.setTime(m.get("time")!=null?new Long(m.get("time").toString()):null);
            activityRecordResp.setCreateBy(m.get("createBy")!=null?Long.valueOf(m.get("createBy").toString()):null);
            activityRecordResp.setForeignId(m.get("foreignId")!=null?m.get("foreignId").toString():null);
            activityRecordResp.setDelFlag(m.get("delFlag")!=null?Integer.valueOf(m.get("delFlag").toString()):null);
            activityRecordResp.setTenantId(m.get("tenantId")!=null?m.get("tenantId").toString():null);
            activityRecordResp.setDeviceName(m.get("deviceName")!=null?m.get("deviceName").toString():null);
            list.add(activityRecordResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setStartRow(activityRecordReq.getPageNum()*activityRecordReq.getPageSize());
        pageInfo.setPageNum(activityRecordReq.getPageNum());
        pageInfo.setPageSize(activityRecordReq.getPageSize());
        pageInfo.setTotal(cursor.count());
        Double pages = Math.ceil(pageInfo.getTotal()/pageInfo.getPageSize());
        pageInfo.setPages(pages.intValue());
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<ActivityRecordResp> queryScheduleLog(ActivityRecordReq req) {
        DBCollection collection = mongoTemplate.getCollection("activity_record");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("tenantId", req.getTenantId());
        basicDBObject.put("foreignId", req.getForeignId());
        String[] types = new String[] {"scene", "ifttt", "location", "ifttt_template"};
        basicDBObject.put("type", new BasicDBObject(QueryOperators.IN, types));
        basicDBObject.put("time", new BasicDBObject(QueryOperators.GTE, Long.parseLong(req.getStartTime())));
        basicDBObject.put("time", new BasicDBObject(QueryOperators.LTE, Long.parseLong(req.getEndTime())));
        DBCursor cursor = collection.find(basicDBObject).limit(req.getPageSize()).sort(new BasicDBObject("_id",-1));
        List<DBObject> mapList = cursor.toArray();
        List<ActivityRecordResp> resultList = assembleList(mapList);
        return resultList;
    }

    private List<ActivityRecordResp> assembleList(List<DBObject> mapList) {
        List<ActivityRecordResp> list = Lists.newArrayList();
        mapList.forEach(m->{
            ActivityRecordResp activityRecordResp =  new ActivityRecordResp();
            activityRecordResp.setType(m.get("type")!=null?m.get("type").toString():null);
            activityRecordResp.setId(m.get("id")!=null?Long.valueOf(m.get("_id").toString()):null);
            activityRecordResp.setIcon(m.get("icon")!=null?m.get("icon").toString():null);
            activityRecordResp.setActivity(m.get("activity")!=null?m.get("activity").toString():null);
            try {
                activityRecordResp.setTime(m.get("time")!=null?new Long(m.get("time").toString()):null);
                activityRecordResp.setSetTime(m.get("setTime")!=null?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.get("setTime").toString()):null);
            } catch (Exception e){
                e.printStackTrace();
            }
            activityRecordResp.setTemplateName(m.get("templateName")!=null?m.get("templateName").toString():null);
            activityRecordResp.setCreateBy(m.get("createBy")!=null?Long.valueOf(m.get("createBy").toString()):null);
            activityRecordResp.setForeignId(m.get("foreignId")!=null?m.get("foreignId").toString():null);
            activityRecordResp.setDelFlag(m.get("delFlag")!=null?Integer.valueOf(m.get("delFlag").toString()):null);
            activityRecordResp.setDeviceName(m.get("deviceName")!=null?m.get("deviceName").toString():null);
            activityRecordResp.setResult(m.get("result")!=null?Integer.valueOf(m.get("result").toString()):null);
            activityRecordResp.setUserId(m.get("userId")!=null?Long.valueOf(m.get("userId").toString()):null);
            list.add(activityRecordResp);
        });
        return list;
    }


    List<ActivityRecordResp> queryActivityRecordByConditionWithMongo(ActivityRecordReq activityRecordReq){
        List<ActivityRecordResp> list = activityRecordMapper.queryActivityRecordByConditionWithMongo(activityRecordReq.getTenantId(),
                activityRecordReq.getSpaceIds().toString().substring(1,activityRecordReq.getSpaceIds().toString().length()-1),
                activityRecordReq.getSpaceTemplateIds().toString().substring(1,activityRecordReq.getSpaceTemplateIds().toString().length()-1));
        return list;
    }

    @Override
    public List<ActivityRecordResp> queryActivityRecordByCondition(ActivityRecordReq activityRecordReq) {
        List<ActivityRecordResp> list  = this.queryActivityRecordByConditionWithMongo(activityRecordReq);
        DBCollection collection = mongoTemplate.getCollection("activity_record");
        BasicDBObject basicDBObject = new BasicDBObject();
        List result = new ArrayList();
        if (list.size()>0){
            basicDBObject.put("tenantId",SaaSContextHolder.currentTenantId());
            BasicDBList basicDBList = new BasicDBList();
            list.forEach(m->{
                basicDBList.add(m.getTemplateId());
            });
            String[] strings = new String[basicDBList.size()];
            for (int i=0;i<basicDBList.size();i++){
                strings[i]=basicDBList.get(i).toString();
            }
            basicDBObject.put("foreignId",new BasicDBObject(QueryOperators.IN,strings));
            DBCursor cursor = collection.find(basicDBObject).limit(activityRecordReq.getPageSize()).sort(new BasicDBObject("_id",-1));
            List<DBObject> mapList = cursor.toArray();
            mapList.forEach(m->{
                ActivityRecordResp activityRecordResp =  new ActivityRecordResp();
                activityRecordResp.setId(m.get("id")!=null?Long.valueOf(m.get("_id").toString()):null);
                activityRecordResp.setType(m.get("type")!=null?m.get("type").toString():null);
                activityRecordResp.setIcon(m.get("icon")!=null?m.get("icon").toString():null);
                activityRecordResp.setActivity(m.get("activity")!=null?m.get("activity").toString():null);
                try {
                    activityRecordResp.setTime(m.get("time")!=null?new Long(m.get("time").toString()):null);
                    activityRecordResp.setSetTime(m.get("setTime")!=null?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.get("setTime").toString()):null);
                } catch (Exception e){
                    e.printStackTrace();
                }
                activityRecordResp.setCreateBy(m.get("createBy")!=null?Long.valueOf(m.get("createBy").toString()):null);
                activityRecordResp.setForeignId(m.get("foreignId")!=null?m.get("foreignId").toString():null);
                activityRecordResp.setDelFlag(m.get("delFlag")!=null?Integer.valueOf(m.get("delFlag").toString()):null);
                activityRecordResp.setDeviceName(m.get("deviceName")!=null?m.get("deviceName").toString():null);
                activityRecordResp.setResult(m.get("result")!=null?Integer.valueOf(m.get("result").toString()):null);
                activityRecordResp.setUserId(m.get("userId")!=null?Long.valueOf(m.get("userId").toString()):null);
                result.add(activityRecordResp);
            });
        }
        return result;
    }
}
