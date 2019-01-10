package com.iot.video.dao;

import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.vo.EventVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface VideoTimerMapper {

    /**
     * 
     * 描述：获取过期的计划ID和套餐天数
     * @author 李帅
     * @created 2018年3月22日 上午11:00:46
     * @since 
     * @return
     * @throws Exception
     */
	@Select("SELECT t7.planId,t7.eventOrFulltimeAmount FROM (SELECT t6.planId,t6.eventOrFulltimeAmount, count(1) as countDay" + 
			"			  FROM (" + 
			"			SELECT distinct t1.plan_id as planId," + 
			"			                DATE_FORMAT(t2.video_start_time, '%Y-%m-%d') as fileTime," + 
			"			                t.EVENT_OR_FULLTIME_AMOUNT as eventOrFulltimeAmount" + 
			"			  FROM video_package t," + 
			"			       video_plan    t1," + 
			"			       video_file    t2" + 
			"			 where 1 = 1" + 
			"			   and t.id = t1.package_id" + 
			"			   and t1.plan_id = t2.plan_id" + 
			"			   and t1.plan_exec_status = '1'" + 
			"			   and t.package_type = '0'" + 
			"			   and t2.file_type = 'ts') t6" + 
			"			 group by t6.planId,t6.eventOrFulltimeAmount)t7" + 
			"			 where t7.eventOrFulltimeAmount < t7.countDay")
	List<Map<String, String>> getLapsePlanIdList();

	/**
	 * 
	 * 描述：获取计划录影日期
	 * @author 李帅
	 * @created 2018年3月22日 上午11:01:28
	 * @since 
	 * @param planIdKey
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT t1.fileTime" + 
			"		  FROM (SELECT distinct t1.plan_id as planId," + 
			"		                        DATE_FORMAT(t2.video_start_time, '%Y-%m-%d') as fileTime" + 
			"		          FROM video_package t," + 
			"		               video_plan    t1," + 
			"		               video_file    t2" + 
			"		         where 1 = 1" + 
			"		           and t.id = t1.package_id" + 
			"		           and t1.plan_id = t2.plan_id" + 
			"		           and t1.plan_exec_status = '1'" + 
			"		           and t.package_type = '0'" + 
			"		           and t2.file_type = 'ts'" + 
			"		           and t1.plan_id = #{planIdKey}) t1" + 
			"		 order by t1.fileTime desc")
	List<String> getPlanVideoDates(@Param("planIdKey") String planIdKey);
	
	/**
	 * 
	 * 描述：获取全时录影失效文件ID
	 * @author 李帅
	 * @created 2018年3月22日 上午11:02:03
	 * @since 
	 * @param planIdKey
	 * @param dueTime
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT t2.file_id" + 
			"		  FROM video_package t," + 
			"		       video_plan    t1," + 
			"		       video_file    t2" + 
			"		 where 1 = 1" + 
			"		   and t.id = t1.package_id" + 
			"		   and t1.plan_id = t2.plan_id" + 
			"		   and t1.plan_exec_status = '1'" + 
			"		   and t.package_type = '0'" + 
			"		   and t2.video_end_time < STR_TO_DATE(CONCAT(#{dueTime},DATE_FORMAT(NOW(), ' %H')),'%Y-%m-%d %H') " + 
			"		   and t2.file_type = 'ts'" + 
			"       and t2.plan_id = #{planIdKey}" + 
			"union all" + 
			"		SELECT t2.file_id" + 
			"		  FROM video_package t," + 
			"		       video_plan    t1," + 
			"		       video_file    t2," + 
			"		       video_event    t3" + 
			"		 where 1 = 1" + 
			"		   and t.id = t1.package_id" + 
			"		   and t1.plan_id = t2.plan_id" + 
			"		   and t3.plan_id = t1.plan_id" + 
			"		   and t3.event_uuid = t2.event_uuid" + 
			"		   and t1.plan_exec_status = '1'" + 
			"		   and t.package_type = '0'" + 
			"		   and t3.event_oddur_time < STR_TO_DATE(CONCAT(#{dueTime},DATE_FORMAT(NOW(), ' %H')),'%Y-%m-%d %H') " + 
			"		   and t2.file_type = 'jpg'" + 
			"       and t2.plan_id = #{planIdKey}")
	List<String> getLapseFileIdList(@Param("planIdKey") String planIdKey, @Param("dueTime") String dueTime);
	
	/**
	 * 
	 * 描述：获取全时录影失效事件ID
	 * @author 李帅
	 * @created 2018年3月22日 上午11:02:33
	 * @since 
	 * @param planIdKey
	 * @param dueTime
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT t3.id" + 
			"		  FROM video_package t," + 
			"		       video_plan    t1," + 
			"		       video_file    t2," + 
			"		       video_event    t3" + 
			"		 where 1 = 1" + 
			"		   and t.id = t1.package_id" + 
			"		   and t1.plan_id = t2.plan_id" + 
			"		   and t3.plan_id = t1.plan_id" + 
			"		   and t3.event_uuid = t2.event_uuid" + 
			"		   and t1.plan_exec_status = '1'" + 
			"		   and t.package_type = '0'" + 
			"		   and t3.event_oddur_time < STR_TO_DATE(CONCAT(#{dueTime},DATE_FORMAT(NOW(), ' %H')),'%Y-%m-%d %H') " + 
			"		   and t2.file_type = 'jpg'" + 
			"       and t2.plan_id = #{planIdKey}")
	List<Long> getLapseEventIdList(@Param("planIdKey") String planIdKey, @Param("dueTime") String dueTime);
	
	/**
	 * 
	 * 描述：删除全时录影失效文件数据
	 * @author 李帅
	 * @created 2018年3月22日 上午11:02:33
	 * @since 
	 * @param planIdKey
	 * @param dueTime
	 * @return
	 * @throws Exception
	 */
	@Delete({ "<script>", 
			"delete ",
			"FROM video_file ",
			"WHERE file_id IN ",
			"<foreach item='item' index='index' collection='lapseFileId'",
				"open='(' separator=',' close=')'>", 
				"#{item}",
			"</foreach>", 
			"</script>" })
	void deleteLapseFile(@Param("lapseFileId") List<String> lapseFileId);
	
	/**
	 * 
	 * 描述：删除全时录影失效事件数据
	 * @author 李帅
	 * @created 2018年3月22日 上午11:19:49
	 * @since 
	 * @param lapseEventId
	 * @throws Exception
	 */
	@Delete({ "<script>", 
		"delete ",
		"FROM video_event ",
		"WHERE id IN ",
		"<foreach item='item' index='index' collection='lapseEventId'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void deleteLapseEvent(@Param("lapseEventId") List<Long> lapseEventId);
	
	/**
	 * 
	 * 描述：获取计划信息
	 * @author 李帅
	 * @created 2018年3月23日 下午3:12:00
	 * @since 
	 * @param planQueryState
	 * @return
	 * @throws Exception
	 */
	@Select({ "<script>", 
		"select t.plan_id as planId, t.plan_name as planName, DATE_FORMAT(t.plan_start_time, '%Y-%m-%d %H') as planStartTime,"+ 
		" DATE_FORMAT(t.plan_end_time, '%Y-%m-%d %H') as planEndTime, "
		+ "t.user_id as userId,t.tenant_id as tenantId,t.device_id as deviceId,t.package_id as packageId " +
		" from video_plan t " +
		" where 1 = 1",
		" <if test=\"planQueryState == '0'.toString()\"> and t.comedue_remind_state = '0' and t.plan_exec_status in ('0', '1')"
		+ " and t.plan_end_time <![CDATA[<=]]> STR_TO_DATE(DATE_FORMAT((NOW() + interval '5' day), '%Y-%m-%d %H'),'%Y-%m-%d %H') </if>",
		" <if test=\"planQueryState == '1'.toString()\"> and t.comedue_remind_state = '1' and t.plan_exec_status in ('0', '1')"
		+ " and t.plan_end_time <![CDATA[<=]]> STR_TO_DATE(DATE_FORMAT((NOW()), '%Y-%m-%d %H'),'%Y-%m-%d %H') </if>",
		" <if test=\"planQueryState == '2'.toString()\"> and t.comedue_remind_state = '2' and t.plan_exec_status ='2'"
		+ " and t.plan_end_time <![CDATA[<=]]> STR_TO_DATE(DATE_FORMAT((NOW() - interval '5' day), '%Y-%m-%d %H'),'%Y-%m-%d %H') </if>", 
		" <if test=\"planQueryState == '21'.toString()\"> and t.comedue_remind_state = '2' and t.plan_exec_status ='2'"
		+ " and DATE_FORMAT(t.plan_end_time, '%Y-%m-%d %H') = DATE_FORMAT((NOW() - interval '1' day), '%Y-%m-%d %H') </if>",
		" <if test=\"planQueryState == '22'.toString()\"> and t.comedue_remind_state = '2' and t.plan_exec_status ='2'"
		+ " and DATE_FORMAT(t.plan_end_time, '%Y-%m-%d %H') = DATE_FORMAT((NOW() - interval '2' day), '%Y-%m-%d %H') </if>",
		" <if test=\"planQueryState == '23'.toString()\"> and t.comedue_remind_state = '2' and t.plan_exec_status ='2'"
		+ " and DATE_FORMAT(t.plan_end_time, '%Y-%m-%d %H') = DATE_FORMAT((NOW() - interval '3' day), '%Y-%m-%d %H') </if>",
		" <if test=\"planQueryState == '24'.toString()\"> and t.comedue_remind_state = '2' and t.plan_exec_status ='2'"
		+ " and DATE_FORMAT(t.plan_end_time, '%Y-%m-%d %H') = DATE_FORMAT((NOW() - interval '4' day), '%Y-%m-%d %H') </if>", 
		"</script>" })
	List<SchedulePlanDto> getSchedulePlanInfoList(@Param("planQueryState") String planQueryState);
	
	/**
	 * 
	 * 描述：批量更新计划提醒状态
	 * @author 李帅
	 * @created 2018年3月22日 上午11:54:36
	 * @since 
	 * @param planIdList
	 * @param comedueRemindState
	 * @throws Exception
	 */
	@Update({ "<script>", 
		"update ",
		"video_plan ",
		"set COMEDUE_REMIND_STATE = #{comedueRemindState} ",
		"WHERE PLAN_ID IN ",
		"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void batchUpdatePlanRemindState(@Param("planIdList") List<String> planIdList, @Param("comedueRemindState") String comedueRemindState);
	
	/**
	 * 
	 * 描述：批量更新计划执行状态
	 * @author 李帅
	 * @created 2018年3月22日 下午1:44:51
	 * @since 
	 * @param planIdList
	 * @param planExecStatus
	 * @throws Exception
	 */
	@Update({ "<script>", 
		"update ",
		"video_plan ",
		"set plan_exec_status = #{planExecStatus} ",
		"WHERE PLAN_ID IN ",
		"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void batchUpdatePlanState(@Param("planIdList") List<String> planIdList, @Param("planExecStatus") String planExecStatus);
	
	/**
	 * 
	 * 描述：批量更新计划执行状态
	 * @author 李帅
	 * @created 2018年3月22日 下午1:44:51
	 * @since 
	 * @param planIdList
	 * @param planExecStatus
	 * @throws Exception
	 */
	@Update({ "<script>", 
		"update ",
		"video_pay_record ",
		"set plan_status = #{planExecStatus} ",
		"WHERE PLAN_ID IN ",
		"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void batchUpdatePayRecordPlanState(@Param("planIdList") List<String> planIdList, @Param("planExecStatus") String planExecStatus);
	

	
	/**
	 * 
	 * 描述：删除计划文件数据
	 * @author 李帅
	 * @created 2018年3月22日 下午2:26:20
	 * @since 
	 * @param planIdList
	 * @throws Exception
	 */
	@Delete({ "<script>", 
		"delete ",
		"FROM video_file ",
		"where 1=1 and data_status = 0 and PLAN_ID in ",
		"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void deleteLapseFileByPlanId(@Param("planIdList") List<String> planIdList);
	
	/**
	 * 
	 * 描述：删除计划事件数据
	 * @author 李帅
	 * @created 2018年3月22日 下午2:29:35
	 * @since 
	 * @param planIdList
	 * @throws Exception
	 */
	@Delete({ "<script>", 
		"delete ",
		"FROM video_event ",
		"where 1=1 and data_status = 0 and PLAN_ID in ",
		"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>", 
			"#{item}",
		"</foreach>", 
		"</script>" })
	void deleteLapseEventByPlanId(@Param("planIdList") List<String> planIdList);
	
	/**
	 * 
	 * 描述：获取计划ID和计划套餐量
	 * @author 李帅
	 * @created 2018年3月22日 下午2:37:54
	 * @since 
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT a.planId, a.beyondAmount FROM (" + 
			" SELECT DISTINCT t1.plan_id AS planId, t3.userAmount -t2.event_or_fulltime_amount AS beyondAmount" + 
			" FROM video_plan t1, video_package t2," + 
			" (select t.plan_id,count(t.plan_id) as userAmount from video_event t GROUP BY t.plan_id) t3" + 
			" WHERE 1 = 1 AND t1.package_id = t2.id AND t2.package_type = '1'" + 
			" AND t1.plan_id = t3.plan_id and t3.userAmount > t2.event_or_fulltime_amount ) AS a")
	List<VideoPlanBeyond> getAllVenderPlanList();
	
	/**
	 * 
	 * 描述：排序获取前num条事件信息
	 * @author 李帅
	 * @created 2018年3月22日 下午2:54:46
	 * @since 
	 * @param planId
	 * @param batchValue
	 * @return
	 * @throws Exception
	 */
	@Select("select t.plan_id as planId,t.id as eventId,"
			+ "DATE_FORMAT(t.event_oddur_time,'%Y-%m-%d %H:%i:%s') as eventOddurTime " + 
			"	     from video_event t where t.plan_id=#{planId}" + 
			"	     order by t.event_oddur_time asc" + 
			"	     limit #{batchValue}")
	List<EventVo> getSortVideoEventInfo(@Param("planId") String planId, @Param("batchValue") int batchValue);
	
	/**
	 * 
	 * 描述：获取事件关联的ts文件信息
	 * @author 李帅
	 * @created 2018年3月22日 下午3:02:14
	 * @since 
	 * @param planId
	 * @param eventOddurTime
	 * @return
	 * @throws Exception
	 */
	@Select("select t.file_id as fileId from video_file t "
			+ "where t.plan_id=#{planId} and (t.file_type='ts' or t.file_type='jpg')" + 
			"        and t.video_end_time <= #{eventOddurTime}")
	List<String> getEventTsFileInfo(@Param("planId") String planId, @Param("eventOddurTime") Date eventOddurTime);


	/**
	 * @despriction：批量处理计划过期5天之内的数据
	 * @author  yeshiyuan
	 * @created 2018/6/14 15:04
	 * @param planIds 计划集合
	 * @return
	 */
	@Update({ "<script>",
			"update ",
			"video_plan ",
			"set plan_exec_status = 2,comedue_remind_state=2,plan_status=0 ",
			"WHERE PLAN_ID IN ",
			"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>",
			"#{item}",
			"</foreach>",
			"</script>" })
	int bacthDealPlanExpireWithInFiveDay(@Param("planIdList") List<String> planIdList);

	/**
	 * @despriction：批量处理计划过期5天之后的数据
	 * @author  yeshiyuan
	 * @created 2018/6/14 15:04
	 * @param planIds 计划集合
	 * @return
	 */
	@Update({ "<script>",
			"update ",
			"video_plan ",
			"set plan_exec_status = 3,comedue_remind_state=3,plan_status=0 ",
			"WHERE PLAN_ID IN ",
			"<foreach item='item' index='index' collection='planIdList'",
			"open='(' separator=',' close=')'>",
			"#{item}",
			"</foreach>",
			"</script>" })
	int bacthDealPlanExpireAfterFiveDay(@Param("planIdList") List<String> planIdList);

	/**
	  * @despriction：统计失效文件数据
	  * @author  yeshiyuan
	  * @created 2018/6/26 15:26
	  * @param planId 计划uuid
	  * @return
	  */
	@Select("select count(1) from video_file where plan_id = #{planId} and data_status = 0")
	int countInvalidVideoFile(@Param("planId")String planId);

	/**
	 * @despriction：统计失效事件数据
	 * @author  yeshiyuan
	 * @created 2018/6/26 15:26
	 * @param planId 计划uuid
	 * @return
	 */
	@Select("select count(1) from video_event where plan_id = #{planId} and data_status = 0")
	int countInvalidVideoEvent(@Param("planId")String planId);
}