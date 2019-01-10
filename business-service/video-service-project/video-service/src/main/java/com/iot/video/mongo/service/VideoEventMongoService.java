package com.iot.video.mongo.service;

import com.iot.video.dto.EventParamDto;
import com.iot.video.dto.VideoEventParamDto;
import com.iot.video.entity.VideoEvent;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.vo.MongoPageInfo;
import com.iot.video.vo.VideoEventVo;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：操作mongodb的videoEvent
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 18:08
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 18:08
 * 修改描述：
 */
public interface VideoEventMongoService {

    /**
     * @despriction：获取视频事件信息
     * @author  yeshiyuan
     * @created 2018/5/10 13:49
     * @param planId 计划id
     * @param eventUuid 事件uuid
     * @return
     */
    VideoEventEntity getEventByPlanIdAndEventUuid(String planId, String eventUuid);

    /**
     * @despriction：更新事件
     * @author  yeshiyuan
     * @created 2018/5/10 14:05
     * @param videoEvent
     * @return
     */
    int updateVideoEvent(VideoEvent videoEvent);

    /**
     * @despriction：插入录影事件
     * @author  yeshiyuan
     * @created 2018/4/8 10:34
     * @param videoEvent
     * @return
     */
    void insertVideoEvent(VideoEventEntity videoEvent);

    /**
     * @despriction：统计某计划的已录事件数
     * @author  yeshiyuan
     * @created 2018/6/13 14:38
     * @param tenantId 租户id
     * @param planId 计划id
     * @return
     */
    int countVideoEventByPlanId(String planId);

    /**
     * 描述：设置录影事件无效
     * @author mao2080@sina.com
     * @created 2018/5/23 20:03
     * @param tenantId 租户id
     * @param planId 计划id
     * @return void
     */
    int setVideoEventDataInvalid(String planId);

    /**
     * @despriction：删除失效视频事件数据
     * @author  yeshiyuan
     * @created 2018/8/7 11:28
     * @param planId 计划id
     * @return
     */
    int deleteInvalidVideoEventData(String planId);

    /**
     * @despriction：找到某一时间前的事件uuid
     * @author  yeshiyuan
     * @created 2018/6/12 19:14
     * @param null
     * @return
     */
    List<String> selectEventUuidsByTime(String planId, Date eventOddurTime);

    /**
     * @despriction：某一时间前的事件置为失效
     * @author  yeshiyuan
     * @created 2018/6/12 18:48
     * @param tenantId 租户id
     * @param planId 计划id
     * @return
     */
    int setVideoEventDataInvalid(String planId, Date eventTime);

    /**
     * @despriction：批量插入videoEvent
     * @author  yeshiyuan
     * @created 2018/8/6 19:15
     * @return
     */
    void bacthInsert(List<VideoEventEntity> list);

    /**
     * 描述：获取计划临界时间点
     * @author yeshiyuan
     * @created 2018/6/12 17:51
     * @param tenantId 租户id
     * @param planId 计划id
     * @return java.util.Date
     */
    Date getVideoLimitTime(String planId);

    /**
     * 描述：查询一段时间的事件列表
     * @author yeshiyuan
     * @created 2018/8/9 14:39
     * @param vespDto 查询参数
     * @return java.util.List<com.lds.iot.video.dto.VideoEventDto>
     */
    MongoPageInfo<VideoEventEntity> getVideoEventList(VideoEventParamDto vespDto);

    /**
     * 描述：查询一段时间的事件数量
     * @author yeshiyuan
     * @date 2018/8/9 14:30
     * @param vespDto 查询参数
     * @return
     */
    Long getVideoEventCount(VideoEventParamDto vespDto);

    /**
     *
     * 描述：删除并获取视频事件信息
     * @author yeshiyuan
     * @created 2018年8月9日 上午10:08:51
     * @param tenantId 租户id
     * @param userId 用户id
     * @param planId 计划id
     * @param eventId 事件id
     * @return
     */
    VideoEventEntity findByPlanIdAndEventId(Long tenantId, String planId, String eventId);

    /**
     * 描述：获取事件图片URL列表
     * @author yeshiyuan
     * @created 2018/3/23 15:29
     * @param epDto 查询参数VO
     * @return
     */
    MongoPageInfo<VideoEventEntity> getEventPhotoList(EventParamDto param);

    /**
     * @despriction：查询有事件录影的日期
     * @author  yeshiyuan
     * @created 2018/7/26 11:38
     * @return
     */
    List<String> selectHasEventVideoDay(String planId, Date startDate, Date endDate);

    /**
     * @despriction：删除事件信息
     * @author  yeshiyuan
     * @created 2018/8/15 11:06
     * @param null
     * @return
     */
    int deleteByPlanIdAndEventUuidAndDataStatus(String planId, String eventUuid, Integer dataStatus);
}
