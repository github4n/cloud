package com.iot.video.api;

import com.github.pagehelper.PageInfo;
import com.iot.video.api.fallback.VideoManageApiFallbackFactory;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoEvent;
import com.iot.video.entity.VideoFile;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
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

@Api(tags = "视频服务-计划管理接口")
@FeignClient(value = "video-service", fallbackFactory = VideoManageApiFallbackFactory.class)
@RequestMapping("/api/videoManage/service")
public interface VideoManageApi {

	/**
	 * 
	 * 描述：查询计划列表
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:07:07
	 * @since 
	 * @param planSearchParam
	 * @return
	 */
	@ApiOperation(value = "获取计划列表" ,  notes="获取计划列表",response=PlanSearchParam.class)
	/*@ApiImplicitParams({@ApiImplicitParam(name = "planSearchParam", value = "计划参数", required = true, dataType = "PlanSearchParam")})*/
	@RequestMapping(value="/getPlanList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    PageInfo<VideoPlanDto> getPlanList(@RequestBody PlanSearchParam planSearchParam);

	@ApiOperation(value = "通过设备查找录影计划" ,  notes="通过设备查找录影计划")
	@ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/getPlanInfoByDevId",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	VideoPlanInfoDto getPlanInfoByDevId(@RequestBody PlanSearchParam planSearchParam);
	/**
	 * 
	 * 描述：修改录影状态
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:08:14
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planExecStatus
	 */
	@ApiOperation(value = "更新录影计划状态" ,  notes="更新录影计划状态")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
		@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
		@ApiImplicitParam(name = "planExecStatus", value = "计划执行状态", required = true, paramType = "query", dataType = "Integer")})
	@RequestMapping(value="/updatePlanExecStatus",method=RequestMethod.POST)
    void updatePlanExecStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") String userId, @RequestParam("planId") String planId,
                              @RequestParam("planExecStatus") Integer planExecStatus);
	
	/**
	 * 
	 * 描述：修改计划名字
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:08:28
	 * @since
	 * @param planNameParam
	 */
	@ApiOperation(value = "更新计划名称" ,  notes="更新计划名称")
	@RequestMapping(value="/updatePlanName",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    void updatePlanName(@RequestBody PlanNameParam planNameParam);
	
	/**
	 * 
	 * 描述：查询录影任务
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:08:47
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @return
	 */
	@ApiOperation(value = "查询录影任务" ,  notes="查询录影任务")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
		@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/getVideoTaskList",method=RequestMethod.POST)
    List<VideoTaskDto> getVideoTaskList(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") String userId, @RequestParam("planId") String planId);
	
	/**
	 * 
	 * 描述：修改录影计划任务
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:09:01
	 * @since 
	 * @param taskPlanList
	 */
	@ApiOperation(value = "修改录影计划" ,  notes="修改录影计划")
	/*@ApiImplicitParams({@ApiImplicitParam(name = "taskPlanList", value = "录影计划列表", required = true, dataType = "TaskPlanParam")})*/
	@RequestMapping(value="/updatePlan",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    void updatePlanTask(@RequestBody List<TaskPlanParam> taskPlanList);
	
	/**
	 * 
	 * 描述：查询套餐列表
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:09:28
	 * @since 
	 * @param videoPackageSearchParam
	 * @return
	 */
	@ApiOperation(value = "查询套餐列表" ,  notes="查询套餐列表")
	/*@ApiImplicitParams({@ApiImplicitParam(name = "videoPackageSearchParam", value = "套餐查询参数", required = true, dataType = "VideoPackageSearchParam")
		})*/
	@RequestMapping(value="/getVideoPackageList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    PageInfo<VideoPackageDto> getVideoPackageList(@RequestBody VideoPackageSearchParam videoPackageSearchParam);
	
	/**
	 * 
	 * 描述：创建计划
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:09:41
	 * @since 
	 * @param plan
	 */
	@ApiOperation(value = "创建计划" ,  notes="创建计划",response=PlanParam.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "plan", value = "计划参数", required = true, dataType = "PlanParam")})
	@RequestMapping(value="/createPlan",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    String createPlan(@RequestBody PlanParam plan);
	
	/**
	 * 
	 * 描述：查询购买历史
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:09:56
	 * @since 
	 * @param hisRecordSearchParam
	 * @return
	 */
	@ApiOperation(value = "查询购买历史" ,  notes="查询购买历史")
	@ApiImplicitParams({@ApiImplicitParam(name = "hisRecordSearchParam", value = "历史购买记录查询条件", required = true, dataType = "HisRecordSearchParam")})
	@RequestMapping(value="/getBuyHisRecordList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    PageInfo<PayRecordDto> getBuyHisRecordList(@RequestBody HisRecordSearchParam hisRecordSearchParam);
	
	/**
	 * 
	 * 描述：查询购买记录
	 * @author wujianlong
	 * @created 2018年3月23日 上午11:10:12
	 * @since 
	 * @param searchParam
	 * @return
	 */
	@ApiOperation(value = "查询购买记录" ,  notes="查询购买记录")
	//@ApiImplicitParams({@ApiImplicitParam(name = "recordSearchParam", value = "记录查询参数", required = true, dataType = "RecordSearchParam")})
	@RequestMapping(value="/getBuyRecordList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    PageInfo<PayRecordDto> getBuyRecordList(@RequestBody RecordSearchParam searchParam);
	
	/**
	 *
	 * 描述：查询所有计划购买记录
	 * @author 李帅
	 * @created 2018年5月21日 下午5:46:46
	 * @since
	 * @param searchParam
	 * @return
	 */
	@ApiOperation(value = "查询所有计划购买记录" ,  notes="查询所有计划购买记录")
	@RequestMapping(value="/getAllBuyRecordList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    List<ALLRecordDto> getAllBuyRecordList(@RequestBody AllRecordSearchParam searchParam);

	/**
	 * 
	 * 描述：存储webPlan参数到redis中
	 * @author wujianlong
	 * @created 2018年3月28日 下午3:41:39
	 * @since 
	 * @param webPlan
	 */
	@RequestMapping(value="/saveWebPlan",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    void saveWebPlan(@RequestBody WebPlanDto webPlan);
	
	/**
	 * 
	 * 描述：从redis中获取webPlan参数
	 * @author wujianlong
	 * @created 2018年3月28日 下午3:43:01
	 * @since 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/getWebPlan",method=RequestMethod.POST)
    WebPlanDto getWebPlan(@RequestParam("orderId") String orderId);
	
	/**
	 * 
	 * 描述：获取套餐价格
	 * @author wujianlong
	 * @created 2018年3月29日 下午5:46:04
	 * @since 
	 * @param packageId
	 * @return
	 */
	@RequestMapping(value="/getPackagePriceById",method=RequestMethod.POST)
    AppPayDto getPackagePriceById(@RequestParam("packageId") String packageId);

	/**
	 * 
	 * 描述：保存redis，存储payId,planId
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:05:22
	 * @since 
	 * @param payId
	 * @param planId
	 */
	@RequestMapping(value="/saveAppPay",method=RequestMethod.POST)
    void saveAppPay(@RequestParam("payId") String payId, @RequestParam("planId") String planId);

	/**
	 * 
	 * 描述：获取planId
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:05:43
	 * @since 
	 * @param payId
	 * @return
	 */
	@RequestMapping(value="/getAppPay",method=RequestMethod.POST)
    String getAppPay(@RequestParam("payId") String payId);

	/**
	 *
	 * 描述：续费计划
	 * @author ouyangjie
	 * @created 2018年3月23日 上午11:09:41
	 * @since
	 * @param
	 */
	@ApiOperation(value = "续费计划" ,  notes="续费计划")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "counts", value = "续费时长", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "query", dataType = "String")
	})
	@RequestMapping(value="/renewPlan",method=RequestMethod.POST)
    int renewPlan(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") String userId, @RequestParam("planId") String planId,
                  @RequestParam("counts") int counts, @RequestParam("orderId") String orderId);
	/**
	 * 描述：计划绑定设备
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @param deviceId 设备id
	 * @return void
	 */
	@ApiOperation(value = "计划绑定设备" ,  notes="计划绑定设备")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
		@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
	    @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/planBandingDevice",method=RequestMethod.POST)
	void planBandingDevice(@RequestParam("tenantId") Long tenantId,@RequestParam("userId") String userId, @RequestParam("planId") String planId,
			@RequestParam("deviceId") String deviceId);

	/**
	 * 描述：删除录影事件
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @param eventId 事件id
	 * @return void
	 */
	@ApiOperation(value = "删除录影事件" ,  notes="删除录影事件")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
		@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
	    @ApiImplicitParam(name = "eventId", value = "事件id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/deleteVideoEvent",method=RequestMethod.POST)
	void deleteVideoEvent(@RequestParam("tenantId") Long tenantId,@RequestParam("userId") String userId, @RequestParam("planId") String planId,
			@RequestParam("eventId") String eventId);
	
	/**
	  * @despriction：上报事件
	  * @author  yeshiyuan
	  * @created 2018/4/8 10:12
	  * @param
	  * @return int
	  */
	@ApiOperation(value = "上报事件" , notes = "上报事件")
	@RequestMapping(value = "/insertVideoEvent",method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	void insertVideoEvent(@RequestBody VideoEvent videoEvent);

	/**
	 * 描述：根据计划Id获取设备Id
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @return String
	 */
	@ApiOperation(value = "根据计划Id获取设备ID" ,  notes="根据计划Id获取设备ID")
	@ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/getDeviceId",method=RequestMethod.POST)
	String getDeviceId(@RequestParam("planId") String planId);

	/**
	 * 描述：根据计划Id获取设备Id
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param deviceId 设备id
	 * @return String
	 */
	@ApiOperation(value = "根据设备Id获取计划ID" ,  notes="根据设备Id获取计划ID")
	@ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/getPlanId",method=RequestMethod.POST)
	String getPlanId(@RequestParam("deviceId") String deviceId);

	/**
	 * 描述：获取需要同步给设备的任务信息
	 * @author 490485964@qq.com
	 * @date 2018/4/8 14:04
	 * @param planId 计划id
	 * @return List<VideoPlanTaskDto>
	 */
	@ApiOperation(value = "获取需要同步给设备的任务信息" ,  notes="获取需要同步给设备的任务信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/getSyncTaskInfo",method=RequestMethod.POST)
	List<VideoPlanTaskDto> getSyncTaskInfo(@RequestParam("planId") String planId);

	/**
	 * 描述：添加文件信息
	 * @author 490485964@qq.com
	 * @date 2018/4/8 17:24
	 * @param videoFile  文件信息
	 * @return
	 */
	@ApiOperation(value = "添加文件信息" ,  notes="添加文件信息",response=VideoFile.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "videoFile", value = "文件信息", required = true, dataType = "VideoFile")})
	@RequestMapping(value="/createVideoFile",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	void createVideoFile(@RequestBody VideoFile videoFile);

	@ApiOperation(value = "判断设备是否已经绑定计划" ,  notes="判断设备是否已经绑定计划")
	@ApiImplicitParams({@ApiImplicitParam(name = "deviceIdList", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/countDeviceBandingPlan",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	List<String> judgeDeviceBandPlan(@RequestBody List<String> deviceIdList);

	/**
	  * @despriction：删除购买计划订单（redis数据）
	  * @author  yeshiyuan
	  * @created 2018/4/28 11:19
	  * @return
	  */
	@ApiOperation(value = "删除购买计划订单（redis数据）",notes = "删除购买计划订单（redis数据）")
	@ApiImplicitParam(name = "orderId",value = "订单id",required = true, paramType = "query", dataType = "String")
	@RequestMapping(value = "/deletePlanOrder",method = RequestMethod.POST)
	void deletePlanOrder(@RequestParam("orderId") String orderId);

	@ApiOperation(value = "判断计划是否存在" ,  notes="判断计划是否存在")
	@ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/judgePlanExist",method=RequestMethod.POST)
	boolean judgePlanExist(@RequestParam("planId") String planId);


	@ApiOperation(value = "校验设备是否已绑定计划",notes = "校验设备是否已绑定计划")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deviceId",value = "设备id",required = true,paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/checkDeviceHasBindPlan",method = RequestMethod.GET)
	boolean checkDeviceHasBindPlan(@RequestParam("deviceId") String deviceId);


	@ApiOperation(value = "根据计划ID获取最新订单" , notes = "根据计划ID获取最新订单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "planStatus", value = "订单状态",paramType = "query",dataType = "Integer")
	})
	@RequestMapping(value="/getLatestOrderByPlanIdAndStatus",method=RequestMethod.POST)
	VideoPayRecordDto getLatestOrderByPlanIdAndStatus(@RequestParam("tenantId") Long tenantId,
													  @RequestParam("planId") String planId, @RequestParam("planStatus") Integer planStatus);

	@ApiOperation(value = "查询某一天的的每个小时是否有视频" , notes = "查询某一天的的每个小时是否有视频")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchDate", value = "查询日期", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")
	})
	@RequestMapping(value="/getVideoStartTimeHourByDate",method=RequestMethod.POST)
	List<Integer> getVideoStartTimeHourByDate(@RequestParam("searchDate") String searchDate,@RequestParam("planId") String planId);

	@ApiOperation(value = "查询视频某一购买记录",notes = "查询视频某一购买记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "orderId", value = "订单id",paramType = "query",dataType = "String")
	})
	@RequestMapping(value="/getVideoPayRecord",method=RequestMethod.GET)
	VideoPayRecord getVideoPayRecord(@RequestParam("tenantId") Long tenantId, @RequestParam("planId") String planId, @RequestParam("orderId") String orderId);

	/**
	 * @despriction：退款计划中的某个订单
	 * @author  yeshiyuan
	 * @created 2018/5/22 13:47
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @return
	 */
	@ApiOperation(value = "退款计划中的某个订单",notes = "退款计划中的某个订单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "orderId", value = "订单id",paramType = "query",dataType = "String")
	})
	@RequestMapping(value="/refundOneOrderOfPlan",method=RequestMethod.POST)
    void refundOneOrderOfPlan(@RequestParam("planId") String planId, @RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId);

	/**
	 * @despriction：查询计划的其他购买记录
	 * @author  yeshiyuanPlan
	 * @created 2018/5/22 13:47
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @return
	 */
	@ApiOperation(value = "查询计划的其他购买记录",notes = "查询计划的其他购买记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "orderId", value = "订单id",paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/queryPlanOtherPayRecord", method = RequestMethod.GET)
	List<VideoPlanOrderDto> queryPlanOtherPayRecord(@RequestParam("orderId") String orderId,@RequestParam("planId")String planId);

	/**
	 * 描述：计划解绑设备
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	@ApiOperation(value = "计划解绑设备",notes = "计划解绑设备")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tenantId", value = "租户id",required = true, paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "deviceId", value = "设备id",required = true, paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/planUnBandingDevice", method = RequestMethod.GET)
	void planUnBandingDevice(@RequestParam("tenantId")Long tenantId, @RequestParam("deviceId")String deviceId);

	/**
	  * @despriction：处理某一时间前的全时录影溢出数据（video_file,video_event）置为无效
	  * @author  yeshiyuan
	  * @created 2018/6/12 17:31
	  * @param planId 计划id
	  * @return
	  */
	@ApiOperation(value = "处理某一时间前的全时录影溢出数据（video_file,video_event）置为无效" , notes = "处理某一时间前的全时录影溢出数据（video_file,video_event）置为无效")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "videoStartTime", value = "录影时间",required = true, paramType = "query",dataType = "String")
	})
	@RequestMapping(value = "/dealPlanAllTimeOver",method = RequestMethod.POST)
	void dealPlanAllTimeOver(@RequestParam("planId") String planId,@RequestParam("videoStartTime")String videoStartTime);

	/**
	  * @despriction：清除计划事件录影溢出数据（video_file,video_event），置为失效
	  * @author  yeshiyuan
	  * @created 2018/6/13 17:25
	  * @param planId 计划id
	  * @return
	  */
	@ApiOperation(value = "清除计划事件录影溢出数据（video_file,video_event），置为失效" , notes = "清除计划事件录影溢出数据（video_file,video_event），置为失效")
	@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String")
	@RequestMapping(value = "/dealPlanEventOver",method = RequestMethod.POST)
	void dealPlanEventOver(@RequestParam("planId") String planId);

	/**
	  * @despriction：处理过期计划的相关数据（video_file,video_event，redis缓存信息、解绑设备）
	  * @author  yeshiyuan
	  * @created 2018/6/15 13:41
	 * @param planId
	  * @return
	  */
	@ApiOperation(value = "处理过期计划的相关数据（video_file,video_event，redis缓存信息、解绑设备）" , notes = "处理过期计划的相关数据（video_file,video_event，redis缓存信息、解绑设备）")
	@ApiImplicitParam(name = "planId", value = "计划id",required = true,paramType = "query",dataType = "String")
	@RequestMapping(value = "/dealPlanExpireRelateData",method = RequestMethod.POST)
	void dealPlanExpireRelateData(@RequestParam("planId") String planId);

	/**
	 * @despriction：通过事件id获取事件对应的视频文件
	 * @author  yeshiyuan
	 * @created 2018/6/22 16:44
	 * @param planId
	 * @param eventUuid
	 * @param fileType
	 * @return
	 */
	@ApiOperation(value = "通过事件id获取事件对应的视频文件",notes = "通过事件id获取事件对应的视频文件")
	@RequestMapping(value = "/getVideoFileListByEventUuid",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<VideoTsFileDto> getVideoFileListByEventUuid(@RequestBody GetEventVideoFileReq getEventVideoFileReq);

	/**
	  * @despriction：统计IPC录影日期
	  * @author  yeshiyuan
	  * @created 2018/7/26 10:56
	  * @return
	  */
	@ApiOperation(value = "统计IPC录影日期", notes = "统计IPC录影日期")
	@RequestMapping(value = "/countVideoDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<String> countVideoDate(@RequestBody CountVideoDateReq req);
}