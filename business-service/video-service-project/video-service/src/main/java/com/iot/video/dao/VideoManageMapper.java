package com.iot.video.dao;

import com.github.pagehelper.Page;
import com.iot.video.dto.*;
import com.iot.video.entity.*;
import com.iot.video.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface VideoManageMapper {

	/**
	 * 
	 * 描述：更新执行计划状态
	 * @author wujianlong
	 * @created 2018年3月22日 上午10:33:18
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param
	 */
	@Update("update video_plan set plan_exec_status = #{planExecStatus,jdbcType=INTEGER} "
			+ "where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	void updatePlanExecStatus(@Param("tenantId") Long tenantId,@Param("userId") String userId, @Param("planId") String planId,
			@Param("planExecStatus") Integer planExecStatus);

	/**
	 * 
	 * 描述：更新计划周期
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:50:12
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planCycle
	 * @param planStatus
	 */
	@Update("update video_plan set plan_cycle = #{planCycle,jdbcType=VARCHAR},"
			+ "plan_status = #{planStatus,jdbcType=VARCHAR} "
			+ "where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	void updatePlanCycle(@Param("tenantId") Long tenantId,@Param("userId") String userId,@Param("planId") String planId,
			@Param("planCycle") String planCycle,@Param("planStatus") int planStatus);

	/**
	 * 
	 * 描述：删除任务
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:50:36
	 * @since 
	 * @param tenantId 租户id
	 * @param userId 用户id
	 * @param planId 计划id
	 */
	@Delete("delete from video_task where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	void deleteTaskByPlanId(@Param("tenantId") Long tenantId,@Param("userId") String userId,@Param("planId") String planId);
	
	@Insert("insert into video_task (tenant_id,user_id, "
//			+ "id, "
			+ "plan_id, task_date,"
			+ "execute_start_time, execute_end_time, task_status"
			+ ")"
			+ "values (#{videoTask.tenantId,jdbcType=BIGINT},#{videoTask.userId,jdbcType=VARCHAR},"
//			+ "#{videoTask.taskId,jdbcType=INTEGER},"
			+ " #{videoTask.planId,jdbcType=VARCHAR}, "
			+ "#{videoTask.taskDate,jdbcType=VARCHAR},"
			+ "#{videoTask.executeStartTime,jdbcType=VARCHAR}, #{videoTask.executeEndTime,jdbcType=VARCHAR},"
			+ "#{videoTask.taskStatus,jdbcType=INTEGER}"
			+ ")")
	void createVideoTask(@Param("videoTask") VideoTask videoTask);

	/**
	 * 
	 * 描述：获取计划列表
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:51:11
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@Select("select t1.plan_id AS planId," +
			" t1.package_id AS packageId," +
			" t1.device_id AS deviceId," +
			" t1.plan_start_time AS planStartTime," +
			" t1.plan_end_time AS planEndTime," +
			" t1.plan_cycle AS planCycle," +
			" t1.plan_status AS planStatus," +
			" t1.plan_name AS planName," +
			" t1.plan_exec_status AS planExecStatus," +
			" t1.plan_order AS planOrder," +
			" t1.plan_end_time  AS planEndTime," +
			" t1.comedue_remind_state AS  renewRemindFlag "+
			" from video_plan t1 where" +
			" t1.tenant_id = #{tenantId,jdbcType=BIGINT}" +
			"  and t1.user_id = #{userId,jdbcType=VARCHAR}" +
			"  and t1.plan_end_time > STR_TO_DATE(#{timePara,jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s')" +
			"  order by t1.plan_order asc")
	Page<VideoPlanVo> getVideoPlanList(@Param("tenantId") long tenantId,@Param("userId") String userId,@Param("timePara") String timePara);

	@Select({
			"<script>",
			"SELECT v.plan_id AS planId, GROUP_CONCAT(v.task_date) AS taskDateStr FROM video_task v",
			" <if test='planIds != null and planIds.size() >0'>",
			" where v.plan_id in",
			" <foreach item='planId' index='index' collection='planIds' open='(' separator=',' close=')'>",
			" #{planId}",
			" </foreach>",
			" group by v.plan_id",
			" </if>",
			"</script>"
	})
	List<PlanTaskDto> getDayIndexByPlanId(@Param("planIds")List<String> planIds);

	@Select(
			"SELECT v.plan_id AS planId, GROUP_CONCAT(v.task_date) AS taskDateStr FROM video_task v"+
					" where v.plan_id =#{planId}"
	)
	PlanTaskDto getDayIndexByPlanIdExec(@Param("planId")String planId);


	@Select({
			"<script>",
			"SELECT plan_id AS planId, COUNT(1) AS useQuantity FROM video_event",
			" <if test='planIds != null and planIds.size() >0'>",
			" where plan_id in",
			" <foreach item='planId' index='index' collection='planIds' open='(' separator=',' close=')'>",
			" #{planId}",
			" </foreach>",
			" group by plan_id",
			" </if>",
			"</script>"
	})
	List<PlanEventDto> getEventUseQuantityByPlanId(@Param("planIds")List<String> planIds);

	@Select(
			"SELECT plan_id AS planId, COUNT(1) AS useQuantity FROM video_event "+
					" where plan_id =#{planId} "
	)
	PlanEventDto getEventUseQuantityByPlanIdExec(@Param("planId")String planId);


	@Select("select t1.plan_id AS planId," +
			" t1.package_id AS packageId," +
			" t1.device_id AS deviceId," +
			" t1.plan_start_time AS planStartTime," +
			" t1.plan_end_time AS planEndTime," +
			" t1.plan_cycle AS planCycle," +
			" t1.plan_status AS planStatus," +
			" t1.plan_name AS planName," +
			" t1.plan_exec_status AS planExecStatus," +
			" t1.plan_order AS planOrder," +
			" t1.plan_end_time  AS planEndTime," +
			" t1.comedue_remind_state AS  renewRemindFlag "+
			" from video_plan t1 where" +
			" t1.tenant_id = #{tenantId,jdbcType=BIGINT}" +
			"  and t1.user_id = #{userId,jdbcType=VARCHAR}  " +
			"  and t1.device_id = #{deviceId,jdbcType=VARCHAR}" +
			"  and t1.plan_end_time > STR_TO_DATE(#{timePara,jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s')" +
			"  order by t1.plan_order asc")
	VideoPlanInfoDto getPlanInfoByDevId(@Param("tenantId") long tenantId, @Param("userId") String userId, @Param("deviceId") String deviceId,@Param("timePara") String timePara);
	/**
	 * 
	 * 描述：获取任务
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:52:02
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @return
	 */
	@Select("select t1.plan_id            as planId,"
			+ "t1.id            as taskId,"
			+ "t1.task_date          as taskDate,"
			+ "t1.execute_start_time as executeStartTime,"
			+ "t1.execute_end_time   as executeEndTime,"
			+ "t2.plan_cycle         as planCycle,"
			+ "t2.plan_status         as planStatus "
			+ "from video_task t1 "
			+ "inner join video_plan t2 "
			+ "on t1.plan_id = t2.plan_id and t2.plan_exec_status in (0, 1, 2) "
			+ "where t2.tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "t2.user_id = #{userId,jdbcType=VARCHAR} and "
			+ "t2.plan_id = #{planId,jdbcType=VARCHAR}")
	List<VideoTaskVo> getVideoTaskList(@Param("tenantId") Long tenantId,@Param("userId") String userId, @Param("planId") String planId);

	/**
	 * 
	 * 描述：获取套餐
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:52:17
	 * @since 
	 * @param deviceType
	 * @return
	 */
	@Select("select id  as packageId,"
			+ "device_type as deviceType,"
			+ "package_name as packageName,"
			+ "package_desc as packageDesc,"
			+ "package_price as packagePrice,"
			+ "currency as currency,"
			+ "package_type as packageType,"
			+ "package_name_desc as packageNameDesc "
			+ "from video_package "
			+ "where device_type = #{deviceType,jdbcType=VARCHAR}")
	Page<VideoPackageVo> getVideoPackageList(@Param("deviceType") String deviceType);

	/**
	 * 
	 * 描述：创建计划
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:52:31
	 * @since 
	 * @param plan
	 * @return
	 */
	@Insert("insert into video_plan ("
//			+ "id, "
			+ "plan_id, tenant_id, plan_name,"
			+ "user_id, package_id, device_id, plan_start_time, plan_end_time, plan_exec_status,"
			+ "plan_cycle, plan_status, plan_order, create_time)"
			+ "values ("
//			+ "#{plan.id,jdbcType=BIGINT}, "
			+ "#{plan.planId,jdbcType=VARCHAR}, #{plan.tenantId,jdbcType=BIGINT}, "
			+ "#{plan.planName,jdbcType=VARCHAR}, #{plan.userId,jdbcType=VARCHAR},"
			+ "#{plan.packageId,jdbcType=BIGINT}, #{plan.deviceId,jdbcType=VARCHAR},"
			+ "#{plan.planStartTime,jdbcType=TIMESTAMP}, date_format(#{plan.planEndTime,jdbcType=TIMESTAMP},'%Y-%m-%d 23:59:59'),"
			+ "#{plan.planExecStatus,jdbcType=INTEGER}, #{plan.planCycle,jdbcType=VARCHAR},"
			+ "#{plan.planStatus,jdbcType=INTEGER}, #{plan.planOrder,jdbcType=INTEGER},"
			+ "#{plan.createTime,jdbcType=TIMESTAMP}"
			+ ")")
	int createPlan(@Param("plan") VideoPlan plan);


	/**
	 * 
	 * 描述：更新计划名
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:52:46
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planName
	 */
	@Update("update video_plan set plan_name = #{planName,jdbcType=VARCHAR} "
			+ "where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	void updatePlanName(@Param("tenantId") long tenantId,@Param("userId") String userId,@Param("planId") String planId,
							  @Param("planName") String planName);

	/**
	 * 
	 * 描述：获取历史记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:52:58
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param orderId
	 * @return
	 */
	@Select("select t1.id			as payRecordId,"
			+ "t1.order_id          			as orderId,"
			+ "t1.plan_id 						as planId,"
			+ "t1.package_id   					as packageId,"
			+ "t1.counts         				as counts,"
			+ "t1.create_time         			as payTime,"
			+ "t1.plan_start_time         		as planStartTime,"
			+ "t1.plan_end_time         		as planEndTime,"
			+ "t1.plan_status         			as planStatus "
			+ " from video_pay_record t1  "
			+ "where t1.tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "t1.user_id = #{userId,jdbcType=VARCHAR} and "
			+ "t1.plan_id = #{planId,jdbcType=VARCHAR} and t1.order_id != #{orderId,jdbcType=VARCHAR} ")
	Page<PayRecordVo> getBuyHisRecordList(@Param("tenantId") Long tenantId,@Param("userId") String userId,
										   @Param("planId") String planId, @Param("orderId") String orderId);

	/**
	 * 
	 * 描述：获取记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:53:16
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@Select("select t1.id			as payRecordId,"
			+ "t1.order_id          			as orderId,"
			+ "t1.plan_id 						as planId,"
			+ "t1.package_id   					as packageId,"
			+ "t1.counts         				as counts,"
			+ "t1.create_time         			as payTime,"
			+ "t1.plan_start_time         		as planStartTime,"
			+ "t1.plan_end_time         		as planEndTime,"
			+ "t1.plan_status         			as planStatus "
			+ " from (SELECT t.* from "
			+ " (SELECT DISTINCT(plan_id), id,order_id, package_id, counts, "
			+ "create_time,plan_start_time,plan_end_time,plan_status "
			+ " FROM video_pay_record where user_id=#{userId,jdbcType=VARCHAR}"
			+ " and tenant_id = #{tenantId,jdbcType=BIGINT}"
			+ " order BY plan_end_time desc) t"
			+ " GROUP BY t.plan_id ) t1")
	Page<PayRecordVo> getBuyRecordList(@Param("tenantId") Long tenantId,@Param("userId") String userId);

	/**
	 * 
	 * 描述：获取套餐价格
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:53:37
	 * @since 
	 * @param packageId
	 * @return
	 */
	@Select("select package_price as packagePrice,currency as currency,package_name as packageName from video_package where id = #{packageId}")
	AppPayDto getPackagePriceById(@Param("packageId") Long packageId);

	@Update("update video_plan set plan_end_time = #{plan.planEndTime,jdbcType=VARCHAR},comedue_remind_state= '0'," +
			" plan_exec_status=#{plan.planExecStatus},plan_status=#{plan.planStatus} "
			+ " where tenant_id = #{plan.tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{plan.userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{plan.planId,jdbcType=VARCHAR}")
	int renewPlan(@Param("plan") VideoPlan plan);

	@Select("select plan_id as planId,"
			+ "package_id as packageId,"
			+ "device_id as deviceId,"
			+ "plan_start_time as planStartTime,"
			+ "plan_end_time as planEndTime,"
			+ "plan_cycle as planCycle,"
			+ "plan_status as planStatus,"
			+ "plan_name as planName,"
			+ "plan_exec_status as planExecStatus"
			+ " from video_plan where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	VideoPlanVo getPlanEndTime(@Param("tenantId") Long tenantId,@Param("userId") String userId, @Param("planId") String planId);

	@Insert("insert into video_pay_record ("
//			+ "id, "
			+ "tenant_id, user_id,"
			+ "order_id, plan_id, package_id, counts, create_time, plan_start_time, plan_end_time, plan_status"
			+ ")"
			+ "values ("
//			+ "#{recordVo.payRecordId,jdbcType=BIGINT},"
			+ " #{tenantId,jdbcType=BIGINT}, "
			+ "#{userId, jdbcType=VARCHAR},"
			+ "#{recordVo.orderId,jdbcType=VARCHAR},"
			+ "#{recordVo.planId,jdbcType=VARCHAR},"
			+ "#{recordVo.packageId,jdbcType=BIGINT},"
			+ "#{recordVo.counts,jdbcType=INTEGER},"
			+ "#{recordVo.payTime,jdbcType=VARCHAR},"
			+ "#{recordVo.planStartTime,jdbcType=VARCHAR},"
			+ "#{recordVo.planEndTime,jdbcType=VARCHAR},"
			+ "#{recordVo.planStatus,jdbcType=INTEGER}"
			+ ")")
	void insertPayRecord(@Param("recordVo") PayRecordVo recordVo, @Param("tenantId") long tenantId, @Param("userId") String userId);

	@Select("select TIMESTAMPDIFF(DAY, plan_start_time, plan_end_time) where"
			+ " from video_plan where tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "user_id = #{userId,jdbcType=VARCHAR} and "
			+ "plan_id = #{planId,jdbcType=VARCHAR}")
	int getPlanLeftDays(@Param("tenantId") String tenantId,@Param("userId") String userId, @Param("planId") String planId);

	@Update("update video_plan set device_id = #{deviceId,jdbcType=VARCHAR} "
			+ " where 1=1"
			+ " and plan_id = #{planId,jdbcType=VARCHAR}"
			+ " and tenant_id = #{tenantId,jdbcType=BIGINT}"
			+ " and user_id = #{userId,jdbcType=VARCHAR}")
	void planBandingDevice(@Param("tenantId") Long tenantId,@Param("userId") String userId, 
			@Param("planId") String planId,@Param("deviceId") String deviceId);

	
	@Select("select t1.plan_id            as planId," +
			"       t1.plan_exec_status    as planExecStatus," +
			"       t1.plan_status         as planStatus," +
			"       t1.plan_cycle         as planCycle," +
			"       t1.package_id         as packageId," +
			"       t3.task_date          as taskDate," +
			"       t3.execute_start_time as executeStartTime," +
			"       t3.execute_end_time   as executeEndTime" +
			"  from video_plan t1" +
			" left join video_task t3" +
			"    on t1.plan_id = t3.plan_id" +
			" where t1.plan_id = #{planId,jdbcType=VARCHAR}" +
			"   and t1.plan_exec_status in ('0', '1', '2')"
			/*"   and STR_TO_DATE(#{nowTime,jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s') >=" +
			"       t1.plan_start_time" +
			"   and t1.plan_end_time >=" +
			"       STR_TO_DATE(#{nowTime,jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s')"*/
			)
	List<VideoPlanTaskDto> getSyncTaskInfo(@Param("planId") String planId, @Param("nowTime") String nowTime);

	@Select("select t.device_id from video_plan t " +
			" where t.plan_id = #{planId,jdbcType=VARCHAR}"
	)
	String getDeviceId(@Param("planId") String planId);

	@Select("select  t.plan_id from video_plan t " +
			" where t.device_id = #{deviceId,jdbcType=VARCHAR}"
	)
	String getPlanId(@Param("deviceId") String deviceId);

	@Insert("INSERT INTO video_file ("
//			+ "id,"
			+ " tenant_id,plan_id,file_id,device_id,video_type," +
			"       		video_start_time,video_end_time,file_exp_date,file_type," +
			"				event_uuid,video_length,file_size,create_time)" +
			"       VALUES ("
//			+ "#{videoFile.videoFileId,jdbcType=BIGINT}," 
			+ "				#{videoFile.tenantId,jdbcType=BIGINT},"+
			"				#{videoFile.planId,jdbcType=VARCHAR},"+
			"				#{videoFile.fileId,jdbcType=VARCHAR}," +
			"      		    #{videoFile.deviceId,jdbcType=VARCHAR}," +
			"				#{videoFile.videoType,jdbcType=VARCHAR}," +
			"				#{videoFile.videoStartTime}," +
			"       		#{videoFile.videoEndTime}," +
			"       		#{videoFile.fileExpDate}," +
			"       		#{videoFile.fileType,jdbcType=VARCHAR}," +
			"				#{videoFile.eventUuid,jdbcType=VARCHAR}," +
			"      		    #{videoFile.videoLength}," +
			"				#{videoFile.fileSize}," +
			"				date_format(now(),'%Y-%m-%d %H:%i:%s')) "
			)
	void createVideoFile(@Param("videoFile") VideoFile videoFile);


	@Select("select t.tenant_id from video_plan t " +
			" where t.plan_id = #{planId,jdbcType=VARCHAR}"
	)
	Long getTenantId(@Param("planId") String planId);

	@Select("SELECT COUNT(1) FROM video_plan WHERE device_id =  #{deviceId,jdbcType=VARCHAR}")
	int countDeviceBandingPlan(@Param("deviceId") String deviceId);

	@Select({ "<script>",
			"SELECT device_id FROM video_plan WHERE NOW()>= plan_start_time AND NOW() &lt; plan_end_time AND device_id in  ",
			"<foreach item='item' index='index' collection='deviceIdList'",
			"open='(' separator=',' close=')'>",
			"#{item}",
			"</foreach>",
			"</script>" })
	List<String> selectBandedPlanDeviceIds(@Param("deviceIdList") List<String> deviceIdList);

	@Select("SELECT COUNT(1) FROM video_plan WHERE plan_id =  #{planId,jdbcType=VARCHAR}")
	int countPlanById(@Param("planId") String planId);

	
	@Select("SELECT " + 
			"	file_id " + 
			"FROM " + 
			"	video_file " + 
			"WHERE " + 
			"	tenant_id = #{tenantId} " + 
			"AND event_uuid = #{eventUUID} " + 
			"AND plan_id = #{planId} " + 
			"AND data_status = 1 ")
	List<String> getEventFileId(@Param("tenantId") Long tenantId, @Param("planId") String planId,@Param("eventUUID") String eventUUID);
	
	/**
	 * @despriction：统计设备已绑定的计划
	 * @author  yeshiyuan
	 * @created 2018/5/17 14:05
	 * @param deviceId 设备id
	 * @return
	 */
	@Select("SELECT count(1) from video_plan where device_id=#{deviceId} and plan_end_time > NOW() ")
	int countDeviceHasBindPlan(@Param("deviceId") String deviceId);


	@Select("select t1.id			as payRecordId,"
			+ "t1.order_id          			as orderId,"
			+ "t1.user_id          				as userId,"
			+ "t1.plan_id 						as planId,"
			+ "t1.package_id   					as packageId,"
			+ "t1.counts         				as counts,"
			+ "t1.create_time         			as payTime,"
			+ "t1.plan_start_time         		as planStartTime,"
			+ "t1.plan_end_time         		as planEndTime,"
			+ "t1.plan_status         			as planStatus "
			+ " from video_pay_record t1 "
			+ "where t1.tenant_id = #{tenantId,jdbcType=BIGINT} and "
			+ "t1.plan_id = #{planId,jdbcType=VARCHAR} and t1.plan_status != #{planStatus,jdbcType=INTEGER} "
			+"  ORDER BY t1.create_time DESC LIMIT 1")
	VideoPayRecordDto getLatestOrderByPlanIdAndStatus(@Param("tenantId") Long tenantId,
													  @Param("planId") String planId, @Param("planStatus") Integer planStatus);

	/**
	  * @despriction：查询video_pay_record
	  * @author  yeshiyuan
	  * @created 2018/5/22 10:18
	  * @param planId 计划id
	  * @param orderId 订单id
	  * @param tenantId 租户id
	  * @return
	  */
	@Select("select id," +
			"tenant_id as tenantId," +
			"user_id as userId," +
			"order_id as orderId," +
			"plan_id as planId," +
			"package_id as packageId," +
			"counts," +
			"create_time as createTime," +
			"plan_start_time as planStartTime," +
			"plan_end_time as planEndTime," +
			"plan_status as planStatus " +
			" from video_pay_record where plan_id=#{planId} and order_id = #{orderId} and tenant_id=#{tenantId} limit 1")
	VideoPayRecord getVideoPayRecord(@Param("planId") String planId, @Param("orderId") String orderId, @Param("tenantId") Long tenantId);

	/**
	  * @despriction：视频购买记录失效
	  * @author  yeshiyuan
	  * @created 2018/5/22 14:11
	  * @return
	  */
	@Update("update video_pay_record set plan_status=#{planStatus} where plan_id=#{planId} and order_id = #{orderId} and tenant_id=#{tenantId}")
	int updateVideoPayRecordOfPlanStatus(@Param("planId") String planId,@Param("orderId") String orderId,@Param("tenantId") Long tenantId,@Param("planStatus") Integer planStatus);

	/**
	  * @despriction：获取计划某一订单的时间差
	  * @author  yeshiyuan
	  * @created 2018/5/22 14:46
	  * @return
	  */
	@Select("SELECT DATEDIFF(plan_end_time,plan_start_time) from video_pay_record where plan_id=#{planId} and order_id =#{orderId} and tenant_id=#{tenantId}")
	int getPayRecordDateDiff(@Param("planId") String planId,@Param("orderId") String orderId,@Param("tenantId") Long tenantId);

	/**
	  * @despriction：描述
	  * @author  yeshiyuan
	  * @created 2018/5/22 15:03
	  * @param planId 计划id
	  * @param dateDiff 时间差
	  * @return
	  */
	@Update("UPDATE video_plan " +
			"set plan_exec_status= (" +
			" SELECT a.plan_exec_status from " +
			"   (SELECT " +
			"      IF(DATE_SUB(p.plan_end_time,INTERVAL #{dateDiff} DAY) <= NOW(),0,p.plan_exec_status) as plan_exec_status " +
			"    from video_plan p where p.plan_id=#{planId}" +
			"   ) a  " +
			" ), " +
			"plan_end_time= DATE_SUB(plan_end_time,INTERVAL #{dateDiff} DAY) " +
			"where plan_id = #{planId}")
	int updatePlanEndTime(@Param("planId")String planId,@Param("dateDiff") Integer dateDiff);
	
	/**
	 * 
	 * 描述：获取记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:53:16
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@Select({"<script>", 
			"SELECT t1.id AS payRecordId, " +
			"	t1.user_id AS userId, " +
			"	t1.tenant_id AS tenant_id, " +
			"	t1.order_id AS orderId, " +
			"	t1.plan_id AS planId, " +
			"	t1.package_id AS packageId, " +
			"	t1.counts AS counts, " +
			"	t1.create_time AS payTime, " +
			"	t1.plan_start_time AS planStartTime, " +
			"	t1.plan_end_time AS planEndTime, " +
			"	t1.plan_status AS planStatus " +
			"FROM  (  SELECT  t.*  FROM  (  SELECT DISTINCT " +
			"					(plan_id), " +
			"					id, " +
			"					order_id, " +
			"					package_id, " +
			"					counts, " +
			"					create_time, " +
			"					plan_start_time, " +
			"					plan_end_time, " +
			"					plan_status, " +
			"					user_id, " +
			"					tenant_id " +
			"				FROM " +
			"					video_pay_record " +
			"				WHERE " +
			"					1 = 1 " +
			"				ORDER BY " +
			"					plan_end_time DESC " +
			"			) t  GROUP BY  t.plan_id  ) t1 " +
			" where 1 = 1 ",
			"<if test=\"key != null and key != ''.toString()\"> and (t1.user_id = #{key,jdbcType=VARCHAR} "
			+ "or t1.tenant_id = #{key,jdbcType=VARCHAR} or t1.order_id = #{key,jdbcType=VARCHAR}) </if>",
			//时间查询 ：0-付款日期，1-计划开始时间
			"<if test=\"timeType != null and timeType == '0'.toString()\"> and t1.create_time >= STR_TO_DATE(#{startTime},'%Y-%m-%d %H:%i:%s')"
					+ " and t1.create_time <![CDATA[<=]]> STR_TO_DATE(#{endTime},'%Y-%m-%d %H:%i:%s') </if>",
			"<if test=\"timeType != null and timeType == '1'.toString()\"> and t1.plan_start_time >= STR_TO_DATE(#{startTime},'%Y-%m-%d %H:%i:%s')"
					+ " and t1.plan_start_time <![CDATA[<=]]> STR_TO_DATE(#{endTime},'%Y-%m-%d %H:%i:%s') </if>",
			//订单状态：0-支付成功，1-服务开通，2-服务开通失败，3-服务已关闭
			"<if test=\"recordState != null and recordState == '0'.toString()\"> and t1.plan_start_time > NOW() </if>",
			"<if test=\"recordState != null and recordState == '1'.toString()\"> and t1.plan_end_time > NOW() </if>",
			"<if test=\"recordState != null and recordState == '3'.toString()\"> and t1.plan_end_time <![CDATA[<]]> NOW()</if>",
			"</script>"})
	List<AllRecordVo> getAllBuyRecordList(AllRecordSearchParam searchParam);

	@Select("SELECT r.order_id as orderId," +
			"r.plan_id as planId," +
			"r.package_id as packageId," +
			"r.user_id as userId," +
			"r.create_time as payTime," +
			"r.plan_start_time as planStartTime," +
			"r.plan_end_time as planEndTime," +
			"CASE WHEN r.plan_start_time > #{now} AND r.plan_status!=4 THEN 0 " +
			"WHEN r.plan_start_time <= #{now} AND r.plan_end_time > #{now} AND r.plan_status!=4 THEN 1 " +
			"WHEN DATE_ADD(r.plan_start_time,INTERVAL 5 DAY) > #{now} and r.plan_start_time < #{now} AND  r.plan_status!=4 then 2 " +
			"WHEN DATE_ADD(r.plan_start_time,INTERVAL 5 DAY) < #{now} AND  r.plan_status!=4 then 3 " +
			"WHEN r.plan_status=4 then 4 " +
			"end as orderStatus," +
			"p.plan_name " +
			"from video_pay_record r " +
			"LEFT JOIN video_plan p on r.plan_id=p.plan_id " +
			"where r.order_id != #{orderId} and r.plan_id= #{planId}" +
			"ORDER BY r.create_time DESC")
	List<VideoPlanOrderDto> queryPlanOtherPayRecord(@Param("orderId") String orderId,@Param("planId") String planId,@Param("now") Date now);

	/**
	 * 描述：查询设备最后绑定的计划
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	@Select("select v.plan_id from video_plan v" +
			" where v.tenant_id = #{tenantId}" +
			" and v.device_id = #{deviceId}" +
			" order by v.create_time desc limit 1")
	String getBandingDevicePlanId(@Param("tenantId") Long tenantId, @Param("deviceId") String deviceId);

	/**
	 * 描述：计划解绑设备
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	@Update("update video_plan v set v.device_id = null" +
			" where v.tenant_id = #{tenantId}" +
			" and v.plan_id = #{planId}")
	int planUnBandingDevice(@Param("tenantId") Long tenantId, @Param("planId") String planId);

	/**
	 * 描述：获取套餐信息
	 * @author mao2080@sina.com
	 * @created 2018/6/12 17:51
	 * @param tenantId 租户id
	 * @param planId 计划id
	 * @return void
	 */
	@Select("select pl.package_id as packageId from video_plan pl" +
			" where pl.plan_id = #{planId}" +
			" and pl.tenant_id = #{tenantId}")
	Long getPackageId(@Param("tenantId") Long tenantId, @Param("planId") String planId);



}