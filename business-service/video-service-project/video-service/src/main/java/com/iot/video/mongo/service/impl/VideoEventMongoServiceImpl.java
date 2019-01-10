package com.iot.video.mongo.service.impl;

import com.iot.common.util.NumberUtil;
import com.iot.common.util.StringUtil;
import com.iot.enums.DataStatusEnum;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.EventParamDto;
import com.iot.video.dto.VideoEventParamDto;
import com.iot.video.entity.VideoEvent;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.repository.VideoEventRepository;
import com.iot.video.mongo.service.VideoEventMongoService;
import com.iot.video.mongo.vo.MongoPageInfo;
import com.iot.video.service.VideoManageService;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 18:09
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 18:09
 * 修改描述：
 */
@Service
public class VideoEventMongoServiceImpl implements VideoEventMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private VideoEventRepository videoEventRepository;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private VideoManageService videoManageService;

    /**
     * @despriction：获取视频事件信息
     * @author  yeshiyuan
     * @created 2018/5/10 13:49
     * @param planId 计划id
     * @param eventUuid 事件uuid
     * @return
     */
    @Override
    public VideoEventEntity getEventByPlanIdAndEventUuid(String planId, String eventUuid) {
        return videoEventRepository.findByPlanIdAndEventUuidAndDataStatus(planId, eventUuid, DataStatusEnum.VALID.getCode());
    }

    @Override
    public int updateVideoEvent(VideoEvent videoEvent) {
        Query query = new Query();
        Criteria criteria = Criteria.where("planId").is(videoEvent.getPlanId())
                .and("eventOddurTime").is(videoEvent.getEventOddurTime())
                .and("eventUuid").is(videoEvent.getEventUuid())
                .and("dataStatus").is(DataStatusEnum.VALID.getCode());
        query.addCriteria(criteria);
        Update update = new Update().set("eventCode",videoEvent.getEventCode())
                .set("eventName",videoEvent.getEventName())
                .set("eventDesc", videoEvent.getEventDesc())
                .set("eventStatus", videoEvent.getEventStatus())
                .set("updateTime", videoEvent.getUpdateTime());
        WriteResult result = mongoTemplate.updateFirst(query,update, VideoEventEntity.class);
        return result.getN();
    }

    @Override
    public void insertVideoEvent(VideoEventEntity videoEvent) {
        this.mongoTemplate.insert(videoEvent);
    }


    /**
     * @despriction：统计某计划的已录事件数
     * @author  yeshiyuan
     * @created 2018/6/13 14:38
     * @param tenantId 租户id
     * @param planId 计划id
     * @return
     */
    @Override
    public int countVideoEventByPlanId(String planId) {
        AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("planId").is(planId).and("dataStatus").is(DataStatusEnum.VALID.getCode())),
                        Aggregation.count().as("usedEventNum")
                )
                , ModuleConstants.TABLE_VIDEO_EVENT, BasicDBObject.class
        );

        BasicDBObject dbObject = results.getUniqueMappedResult();
        return dbObject == null? 0:dbObject.getInt("usedEventNum");
    }

    /**
     * 描述：设置录影事件无效
     * @author yeshiyuan
     * @created 2018/6/13 14:38
     * @param planId 计划id
     * @return void
     */
    @Override
    public int setVideoEventDataInvalid(String planId){
        Query query = Query.query(Criteria.where("planId").is(planId)
                .and("dataStatus").is(DataStatusEnum.VALID.getCode())
        );
        Update update = Update.update("dataStatus",DataStatusEnum.UN_VALID.getCode());
        WriteResult result = mongoTemplate.updateMulti(query, update, ModuleConstants.TABLE_VIDEO_EVENT);
        return result.getN();
    }

    /**
     * @despriction：删除失效视频事件数据
     * @author  yeshiyuan
     * @created 2018/8/7 11:28
     * @param planId 计划id
     * @return
     */
    @Override
    public int deleteInvalidVideoEventData(String planId) {
        Query query = Query.query(Criteria.where("planId").is(planId).and("dataStatus").is(DataStatusEnum.UN_VALID.getCode()));
        WriteResult result = mongoTemplate.remove(query, ModuleConstants.TABLE_VIDEO_EVENT);
        return result.getN();
    }

    /**
     * @despriction：找到某一时间前的事件uuid
     * @author  yeshiyuan
     * @created 2018/6/12 19:14
     * @param null
     * @return
     */
    public List<String> selectEventUuidsByTime(String planId, Date eventOddurTime){
        DBObject queryObject = new BasicDBObject();
        queryObject.put("planId", planId);
        queryObject.put("eventOddurTime", new BasicDBObject("$lte",eventOddurTime));
        queryObject.put("dataStatus", DataStatusEnum.VALID.getCode());
        DBObject fileObject = new BasicDBObject();
        fileObject.put("eventUuid",1);
        fileObject.put("_id",0);
        Query query = new BasicQuery(queryObject,fileObject);
        List<VideoEventEntity> list = mongoTemplate.find(query, VideoEventEntity.class);
        return list.stream().map(o -> {
            return o.getEventUuid();
        }).collect(Collectors.toList());
    }

    /**
     * @despriction：某一时间前的事件置为失效
     * @author  yeshiyuan
     * @created 2018/6/12 18:48
     * @param tenantId 租户id
     * @param planId 计划id
     * @return
     */
    @Override
    public int setVideoEventDataInvalid(String planId, Date eventTime) {
        Query query = Query.query(Criteria.where("planId").is(planId)
                .and("eventOddurTime").lte(eventTime)
                .and("dataStatus").is(DataStatusEnum.VALID.getCode()));
        Update update = Update.update("dataStatus",DataStatusEnum.UN_VALID.getCode());
        WriteResult result = mongoTemplate.updateMulti(query, update, ModuleConstants.TABLE_VIDEO_EVENT);
        return result.getN();
    }

    /**
     * @despriction：批量插入videoEvent
     * @author  yeshiyuan
     * @created 2018/8/6 19:15
     * @return
     */
    @Override
    public void bacthInsert(List<VideoEventEntity> list) {
        mongoTemplate.insert(list, VideoEventEntity.class);
    }

    /**
     * 描述：获取事件计划临界时间点
     * @author yeshiyuan
     * @created 2018/6/12 17:51
     * @param tenantId 租户id
     * @param planId 计划id
     * @return java.util.Date
     */
    @Override
    public Date getVideoLimitTime(String planId) {
        String num = RedisCacheUtil.hashGet(ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR, String.class);
        int packageNum = NumberUtil.toInt(num, 0);
        if(StringUtil.isEmpty(num)){
            Long tenantId = videoManageService.getTenantId(planId);
            Long packageId = videoManageService.getPackageInfo(tenantId, planId);
            GoodsInfo goodsInfo = null;
            if(null != packageId){
                goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(packageId);
            }
            if(goodsInfo == null || StringUtil.isBlank(goodsInfo.getStandard()) ){
                return null;
            }
            packageNum = Integer.parseInt(goodsInfo.getStandard());
        }
        if(packageNum == 0){
            return null;
        }
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put("planId", planId);
        BasicDBObject fileObject = new BasicDBObject().append("eventOddurTime",1).append("_id",0);
        Query query = new BasicQuery(queryObject,fileObject);
        query.with(new Sort(Sort.Direction.DESC,"eventOddurTime")).skip(packageNum).limit(1);
        VideoEventEntity eventEntity = mongoTemplate.findOne(query, VideoEventEntity.class);
        return eventEntity == null ? null : eventEntity.getEventOddurTime();
    }

    /**
     * 描述：查询一段时间的事件列表
     * @author yeshiyuan
     * @created 2018/8/9 14:39
     * @param vespDto 查询参数
     * @return java.util.List<com.lds.iot.video.dto.VideoEventDto>
     */
    @Override
    public MongoPageInfo<VideoEventEntity> getVideoEventList(VideoEventParamDto vespDto) {
        DBObject queryObject = new BasicDBObject().append("planId", vespDto.getPlanId());
        DBObject timeObject = new BasicDBObject()
                .append("$gte", vespDto.getStartTime())
                .append("$lte",vespDto.getEndTime());
        queryObject.put("eventOddurTime", timeObject);
        queryObject.put("dataStatus", DataStatusEnum.VALID.getCode());
        DBObject filedObject = new BasicDBObject().append("_id",0)
                .append("eventUuid", 1)
                .append("eventCode", 1)
                .append("eventDesc",1)
                .append("eventOddurTime",1);
        Query query = new BasicQuery(queryObject, filedObject);
        Long totalCount = mongoTemplate.count(query, ModuleConstants.TABLE_VIDEO_EVENT);
        //创建分页模板Pageable
        Pageable pageable = new PageRequest(vespDto.getPageNum()==0 ? 0:vespDto.getPageNum()-1, vespDto.getPageSize(), new Sort(Sort.Direction.DESC,"eventOddurTime"));
        List<VideoEventEntity> list =  mongoTemplate.find(query.with(pageable), VideoEventEntity.class);
        return new MongoPageInfo<VideoEventEntity>(totalCount, vespDto.getPageNum(), vespDto.getPageSize(), list);
    }

    /**
     * 描述：查询一段时间的事件数量
     * @author yeshiyuan
     * @date 2018/8/9 14:30
     * @param vespDto 查询参数
     * @return
     */
    @Override
    public Long getVideoEventCount(VideoEventParamDto vespDto) {
        DBObject queryObject = new BasicDBObject().append("planId", vespDto.getPlanId())
                .append("dataStatus", DataStatusEnum.VALID.getCode());
        DBObject timeObject = new BasicDBObject()
                .append("$gte", vespDto.getStartTime())
                .append("$lte",vespDto.getEndTime());
        queryObject.put("eventOddurTime", timeObject);
        DBObject filedObject = new BasicDBObject().append("_id",0)
                .append("eventUuid", 1);
        Query query = new BasicQuery(queryObject, filedObject);
        return mongoTemplate.count(query, ModuleConstants.TABLE_VIDEO_EVENT);
    }

    /**
     * 描述：获取视频事件信息
     * @author yeshiyuan
     * @created 2018年8月9日 上午10:08:51
     * @param tenantId 租户id
     * @param userId 用户id
     * @param planId 计划id
     * @param eventId 事件id
     * @return
     */
    @Override
    public VideoEventEntity findByPlanIdAndEventId(Long tenantId, String planId, String eventId) {
        Query query = Query.query(Criteria.where("planId").is(planId)
                .and("eventUuid").is(eventId)
                .and("tenantId").is(tenantId)
                .and("dataStatus").is(DataStatusEnum.VALID.getCode()));
        return mongoTemplate.findOne(query, VideoEventEntity.class);
    }

    /**
     * 描述：获取事件图片URL列表
     * @author yeshiyuan
     * @created 2018/3/23 15:29
     * @param epDto 查询参数VO
     * @return
     */
    @Override
    public MongoPageInfo<VideoEventEntity> getEventPhotoList(EventParamDto param) {
        DBObject queryObject = new BasicDBObject().append("planId", param.getPlanId());
        DBObject timeObject = new BasicDBObject()
                .append("$gte", param.getEventStartTime())
                .append("$lte",param.getEventEndTime());
        queryObject.put("eventOddurTime", timeObject);
        queryObject.put("dataStatus",DataStatusEnum.VALID.getCode());
        DBObject fieldObject = new BasicDBObject().append("_id",0)
                .append("eventUuid",1)
                .append("eventOddurTime", 1);
        Query query = new BasicQuery(queryObject, fieldObject);
        Long total = mongoTemplate.count(query,ModuleConstants.TABLE_VIDEO_EVENT);
        Pageable pageable = new PageRequest(param.getPageNum()==0 ? 0 : param.getPageNum()-1, param.getPageSize(), new Sort(Sort.Direction.DESC, "eventOddurTime"));
        List<VideoEventEntity> list = mongoTemplate.find(query.with(pageable), VideoEventEntity.class);
        return new MongoPageInfo<VideoEventEntity>(total, param.getPageSize(), param.getPageNum(), list);
    }

    /**
     * @despriction：查询有事件录影的日期
     * @author  yeshiyuan
     * @created 2018/7/26 11:38
     * @return
     */
    @Override
    public List<String> selectHasEventVideoDay(String planId, Date startDate, Date endDate) {
        List<String> days = new ArrayList<>();
        BasicDBObject filedObject = new BasicDBObject()
                .append("$dateToString",new BasicDBObject().append("format","%Y-%m-%d").append("date","$eventOddurTime"));
        AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("planId").is(planId).and("eventOddurTime").gte(startDate).lte(endDate).and("dataStatus").is(DataStatusEnum.VALID.getCode())),
                        Aggregation.group().addToSet(filedObject).as("hasEventDay")
                ),
                ModuleConstants.TABLE_VIDEO_EVENT, BasicDBObject.class
        );
        BasicDBObject dbObject = results.getUniqueMappedResult();
        if (dbObject != null && dbObject.containsField("hasEventDay")){
            BasicDBList dbList = (BasicDBList)dbObject.get("hasEventDay");
            dbList.forEach(o->{
                days.add(o.toString());
            });
        }
        return days;
    }

    /**
     * @despriction：删除事件信息
     * @author  yeshiyuan
     * @created 2018/8/15 11:06
     * @param null
     * @return
     */
    @Override
    public int deleteByPlanIdAndEventUuidAndDataStatus(String planId, String eventUuid, Integer dataStatus) {
        return videoEventRepository.deleteByPlanIdAndEventUuidAndDataStatus(planId, eventUuid, dataStatus);
    }
}
