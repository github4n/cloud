package com.iot.video.api;

import com.iot.video.dto.LapseIdDto;
import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.vo.EventVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "视频服务-定时任务接口")
@FeignClient(value="video-service")
@RequestMapping("/api/videoTimer/service")
public interface VideoTimerApi {

	/**
	 * 
	 * 描述：获取过期的文件ID和事件ID
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:58
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "获取过期的文件ID和事件ID", notes = "获取过期的文件ID和事件ID")
	@RequestMapping(value = "/getLapseFileIdAndEventIdList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	LapseIdDto getLapseFileIdAndEventIdList();
	
	/**
	 * 
	 * 描述：删除全时录影失效文件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:51
	 * @since 
	 * @param lapseFileId
	 */
	@ApiOperation(value = "删除全时录影失效文件数据", notes = "删除全时录影失效文件数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lapseFileId", value = "过期文件ID", required = true, dataType = "String")})
	@RequestMapping(value = "/deleteLapseFile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void deleteLapseFile(@RequestBody List<String> lapseFileId);
	
	/**
	 * 
	 * 描述：删除全时录影失效事件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:42
	 * @since 
	 * @param lapseEventId
	 */
	@ApiOperation(value = "删除全时录影失效事件数据", notes = "删除全时录影失效事件数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lapseEventId", value = "过期事件ID", required = true, dataType = "String")})
	@RequestMapping(value = "/deleteLapseEvent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void deleteLapseEvent(@RequestBody List<Long> lapseEventId);
	
	/**
	 * 
	 * 描述：获取计划信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:34
	 * @since 
	 * @param planQueryState
	 * @return
	 */
	@ApiOperation(value = "获取计划信息", notes = "获取计划信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planQueryState", value = "查询计划状态", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/getSchedulePlanInfoList", method = RequestMethod.POST)
	List<SchedulePlanDto> getSchedulePlanInfoList(@RequestParam("planQueryState") String planQueryState);
	
	/**
	 * 
	 * 描述：批量更新计划提醒状态
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:24
	 * @since 
	 * @param planIdList
	 * @param comedueRemindState
	 */
	@ApiOperation(value = "批量更新计划提醒状态", notes = "批量更新计划提醒状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planIdList", value = "计划列表", required = true, dataType = "String"),
			@ApiImplicitParam(name = "comedueRemindState", value = "到期提醒状态", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/batchUpdatePlanRemindState", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void batchUpdatePlanRemindState(@RequestBody List<String> planIdList, @RequestParam("comedueRemindState")  String comedueRemindState);
	
	/**
	 * 
	 * 描述：批量更新计划执行状态
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:15
	 * @since 
	 * @param planIdList
	 * @param comedueRemindState
	 */
	@ApiOperation(value = "批量更新计划执行状态", notes = "批量更新计划执行状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planIdList", value = "计划列表", required = true, dataType = "String"),
			@ApiImplicitParam(name = "planExecStatus", value = "计划状态", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/batchUpdatePlanState", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void batchUpdatePlanState(@RequestBody List<String> planIdList, @RequestParam("planExecStatus")  String planExecStatus);

	/**
	 *
	 * 描述：获取失效计划的文件ID
	 * @author 李帅
	 * @created 2018年3月21日 下午5:27:07
	 * @since
	 * @param planIdList
	 * @return
	 *//*
	@ApiOperation(value = "获取失效计划的文件ID", notes = "获取失效计划的文件ID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划id", required = true, dataType = "String")})
	@RequestMapping(value = "/getOutDatePlanFildID", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<String> getOutDatePlanFildID(@RequestParam("planId") String planId);
	*/
	/**
	 * 
	 * 描述：删除计划文件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:26:57
	 * @since 
	 * @param planIdList
	 */
	/*@ApiOperation(value = "删除计划文件数据", notes = "删除计划文件数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planIdList", value = "计划列表", required = true, dataType = "String")})
	@RequestMapping(value = "/deleteLapseFileByPlanId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void deleteLapseFileByPlanId(@RequestBody List<String> planIdList);*/
	
	/**
	 * 
	 * 描述：删除计划事件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:26:50
	 * @since 
	 * @param planIdList
	 */
	@ApiOperation(value = "删除计划事件数据", notes = "删除计划事件数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planIdList", value = "计划列表", required = true, dataType = "String")})
	@RequestMapping(value = "/deleteLapseEventByPlanId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void deleteLapseEventByPlanId(@RequestBody List<String> planIdList);
	
	/**
	 * 
	 * 描述：获取计划ID和计划套餐量
	 * @author 李帅
	 * @created 2018年3月21日 下午5:26:42
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "获取计划ID和计划套餐量", notes = "获取计划ID和计划套餐量")
	@ApiImplicitParams({})
	@RequestMapping(value = "/getAllVenderPlanList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<VideoPlanBeyond> getAllVenderPlanList();
	
	/**
	 * 
	 * 描述：排序获取前num条事件信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:26:26
	 * @since 
	 * @param planId
	 * @param batchValue
	 * @return
	 */
	@ApiOperation(value = "排序获取前num条事件信息", notes = "排序获取前num条事件信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划ID", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "batchValue", value = "批次数据量", required = true, paramType = "query", dataType = "int")})
	@RequestMapping(value = "/getSortVideoEventInfo", method = RequestMethod.POST)
	List<EventVo> getSortVideoEventInfo(@RequestParam("planId") String planId, @RequestParam("batchValue") int batchValue);
	
	/**
	 * 
	 * 描述：获取事件关联的ts文件信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:26:18
	 * @since 
	 * @param planId
	 * @param eventOddurTime
	 * @return
	 */
	@ApiOperation(value = "获取事件关联的ts文件信息", notes = "获取事件关联的ts文件信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划ID", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "eventOddurTime", value = "取最后一个事件的发生时间", required = true, paramType = "query", dataType = "date")})
	@RequestMapping(value = "/getEventTsFileInfo", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	List<String> getEventTsFileInfo(@RequestBody EventVo eventVo);
	
	/**
	 * 
	 * 描述：获取解绑任务信息
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:02
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "获取解绑任务信息", notes = "获取解绑任务信息")
	@RequestMapping(value = "/getUntieTaskInfo", method = RequestMethod.POST)
	Map<String,String> getUntieTaskInfo();
	
	/**
	 * 
	 * 描述：获取解绑任务信息
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:02
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "删除解绑任务信息", notes = "删除解绑任务信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "redisKey", value = "RedisKey", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/deleteRedisObject", method = RequestMethod.POST)
	void deleteRedisObject(@RequestParam("redisKey") String redisKey);
	
	/**
	 * 
	 * 描述：删除失效数据
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:02
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "删除失效数据", notes = "删除失效数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "planId",value = "计划id",paramType = "query",dataType = "String"),
		@ApiImplicitParam(name = "taskKey",value = "任务id",paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/deleteLapseDatas", method = RequestMethod.POST)
	void deleteLapseDatas(@RequestParam("planId") String planId, @RequestParam("taskKey") String taskKey);

	/**
	  * @despriction：从redis中获取某一小时里有录影的全时计划
	  * @author  yeshiyuan
	  * @created 2018/6/12 15:49
	  * @param dateStr 时间（格式为2018-06-11:11）
	  * @return
	  */
	@ApiOperation(value = "从redis中获取某一小时里有录影的全时计划",notes = "从redis中获取某一小时里有录影的全时计划")
	@ApiImplicitParam(name = "dateStr",value = "时间（格式为2018-06-11:11）",dataType = "string",paramType = "query")
	@RequestMapping(value = "/getHadVideoOfPlanFromRedis",method = RequestMethod.GET)
	Set<String> getHadVideoOfPlanFromRedis(@RequestParam("dateStr")String dateStr);

	/**
	 * @despriction：删除redis中某一小时里有录影的全时计划
	 * @author  yeshiyuan
	 * @created 2018/6/12 15:49
	 * @param dateStr 时间（格式为2018-06-11:11）
	 * @return
	 */
	@ApiOperation(value = "删除redis中某一小时里有录影的全时计划",notes = "删除redis中某一小时里有录影的全时计划")
	@ApiImplicitParam(name = "dateStr",value = "时间（格式为2018-06-11:11）",dataType = "string",paramType = "query")
	@RequestMapping(value = "/deletePlanAllTimeOfRedis",method = RequestMethod.POST)
	void deletePlanAllTimeOfRedis(@RequestParam("dateStr")String dateStr);

	/**
	 * @despriction：从redis中获取事件录影溢出的计划
	 * @author  yeshiyuan
	 * @created 2018/6/12 15:49
	 * @return
	 */
	@ApiOperation(value = "从redis中获取事件录影溢出的计划",notes = "从redis中获取事件录影溢出的计划")
	@RequestMapping(value = "/getPlanEventOverFromRedis",method = RequestMethod.GET)
	Set<String> getPlanEventOverFromRedis();


	/**
	 * @despriction：批量处理过期计划
	 * @author  yeshiyuan
	 * @created 2018/6/14 15:04
	 * @param planIds 计划集合
	 * @param opType 操作类型(1:计划过期5天之内；2：计划过期5天之后)
	 * @return
	 */
	@ApiOperation(value = "批量处理过期计划",notes = "批量处理过期计划")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planIds",value = "计划id集合",paramType = "query",dataType = "List<String>"),
			@ApiImplicitParam(name = "opType",value = "操作类型(1:计划过期5天之内；2：计划过期5天之后)",paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/bacthDealPlanExpire",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	int bacthDealPlanExpire(@RequestBody List<String> planIds,@RequestParam("opType") String opType);
}