package com.iot.video.api;

import com.github.pagehelper.PageInfo;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoPlan;
import com.iot.video.vo.redis.RedisVideoPlanInfoVo;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "视频服务-计划列表接口")
@FeignClient(value = "video-service")
@RequestMapping("/api/videoPlan/service")
public interface VideoPlanApi {

	/**
	 * 描述：查询一段时间的事件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vespDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoEventDto>
	 */
	/*@ApiOperation(value = "查询一段时间的事件列表", notes = "查询一段时间的事件列表")
	@RequestMapping(value="/getVideoEventList",method= RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	PageInfo<VideoEventDto> getVideoEventList(@RequestBody VideoEventParamDto vespDto);*/


	/**
	 * 描述：查询一段时间的事件数量
	 * @author 490485964@qq.com
	 * @date 2018/6/6 14:30
	 * @param vespDto 查询参数
	 * @return
	 */
	@ApiOperation(value = "查询一段时间的事件数量", notes = "查询一段时间的事件数量")
	@RequestMapping(value="/getVideoEventCount",method= RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	int getVideoEventCount(@RequestBody VideoEventParamDto vespDto);
	/**
	 * 描述：获取一段时间视频文件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vfpDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoFileDto>
	 */
	@ApiOperation(value = "获取一段时间视频文件列表", notes = "获取一段时间视频文件列表")
	@RequestMapping(value="/getVideoFileList",method= RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	List<VideoFileDto> getVideoFileList(@RequestBody VideoFileParamDto vfpDto);

	/**
	 * 描述：修改录影排序
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:38
	 * @param poDto 排序对象
	 * @return void
	 */
	@ApiOperation(value = "修改录影排序", notes = "修改录影排序")
	@RequestMapping(value = "/updatePlanOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void updatePlanOrder(@RequestBody PlanOrderParamDto poDto);

	/**
	 * 描述：查询事件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:29
	 * @param epDto 查询参数VO
	 * @return com.github.pagehelper.PageInfo<com.lds.iot.video.dto.EventDto>
	 */
	@ApiOperation(value = "获取事件图片URL列表", notes = "获取事件图片URL列表")
	@RequestMapping(value = "/getEventPhotoList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	PageInfo<EventDto> getEventPhotoList(@RequestBody EventParamDto epDto);

	/**
	 * 描述：获取计划最后一帧图片
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:48
	 * @param lppDto 查询参数VO
	 * @return java.util.List<com.lds.iot.video.dto.PlanLastPicDto>
	 */
	@ApiOperation(value = "获取计划最后一帧图片", notes = "获取计划最后一帧图片")
	@RequestMapping(value = "/getPlanLastPic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<PlanLastPicDto> getPlanLastPic(@RequestBody LastPicParamDto lppDto);

	/**
	 * 
	 * 描述：根据设备id查询计划类型
	 * @author 李帅
	 * @created 2018年4月27日 下午4:06:06
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param deviceId
	 * @return com.lds.iot.video.dto.PlanInfoDto
	 */
	@ApiOperation(value = "根据设备id查询计划类型", notes = "根据设备id查询计划类型")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户ID", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/getPlanType", method = RequestMethod.POST)
	PlanInfoDto getPlanType(@RequestParam("tenantId")Long tenantId, @RequestParam("userId") String userId, @RequestParam("deviceId") String deviceId);

	/**
	  * @despriction：获取视频计划详情
	  * @author  yeshiyuan
	  * @created 2018/5/17 15:33
	  * @param planId 计划id
	  * @param userUuid 用户uuid
	  * @return
	  */
	@ApiOperation(value = "获取视频计划详情",notes = "获取视频计划详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "userUuid", value = "用户uuid", required = true, paramType = "query", dataType = "String")
	})
	@RequestMapping(value = "/getVideoPlanDetail",method = RequestMethod.GET)
	VideoPlan getVideoPlanDetail(@RequestParam("planId")String planId,@RequestParam("userUuid") String userUuid);

	/**
	 * @despriction：从redis中获取计划相关信息
	 * @author  yeshiyuan
	 * @created 2018/6/12 11:25
	 * @param null
	 * @return
	 */
	@ApiOperation(value = "从redis中获取计划相关信息",notes = "从redis中获取计划相关信息")
	@ApiImplicitParam(name = "planId", value = "计划uuid",required = true, paramType = "query",dataType = "String")
	@RequestMapping(value = "/getVideoPlanInfoFromRedis",method = RequestMethod.GET)
	RedisVideoPlanInfoVo getVideoPlanInfoFromRedis(@RequestParam("planId")String planId);

	/**
	 * @despriction：修改redis中计划相关信息
	 * @author  yeshiyuan
	 * @created 2018/6/12 11:25
	 * @param null
	 * @return
	 */
	@ApiOperation(value = "修改redis中计划相关信息",notes = "修改redis中计划相关信息")
	@RequestMapping(value = "/updateVideoPlanInfoOfRedis",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	void updateVideoPlanInfoOfRedis(@RequestBody RedisVideoPlanInfoVo redisVideoPlanInfoVo);

	/**
	  * @despriction：从redis中通过计划id获取设备id
	  * @author  yeshiyuan
	  * @created 2018/6/13 16:53
	  * @param null
	  * @return
	  */
	@ApiOperation(value = "从redis中通过计划id获取设备id",notes = "从redis中通过计划id获取设备id")
	@ApiImplicitParam(name = "planId", value = "计划uuid",required = true, paramType = "query",dataType = "String")
	@RequestMapping(value = "/getDeviceIdByPlanIdFromRedis",method = RequestMethod.GET)
	String getDeviceIdByPlanIdFromRedis(@RequestParam("planId")String planId);


	/**
	 * @despriction：查找计划绑定的用户id
	 * @author  yeshiyuan
	 * @created 2018/8/13 17:05
	 * @param null
	 * @return
	 */
	@ApiOperation(value = "查找计划绑定的用户id",notes = "查找计划绑定的用户id")
	@ApiImplicitParam(name = "planId", value = "计划uuid",required = true, paramType = "query",dataType = "String")
	@RequestMapping(value = "/getUserIdByPlanId",method = RequestMethod.GET)
	String getUserIdByPlanId(@RequestParam("planId") String planId);

	/**
	  * @despriction：ipc上报前校验
	  * @author  yeshiyuan
	  * @created 2018/9/3 17:39
	  * @param null
	  * @return
	  */
	@ApiOperation(value = "ipc上报前校验",notes = "ipc上报前校验")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划uuid",required = true, paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "deviceId", value = "设备uuid",required = true, paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/checkBeforeUpload",method = RequestMethod.POST)
	CheckBeforeUploadResult checkBeforeUpload(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId);
}