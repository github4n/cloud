package com.iot.video.mongo.service;

import com.iot.file.vo.VideoFileGetUrlReq;
import com.iot.video.dto.VideoFileParamDto;
import com.iot.video.dto.VideoTsFileDto;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.vo.VideoEventJpg;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 19:13
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 19:13
 * 修改描述：
 */
public interface VideoFileMongoService {

    /**
      * @despriction：批量插入videofile
      * @author  yeshiyuan
      * @created 2018/8/6 19:15
      * @return
      */
    void bacthInsert(List<VideoFileEntity> list);

    /**
     * 描述：设置录影文件无效
     * @author mao2080@sina.com
     * @created 2018/5/23 20:03
     * @param tenantId 租户id
     * @param planId 计划id
     * @return void
     */
    int setVideoFileDataInvalid(String planId);


    /**
      * @despriction：查询失效视频文件数据并删除
      * @author  yeshiyuan
      * @created 2018/8/7 11:28
      * @param planId 计划id
      * @return
      */
    List<String> findInvalidVideoFilePath(String planId);

    /**
     * @despriction：某一时间前的文件置为失效
     * @author  yeshiyuan
     * @created 2018/6/12 18:48
     * @param planId 计划id
     * @return
     */
    int setVideoFileDataInvalid(String planId,Date videoStartTime);

    /**
      * @despriction：删除事件相关的文件信息
      * @author  yeshiyuan
      * @created 2018/8/9 15:24
      * @param null
      * @return
      */
    int deleteVideoEventFile(String planId, String eventUUID);

    /**
      * @despriction：查询事件对应的图片
      * @author  yeshiyuan
      * @created 2018/8/9 17:10
      * @return
      */
    Map<String, VideoEventJpg> getVideoEventJpgPicture(String planId, List<String> eventUuids);

    /**
     * 描述：获取一段时间视频文件列表
     * @author yeshiyuan
     * @created 2018/8/9 14:39
     * @param vfpDto 查询参数
     * @return java.util.List<com.lds.iot.video.dto.VideoFileDto>
     */
    List<VideoFileEntity> getVideoFileList(VideoFileParamDto vfpDto);

    /**
     * 描述：获取计划最后一个视频
     * @author yeshiyuan
     * @created 2018/8/23 15:48
     * @param lppDto 查询参数VO
     */
    VideoFileEntity getLastTsVideoFile(String planId, String deviceId);

    /**
     * 描述：统计某天里哪几个小时有录影
     * @author yeshiyuan
     * @date 2018/8/13 19:25
     * @param
     * @return
     */
    List<Integer> countSomeDayHasVideoHour(Date startDate, Date endDate, String planId);

    /**
     * @despriction：通过事件id获取事件对应的视频文件
     * @author  yeshiyuan
     * @created 2018/6/22 16:44
     * @param null
     * @return
     */
    List<VideoTsFileDto> getVideoFileListByEventUuid(String planId, String eventUuid, String fileType, Date eventOddurTime);

    /**
      * @despriction：获取事件对应的文件路径
      * @author  yeshiyuan
      * @created 2018/8/15 11:12
      * @param null
      * @return
      */
    List<String> findFilePathByPlanIdAndEventUuid(String planId, String eventUuid);

    /**
     * @despriction：删除计划对应的失效视频文件数据
     * @author  yeshiyuan
     * @created 2018/8/7 11:28
     * @param planId 计划id
     * @return
     */
    int deleteInvalidDataByPlanId(String planId);
}
