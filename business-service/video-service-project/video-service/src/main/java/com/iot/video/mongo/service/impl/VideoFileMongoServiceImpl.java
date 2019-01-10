package com.iot.video.mongo.service.impl;

import com.iot.enums.DataStatusEnum;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.VideoFileParamDto;
import com.iot.video.dto.VideoTsFileDto;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.mongo.service.VideoFileMongoService;
import com.iot.video.vo.VideoEventJpg;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 项目名称：cloud
 * 功能描述：videoFile的操作
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 19:13
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 19:13
 * 修改描述：
 */
@Service
public class VideoFileMongoServiceImpl implements VideoFileMongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @despriction：批量插入videofile
     * @author  yeshiyuan
     * @created 2018/8/6 19:15
     * @param list 数据集
     * @return
     */
    @Override
    public void bacthInsert(List<VideoFileEntity> list) {
        mongoTemplate.insertAll(list);
    }


    /**
     * 描述：设置录影文件无效
     * @author yeshiyuan
     * @created 2018/6/13 14:38
     * @param tenantId 租户id
     * @param planId 计划id
     * @return void
     */
    public int setVideoFileDataInvalid(String planId){
        Query query = new Query(
                Criteria.where("planId").is(planId)
                .and("dataStatus").is(DataStatusEnum.VALID.getCode())
        );
        Update update = Update.update("dataStatus",DataStatusEnum.UN_VALID.getCode());
        WriteResult result = mongoTemplate.updateMulti(query, update, ModuleConstants.TABLE_VIDEO_FILE);
        return result.getN();
    }

    @Override
    public List<String> findInvalidVideoFilePath(String planId) {
        List<String> filePaths = new ArrayList<>();
        DBObject queryObject = new BasicDBObject();
        queryObject.put("planId", planId);
        queryObject.put("dataStatus", DataStatusEnum.UN_VALID.getCode());
        BasicDBObject fieldsObject=new BasicDBObject();
        fieldsObject.put("filePath", 1);
        fieldsObject.put("_id", 0);
        DBCursor cursor = mongoTemplate.getCollection(ModuleConstants.TABLE_VIDEO_FILE).find(queryObject, fieldsObject).addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        while (cursor.hasNext()){
            filePaths.add(cursor.next().get("filePath").toString());
        }
        cursor.close();
        return filePaths;
    }

    /**
     * @despriction：某一时间前的文件置为失效
     * @author  yeshiyuan
     * @created 2018/6/12 18:48
     * @param planId 计划id
     * @return
     */
    @Override
    public int setVideoFileDataInvalid(String planId, Date videoStartTime) {
        Query query = Query.query(Criteria.where("planId").is(planId)
                    .and("videoStartTime").lte(videoStartTime)
                    .and("dataStatus").is(DataStatusEnum.VALID.getCode()));
        Update update = Update.update("dataStatus",DataStatusEnum.UN_VALID.getCode());
        WriteResult result = mongoTemplate.updateMulti(query, update, ModuleConstants.TABLE_VIDEO_FILE);
        return result.getN();
    }

    /**
     * @despriction：删除事件相关的文件信息
     * @author  yeshiyuan
     * @created 2018/8/9 15:24
     * @param null
     * @return
     */
    @Override
    public int deleteVideoEventFile(String planId, String eventUUID) {
        Query query = Query.query(Criteria.where("planId").is(planId).and("eventUuid").is(eventUUID).and("dataStatus").is(DataStatusEnum.VALID.getCode()));
        WriteResult result = mongoTemplate.remove(query, VideoFileEntity.class);
        return result.getN();
    }

    /**
     * @despriction：查询事件对应的图片
     * @author  yeshiyuan
     * @created 2018/8/9 17:10
     * @return
     */
    @Override
    public Map<String, VideoEventJpg> getVideoEventJpgPicture(String planId, List<String> eventUuids){
        Map<String, VideoEventJpg> map = new HashMap<>();
        DBObject queryObject = new BasicDBObject().append("planId", planId)
                .append("fileType","jpg")
                .append("dataStatus", DataStatusEnum.VALID.getCode());
        BasicDBList values = new BasicDBList();
        eventUuids.forEach(eventUuid ->{
            values.add(eventUuid);
        });
        queryObject.put("eventUuid", new BasicDBObject("$in", values));
        DBObject filedObject = new BasicDBObject().append("filePath",1).append("_id",0).append("eventUuid",1)
                .append("fileId",1).append("rotation", 1);
        DBCursor dbCursor = mongoTemplate.getCollection(ModuleConstants.TABLE_VIDEO_FILE).find(queryObject, filedObject);
        while (dbCursor.hasNext()){
            DBObject object=dbCursor.next();
            map.put((String) object.get("eventUuid"), new VideoEventJpg(object.get("rotation") ==null ? 0: (Integer) object.get("rotation"),
                    (String) object.get("fileId"),(String) object.get("filePath")));
        }
        return map;
    }

    /**
     * 描述：获取一段时间视频文件列表
     * @author yeshiyuan
     * @created 2018/8/9 14:39
     * @param vfpDto 查询参数
     */
    @Override
    public List<VideoFileEntity> getVideoFileList(VideoFileParamDto vfpDto){
        List<VideoFileEntity> list = new ArrayList<>();
        DBObject queryObject = new BasicDBObject().append("planId", vfpDto.getPlanId());
        DBObject videoTimeObject = new BasicDBObject();
        videoTimeObject.put("$lte", vfpDto.getEndTime());
        videoTimeObject.put("$gte", vfpDto.getStartTime());
        queryObject.put("videoStartTime", videoTimeObject);
        queryObject.put("dataStatus", DataStatusEnum.VALID.getCode());
        queryObject.put("fileType", vfpDto.getFileType());
        DBObject filedObject = new BasicDBObject().append("_id",0)
                .append("fileId",1).append("filePath",1)
                .append("videoStartTime",1).append("videoEndTime",1)
                .append("videoLength",1).append("fileSize", 1).append("rotation", 1);
        DBCursor dbCursor = mongoTemplate.getCollection(ModuleConstants.TABLE_VIDEO_FILE).find(queryObject, filedObject);
        while (dbCursor.hasNext()){
            DBObject object = dbCursor.next();
            VideoFileEntity entity = new VideoFileEntity((Date) object.get("videoStartTime"),(Date) object.get("videoEndTime"),
                (Integer)object.get("fileSize"), Float.parseFloat(object.get("videoLength").toString()),
                    (String) object.get("fileId"), (String)object.get("filePath"));
            entity.setRotation(object.get("rotation")==null ? 0: (Integer)object.get("rotation"));
            list.add(entity);
        }
        return list;
    }

    /**
     * 描述：获取计划最后一个视频
     * @author yeshiyuan
     * @created 2018/8/23 15:48
     * @param lppDto 查询参数VO
     * @return java.util.List<com.lds.iot.video.dto.PlanLastPicDto>
     */
    @Override
    public VideoFileEntity getLastTsVideoFile(String planId, String deviceId){
        DBObject queryObject = new BasicDBObject().append("planId", planId)
                .append("dataStatus",DataStatusEnum.VALID.getCode())
                .append("fileType", "ts").append("deviceId", deviceId);
        DBObject filedObject = new BasicDBObject()
                .append("fileId",1).append("_id",0)
                .append("filePath",1);
        Query query = new BasicQuery(queryObject, filedObject).with(new Sort(Sort.Direction.DESC,"videoStartTime")).limit(1);
        return mongoTemplate.findOne(query, VideoFileEntity.class);
    }

    /**
     * 描述：统计某天里哪几个小时有录影
     * @author yeshiyuan
     * @date 2018/8/13 19:25
     * @param
     * @return
     */
    @Override
    public List<Integer> countSomeDayHasVideoHour(Date startDate, Date endDate, String planId) {
        List<Integer> list = new ArrayList<>();
        BasicDBObject basicDBObject = new BasicDBObject().append("$hour","$videoStartTime");
        AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("planId").is(planId).and("videoStartTime").gte(startDate).lte(endDate).and("dataStatus").is(DataStatusEnum.VALID.getCode())),
                        Aggregation.group().addToSet(basicDBObject).as("hourSet")
                ),
                ModuleConstants.TABLE_VIDEO_FILE, BasicDBObject.class);
        BasicDBObject dbObject = results.getUniqueMappedResult();
        if (dbObject != null && dbObject.containsField("hourSet")) {
            BasicDBList dbList = (BasicDBList)dbObject.get("hourSet");
            dbList.forEach(o ->{
                list.add(Integer.valueOf(o.toString()));
            });
        }
        return list;
    }

    /**
     * @despriction：通过事件id获取事件对应的视频文件
     * @author  yeshiyuan
     * @created 2018/6/22 16:44
     * @param null
     * @return
     */
    @Override
    public List<VideoTsFileDto> getVideoFileListByEventUuid(String planId, String eventUuid, String fileType, Date eventOddurTime) {
        //为了加快查询速度，找到对应的片区，需要计划与事件发生时间同时查询定位到存储块(以时间发生时间的前后两分钟做查询)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(eventOddurTime);
        calendar.add(Calendar.MINUTE, -2);
        Date videoStartTime = calendar.getTime();
        calendar.setTime(eventOddurTime);
        calendar.add(Calendar.MINUTE, 2);
        Date videoEndTime = calendar.getTime();
        DBObject eventTimeObject = new BasicDBObject().append("$gte",videoStartTime).append("$lte",videoEndTime);
        DBObject queryObject = new BasicDBObject().append("planId", planId)
                .append("videoStartTime", eventTimeObject).append("eventUuid", eventUuid)
                .append("fileType", fileType).append("dataStatus", DataStatusEnum.VALID.getCode());
        DBObject filedObject = new BasicDBObject().append("fileId",1).append("_id",0)
                .append("videoStartTime",1).append("videoEndTime",1)
                .append("filePath",1).append("fileSize",1).append("rotation",1);
        Query query = new BasicQuery(queryObject, filedObject);
        return mongoTemplate.find(query.with(new Sort(Sort.Direction.ASC,"videoStartTime")), VideoTsFileDto.class, ModuleConstants.TABLE_VIDEO_FILE);
    }

    /**
     * @despriction：获取事件对应的文件信息
     * @author  yeshiyuan
     * @created 2018/8/15 11:12
     * @param null
     * @return
     */
    @Override
    public List<String> findFilePathByPlanIdAndEventUuid(String planId, String eventUuid) {
        DBObject queryObject = new BasicDBObject().append("planId", planId)
                .append("eventUuid", eventUuid).append("dataStatus",DataStatusEnum.VALID.getCode());
        DBObject fieldObject = new BasicDBObject().append("filePath",1).append("_id",0);
        DBCursor cursor = mongoTemplate.getCollection(ModuleConstants.TABLE_VIDEO_FILE).find(queryObject, fieldObject);
        List<String> filePath = new ArrayList<>();
        if (cursor!=null){
            while (cursor.hasNext()){
                DBObject dbObject = cursor.next();
                filePath.add(dbObject.get("filePath").toString());
            }
        }
        return filePath;
    }

    @Override
    public int deleteInvalidDataByPlanId(String planId) {
        Query query = Query.query(Criteria.where("planId").is(planId).and("dataStatus").is(DataStatusEnum.UN_VALID.getCode()));
        return mongoTemplate.remove(query,ModuleConstants.TABLE_VIDEO_FILE).getN();
    }
}
