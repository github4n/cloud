package com.iot.video.service;

import com.github.pagehelper.PageInfo;
import com.iot.video.dto.PlanInfoDto;
import com.iot.video.vo.*;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
import com.iot.vo.BasePageVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：聚合层接口
 * 创建人：mao2080@sina.com
 * 创建时间：2018/3/20 17:28
 * 修改人：mao2080@sina.com
 * 修改时间：2018/3/20 17:28
 * 修改描述：
 */
public interface VideoBusinessService {

    /**
     * 描述：更新录影计划状态
     *
     * @param planId         计划id
     * @param planExecStatus 计划执行状态
     * @author mao2080@sina.com
     * @created 2018/3/23 9:34
     */
    void updatePlanExecStatus(String planId, Integer planExecStatus);

    /**
     * 描述：修改录影计划任务
     *
     * @param taskPlanList 录影任务参数
     * @return void
     * @author wujianlong
     * @created 2018/3/26 15:51
     */
    void updatePlanTask(List<TaskPlanVo> taskPlanList);

    /**
     * 描述：查询套餐列表
     *
     * @param videoPackageParamVo 套餐查询参数VO
     * @return com.github.pagehelper.PageInfo<com.lds.iot.vo.video.VideoPackageVo>
     * @author wujianlong
     * @created 2018/3/26 15:51
     */
    PageInfo<VideoPackageVo> getVideoPackageList(VideoPackageParamVo videoPackageParamVo);

    /**
     * 描述：查询录影任务
     *
     * @param planId 计划id
     * @return java.util.List<com.lds.iot.vo.video.VideoTaskVo>
     * @author wujianlong
     * @created 2018/3/26 15:52
     */
    List<VideoTaskVo> getVideoTaskList(String planId);

    /**
     * 描述：获取计划列表
     *
     * @param planParamVo 查询计划列表参数VO
     * @return com.github.pagehelper.PageInfo<com.lds.iot.vo.video.VideoPlanVo>
     * @author wujianlong
     * @created 2018/3/26 15:52
     */
    PageInfo<VideoPlanVo> getPlanList(PlanParamVo planParamVo);

    /**  
     * 描述：通过用户查询设备，再通过设备查找录影计划,用于云端录影个人模块计划管理及计划列表展示
     * @author 490485964@qq.com  
     * @date 2018/5/11 17:35
     * @param   
     * @return   
     */
    VideoPlanInfoVo getPlanInfoByDevId(PlanParamVo planParamVo);

    /**
     * 描述：查询一段时间的事件列表
     *
     * @param vespVO 查询参数
     * @return java.util.List<com.lds.iot.vo.video.VideoEventVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 14:06
     */
  /*  PageInfo<VideoEventVO> getVideoEventList(VideoEventParamVO vespVO);*/

    /**
     * 描述：查询一段时间的事件数量
     * @author 490485964@qq.com
     * @date 2018/6/6 14:30
     * @param vespVO 查询参数
     * @return
     */
    int getVideoEventCount(VideoEventParamVO vespVO);

    /**
     * 描述：获取一段时间视频文件列表
     *
     * @param vfpVO 查询参数
     * @return java.util.List<com.lds.iot.vo.video.VideoFileVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 14:06
     */
    List<VideoFileVO> getVideoFileList(VideoFileParamVO vfpVO);

    /**
     * 描述：修改录影排序
     *
     * @param poVO 排序对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/3/23 15:06
     */
    void updatePlanOrder(PlanOrderParamVO poVO);

    /**
     * 描述：获取事件图片URL列表
     *
     * @param epVO 查询条件VO
     * @return com.github.pagehelper.PageInfo<com.lds.iot.vo.video.EventVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 15:27
     */
    PageInfo<EventVO> getEventPhotoList(EventParamVO epVO);

    /**
     * 描述：获取计划最后一帧图片
     *
     * @param lppVO 查询条件VO
     * @return java.util.List<com.lds.iot.vo.video.LastPicVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 15:44
     */
    List<LastPicVO> getPlanLastPic(LastPicParamVO lppVO);

    /**
     * 描述：更新计划名称
     *
     * @param planNameVo 计划名称
     * @return void
     * @author mao2080@sina.com
     * @created 2018/3/26 16:53
     */
    void updatePlanName(PlanNameVo planNameVo);

    /**
     * O
     * 描述：分页查询购买历史
     *
     * @param bhrlpVO 查询参数VO
     * @return com.github.pagehelper.PageInfo<com.lds.iot.vo.video.BuyHisRecordListVO>
     * @author mao2080@sina.com
     * @created 2018/3/26 17:12
     */
    PageInfo<BuyHisRecordListVO> getBuyHisRecordList(BuyHisRecordListParamVO bhrlpVO);

    /**
     * 描述：分页查询购买记录
     *
     * @param bpVO 基础分页VO
     * @return com.github.pagehelper.PageInfo<com.lds.iot.vo.video.BuyRecordListVO>
     * @author mao2080@sina.com
     * @created 2018/3/26 17:20
     */
    PageInfo<BuyRecordListVO> getBuyRecordList(BasePageVO bpVO);

    /**
     * 描述：购买视频计划
     *
     * @param webPlanVo
     * @param localServerUrl 本服务器的ip、端口
     * @return
     * @author wujianlong
     * @created 2018年3月28日 上午10:54:44
     * @since
     */
    String buyVideoPlan(WebPlanVo webPlanVo, String localServerUrl);

    /**
     * 描述：购买视频计划回调
     *
     * @param req
     * @return
     * @author wujianlong
     * @created 2018年3月28日 下午5:22:52
     * @since
     */
    String buyVideoPlanCallBack(HttpServletRequest req);


    /**
     * 描述：根据设备id查询计划类型
     *
     * @param deviceId
     * @return com.lds.iot.video.dto.PlanInfoDto
     * @author 李帅
     * @created 2018年4月16日 下午2:15:40
     * @since
     */
    PlanInfoDto getPlanType(String deviceId);

    /**
     * 描述：计划绑定设备
     *
     * @param planId
     * @param deviceId
     * @author 李帅
     * @created 2018年4月27日 下午1:42:26
     * @since
     */
    void planBandingDevice(String planId, String deviceId);

    /**
     * 描述：删除录影事件
     *
     * @param planId
     * @param eventId
     * @author 李帅
     * @created 2018年4月27日 下午1:42:26
     * @since
     */
    void deleteVideoEvent(String planId, String eventId);



    /**
     * @despriction：通过事件id获取事件对应的视频文件
     * @author  yeshiyuan
     * @created 2018/6/25 9:28
     * @param null
     * @return
     */
    List<VideoFileVO> getVideoTsFileListByEventUuid(GetEventVideoFileReq req);


    /**
     * @despriction：统计IPC录影日期
     * @author  yeshiyuan
     * @created 2018/7/26 10:56
     * @return
     */
    List<String> countVideoDate(CountVideoDateReq req);
}
