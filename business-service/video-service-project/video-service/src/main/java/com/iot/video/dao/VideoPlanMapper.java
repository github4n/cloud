package com.iot.video.dao;

import com.iot.video.dto.PlanInfoDto;
import com.iot.video.dto.VideoPackageDto;
import com.iot.video.dto.VideoPlanDto;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.entity.VideoPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VideoPlanMapper {


    @Select("<script>"
            +"select t.plan_id as planId,"
            +"t.device_id as deviceId "
            +"from video_plan t where 1=1"
            +"<if test=\"deviceId != null and deviceId != ''\"> and t.device_id=#{deviceId,jdbcType=VARCHAR}</if>"
            +"<if test=\"planId != null and planId != ''\"> and t.plan_id=#{planId,jdbcType=VARCHAR}</if>"
            + "</script>")
    VideoPlanDto getPlanByDeviceId(@Param("deviceId") String deviceId,@Param("planId") String planId);



    /**
     * 描述：修改录影排序
     * @author mao2080@sina.com
     * @created 2018/3/23 14:38
     * @param  planOrder planId
     * @return void
     */
    @Update("update video_plan set plan_order=#{planOrder} where plan_id=#{planId}")
    void updatePlanOrder(@Param("planOrder") String planOrder,@Param("planId") String planId);

    /**
     * 
     * 描述：根据设备id查询计划类型
     * @author 李帅
     * @created 2018年4月27日 下午4:02:47
     * @since 
     * @param tenantId
     * @param userId
     * @param deviceId
     * @return com.lds.iot.video.dto.PlanInfoDto
     */
    @Select({"<script>",
	    	"SELECT" +
	    	"	t.plan_id AS planId FROM video_plan t WHERE 1 = 1 " +
	    	" and t.device_id=#{deviceId,jdbcType=VARCHAR} "
	    	+ " and t.user_id = #{userId,jdbcType=VARCHAR} "
			+ " and t.tenant_id = #{tenantId,jdbcType=BIGINT} ",
            "</script>"})
    PlanInfoDto getPlanType(@Param("tenantId") Long tenantId, @Param("userId") String userId, @Param("deviceId") String deviceId);

    /**
      * @despriction：获取video_plan详情
      * @author  yeshiyuan
      * @created 2018/5/17 15:37
      * @param planId 计划id
      * @param userId 用户uuid
      * @return
      */
    @Select("<script>" +
            "select id," +
            "plan_id as planId," +
            "plan_name as planName," +
            "tenant_id as tenantId," +
            "user_id as userId," +
            "package_id as packageId," +
            "device_id as deviceId," +
            "plan_start_time as planStartTime," +
            "plan_end_time as planEndTime," +
            "plan_exec_status as planExecStatus," +
            "plan_cycle as planCycle," +
            "plan_status as planStatus," +
            "plan_order as planOrder," +
            "create_time as createTime" +
            " from video_plan where plan_id = #{planId} " +
            " <if test=\"userId != null and userId != ''\">" +
            " and user_id=#{userId} " +
            "</if>" +
            "</script>")
    VideoPlan getVideoPlan(@Param("planId")String planId,@Param("userId") String userId);

    /**
      * @despriction：通过计划查找套餐信息
      * @author  yeshiyuan
      * @created 2018/6/7 17:32
      * @param null
      * @return
      */
    @Select("select id as packageId," +
            "device_type as deviceType," +
            "package_name as packageName," +
            "package_name_desc as packageNameDesc," +
            "package_type as packageType," +
            "event_or_fulltime_amount as eventOrFulltimeAmount," +
            "package_desc as packageDesc," +
            "package_price as packagePrice," +
            "currency as currency " +
            "from video_package " +
            "where id = " +
            "(select package_id from video_plan where plan_id = #{planId})")
    VideoPackageDto getPackageByPlanId(@Param("planId") String planId);

    /**
     * 描述：查套餐Id
     * @author nongchongwei
     * @date 2018/7/17 16:59
     * @param
     * @return
     */
    @Select("select pl.package_id as packageId from video_plan pl" +
            " where pl.plan_id = #{planId}")
    Long getPackageId(@Param("planId") String planId);


    @Select("select id," +
            " tenant_id as tenantId," +
            " user_id as userId," +
            " order_id as orderId," +
            " plan_id as planId," +
            " package_id as packageId," +
            " counts," +
            " plan_start_time as planStartTime," +
            " plan_end_time as planEndTime" +
            " from video_pay_record where plan_id = #{planId} order by create_time desc limit 1")
    VideoPayRecord getLastVideoPayRecord(@Param("planId") String planId);

    /**
      * @despriction：查找计划绑定的用户id
      * @author  yeshiyuan
      * @created 2018/8/13 17:05
      * @param null
      * @return 
      */
    @Select("select user_id from video_plan where plan_id = #{planId}")
    String getUserIdByPlanId(@Param("planId") String planId);
}