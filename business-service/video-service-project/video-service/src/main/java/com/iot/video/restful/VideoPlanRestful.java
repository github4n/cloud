package com.iot.video.restful;

import com.github.pagehelper.PageInfo;
import com.iot.video.api.VideoPlanApi;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoPlan;
import com.iot.video.manager.VideoPlanManager;
import com.iot.video.vo.redis.RedisVideoPlanInfoVo;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称： 
 * 功能描述：
 * 创建人： 
 * 创建时间：2018/3/19 10:47 
 * 修改人： 修改时间：2018/3/19 10:47
 * 修改描述：
 */
@RestController
public class VideoPlanRestful implements VideoPlanApi {

	@Autowired
	private VideoPlanManager videoPlanManager;

	/**
	 * 描述：查询一段时间的事件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vespDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoEventDto>
	 */
	/*public PageInfo<VideoEventDto> getVideoEventList(@RequestBody VideoEventParamDto vespDto){
		return this.videoPlanManager.getVideoEventList(vespDto);
	}*/

	/**
	 * 描述：查询一段时间的事件数量
	 * @author 490485964@qq.com
	 * @date 2018/6/6 14:30
	 * @param vespDto 查询参数
	 * @return
	 */
	@Override
	public int getVideoEventCount(@RequestBody VideoEventParamDto vespDto) {
		return this.videoPlanManager.getVideoEventCount(vespDto);
	}

	/**
	 * 描述：获取一段时间视频文件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vfpDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoFileDto>
	 */
	public List<VideoFileDto> getVideoFileList(@RequestBody VideoFileParamDto vfpDto){
		return this.videoPlanManager.getVideoFileList(vfpDto);
	}

	/**
	 * 描述：修改录影排序
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:38
	 * @param poDto 排序对象
	 * @return void
	 */
	public void updatePlanOrder(@RequestBody PlanOrderParamDto poDto){
		this.videoPlanManager.updatePlanOrder(poDto);
	}

	/**
	 * 描述：获取事件图片URL列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:29
	 * @param epDto 查询参数VO
	 * @return com.github.pagehelper.PageInfo<com.lds.iot.video.dto.EventDto>
	 */
	public PageInfo<EventDto> getEventPhotoList(@RequestBody EventParamDto epDto){
		return this.videoPlanManager.getEventPhotoList(epDto);
	}

	/**
	 * 描述：获取计划最后一帧图片
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:48
	 * @param lppDto 查询参数VO
	 * @return java.util.List<com.lds.iot.video.dto.PlanLastPicDto>
	 */
	public List<PlanLastPicDto> getPlanLastPic(@RequestBody LastPicParamDto lppDto){
		return this.videoPlanManager.getPlanLastPic(lppDto);
	}

	/**
	 * 
	 * 描述：根据设备id查询计划类型
	 * @author 李帅
	 * @created 2018年4月27日 下午4:04:58
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param deviceId
	 * @return com.lds.iot.video.dto.PlanInfoDto
	 */
	public PlanInfoDto getPlanType(Long tenantId, String userId, String deviceId){
		return this.videoPlanManager.getPlanType(tenantId, userId, deviceId);
	}


	/**
	 * @despriction：获取视频计划详情
	 * @author  yeshiyuan
	 * @created 2018/5/17 15:33
	 * @param planId 计划id
	 * @param userUuid 用户uuid
	 * @return
	 */
	@Override
	public VideoPlan getVideoPlanDetail(String planId, String userUuid) {
		return videoPlanManager.getVideoPlan(planId,userUuid);
	}

	/**
	 * @despriction：从redis中获取计划相关信息
	 * @author  yeshiyuan
	 * @created 2018/6/12 11:25
	 * @param null
	 * @return
	 */
	@Override
	public RedisVideoPlanInfoVo getVideoPlanInfoFromRedis(@RequestParam("planId") String planId) {
		return videoPlanManager.getVideoPlanInfoFromRedis(planId);
	}

	/**
	 * @despriction：修改redis中计划相关信息
	 * @author  yeshiyuan
	 * @created 2018/6/12 11:25
	 * @param null
	 * @return
	 */
	@Override
	public void updateVideoPlanInfoOfRedis(@RequestBody  RedisVideoPlanInfoVo redisVideoPlanInfoVo) {
		videoPlanManager.updateVideoPlanInfoOfRedis(redisVideoPlanInfoVo);
	}

	/**
	 * @despriction：从redis中通过计划id获取设备id
	 * @author  yeshiyuan
	 * @created 2018/6/13 16:53
	 * @param null
	 * @return
	 */
	@Override
	public String getDeviceIdByPlanIdFromRedis(@RequestParam("planId")String planId) {
		return videoPlanManager.getDeviceIdByPlanIdFromRedis(planId);
	}

	/**
	 * @despriction：查找计划绑定的用户id
	 * @author  yeshiyuan
	 * @created 2018/8/13 17:05
	 * @param null
	 * @return
	 */
	@Override
	public String getUserIdByPlanId(@RequestParam("planId") String planId) {
		return videoPlanManager.getUserIdByPlanId(planId);
	}

	/**
	 * @despriction：ipc上报前校验
	 * @author  yeshiyuan
	 * @created 2018/9/3 17:39
	 * @param null
	 * @return
	 */
	@Override
	public CheckBeforeUploadResult checkBeforeUpload(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId){
		return videoPlanManager.checkBeforeUpload(planId, deviceId);
	}
}