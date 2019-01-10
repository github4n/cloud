package com.iot.video.mongo.repository;

import com.iot.video.mongo.entity.VideoEventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 16:53
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 16:53
 * 修改描述：
 */
public interface VideoEventRepository extends MongoRepository<VideoEventEntity, String> {

    /**
      * @despriction：查找事件信息
      * @author  yeshiyuan
      * @created 2018/8/6 16:56
      * @param null
      * @return
      */
    VideoEventEntity findByPlanIdAndEventUuidAndDataStatus(String planId, String eventUuid, Integer dataStatus);

    /**
      * @despriction：删除事件信息
      * @author  yeshiyuan
      * @created 2018/8/15 11:06
      * @param null
      * @return
      */
    int deleteByPlanIdAndEventUuidAndDataStatus(String planId, String eventUuid, Integer dataStatus);
}
