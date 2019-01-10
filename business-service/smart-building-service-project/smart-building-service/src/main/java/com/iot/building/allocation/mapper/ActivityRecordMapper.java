package com.iot.building.allocation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;


public interface ActivityRecordMapper {

    @Insert({
			"insert into activity_record (id, type, ",
			"icon, activity, ",
			"time, create_by, ",
			"foreign_id, del_flag, ",
			"tenant_id, org_id, device_name, ",
			"result, set_time,location_id,user_name,space_id,template_name,space_template_id)",
			"values (#{id,jdbcType=BIGINT}, #{type,jdbcType=VARCHAR}, ",
			"#{icon,jdbcType=VARCHAR}, #{activity,jdbcType=VARCHAR}, ",
			"#{time,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT}, ",
			"#{foreignId,jdbcType=VARCHAR}, #{delFlag,jdbcType=BIT}, ",
			"#{tenantId,jdbcType=BIGINT}, #{orgId,jdbcType=BIGINT}, #{deviceName,jdbcType=VARCHAR}, ",
			"#{result,jdbcType=INTEGER},#{setTime,jdbcType=TIMESTAMP},#{locationId},#{userName},#{spaceId},#{templateName},#{spaceTemplateId})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(ActivityRecordReq activityRecord);

    @UpdateProvider(type = ActivityRecordSqlProvider.class, method = "deleteByCondition")
    int deleteByCondition(@Param("activityRecordReq") ActivityRecordReq activityRecordReq);

    @SelectProvider(type = ActivityRecordSqlProvider.class, method = "queryByCondition")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
            @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity", property="activity", jdbcType=JdbcType.VARCHAR),
            @Result(column="time", property="time", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="foreign_id", property="foreignId", jdbcType=JdbcType.VARCHAR),
            @Result(column="del_flag", property="delFlag", jdbcType=JdbcType.BIT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT),
            @Result(column="device_name", property="deviceName", jdbcType=JdbcType.VARCHAR),
            @Result(column="result", property="result", jdbcType= JdbcType.INTEGER),
            @Result(column="set_time", property="setTime", jdbcType= JdbcType.VARCHAR),
			@Result(column="user_name", property="userName", jdbcType= JdbcType.VARCHAR),
            @Result(column="space_id", property="spaceId", jdbcType= JdbcType.VARCHAR),
            @Result(column="space_template_id", property="spaceTemplateId", jdbcType= JdbcType.BIGINT)
    })
    List<ActivityRecordResp> queryByCondition(@Param("activityRecordReq") ActivityRecordReq activityRecordReq);
    
    @SelectProvider(type = ActivityRecordSqlProvider.class, method = "queryByValidityDate")
    @Results({
    	@Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
    	@Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
    	@Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
    	@Result(column="activity", property="activity", jdbcType=JdbcType.VARCHAR),
    	@Result(column="time", property="time", jdbcType=JdbcType.TIMESTAMP),
    	@Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
    	@Result(column="foreign_id", property="foreignId", jdbcType=JdbcType.VARCHAR),
    	@Result(column="del_flag", property="delFlag", jdbcType=JdbcType.BIT),
    	@Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
    	@Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT),
    	@Result(column="device_name", property="deviceName", jdbcType=JdbcType.VARCHAR),
    	@Result(column="result", property="result", jdbcType= JdbcType.INTEGER),
    	@Result(column="set_time", property="setTime", jdbcType= JdbcType.TIMESTAMP),
    	@Result(column="user_name", property="userName", jdbcType= JdbcType.VARCHAR),
    	@Result(column="space_id", property="spaceId", jdbcType= JdbcType.VARCHAR),
    	@Result(column="template_name", property="templateName", jdbcType= JdbcType.VARCHAR),
    	@Result(column="space_template_id", property="spaceTemplateId", jdbcType= JdbcType.BIGINT)
    })
    List<ActivityRecordResp> queryByValidityDate(@Param("spaceId") Long spaceId,@Param("templateId")String templateId,@Param("type")String type,@Param("startTime")Long startTime,
    		@Param("endTime") Long endTime,@Param("spaceTemplateId")Long spaceTemplateId);
    
    @Select({
			"select DISTINCT tab.templateId as templateId,tab.templateType as templateType",
			"from (",
			"select ir.id as templateId, st.template_type as templateType, ir.name as templateName, st.space_id, st.id as spaceTemplateId, st.properties",
			"from space_template st",
			"LEFT JOIN ifttt_rule ir on st.template_id=ir.template_id",
			"where st.template_type='ifttt' and st.tenant_id = #{tenantId} and ir.tenant_id = #{tenantId}",
			"UNION",
			"select s.id as templateId, st.template_type as templateType, s.scene_name as templateName, st.space_id, st.id as spaceTemplateId, st.properties",
			"from space_template st",
			"LEFT JOIN scene s on st.template_id=s.template_id",
			"where st.template_type='scene' and st.tenant_id = #{tenantId} and s.tenant_id = #{tenantId}",
			") tab",
			"where tab.space_id in ( ${spaceId} )",
			"and tab.spaceTemplateId in ( ${spaceTemplateId} )"
	})
    List<ActivityRecordResp> queryActivityRecordByConditionWithMongo(@Param("tenantId") Long  tenantId, @Param("spaceId") String spaceId, @Param("spaceTemplateId") String spaceTemplateId);
}
