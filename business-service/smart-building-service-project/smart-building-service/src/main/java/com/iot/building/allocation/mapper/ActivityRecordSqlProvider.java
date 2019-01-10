package com.iot.building.allocation.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import com.iot.building.allocation.vo.ActivityRecordReq;


/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/17 17:35
 * 修改人:
 * 修改时间：
 */
public class ActivityRecordSqlProvider {

	public String deleteByCondition(@Param("activityRecordReq") ActivityRecordReq activityRecordReq) {
		BEGIN();
		UPDATE("activity_record");

		SET("del_flag=1");

		if(StringUtils.isNotBlank(activityRecordReq.getType())){
			WHERE("type=#{activityRecordReq.type}");
		}

		if(StringUtils.isNotBlank(activityRecordReq.getForeignId())){
			WHERE("foreign_id=#{activityRecordReq.foreignId}");
		}

		if(activityRecordReq.getCreateBy() != null){
			WHERE("create_by=#{activityRecordReq.createBy}");
		}

		return SQL();
	}

	public String queryByCondition(@Param("activityRecordReq") ActivityRecordReq activityRecordReq) {
		BEGIN();

		SELECT("id, type, icon, activity, time, create_by, foreign_id, del_flag, device_name, result,user_name,set_time");

		FROM("activity_record");

		if(activityRecordReq.getDelFlag() != null){
			WHERE("del_flag=#{activityRecordReq.delFlag}");
		}
		
		if(activityRecordReq.getTime() != null){
			WHERE("time >= #{activityRecordReq.time}");
		}

		if(activityRecordReq.getCreateBy() != null){
			WHERE("create_by=#{activityRecordReq.createBy}");
		}

		if(StringUtils.isNotBlank(activityRecordReq.getForeignId())){
			WHERE("foreign_id=#{activityRecordReq.foreignId}");
		}

		if(StringUtils.isNotBlank(activityRecordReq.getType())){
			WHERE("type=#{activityRecordReq.type}");
		}
		
		if(activityRecordReq.getLocationId() !=null){
			WHERE("location_id=#{activityRecordReq.locationId}");
		}
		if(activityRecordReq.getLocationId() !=null){
			WHERE("space_id=#{activityRecordReq.spaceId}");
		}
		if(activityRecordReq.getSpaceTemplateId() !=null){
			WHERE("space_template_id=#{activityRecordReq.spaceTemplateId}");
		}
		ORDER_BY("time desc");

		return SQL();
	}
	
	@SuppressWarnings("deprecation")
	public String queryByValidityDate(@Param("spaceId") Long spaceId,@Param("templateId") String templateId,
			@Param("type")String type,@Param("startTime") Long startTime,@Param("endTime") Long endTime,@Param("spaceTemplateId")Long spaceTemplateId) {
		BEGIN();
		
		SELECT("id, type, icon, activity, time, create_by, foreign_id, del_flag, device_name, result,user_name,set_time,template_name,space_id");
		
		FROM("activity_record");
		
		WHERE(" foreign_id=#{templateId} ");
		
		WHERE(" space_id=#{spaceId} ");
		
		if(spaceTemplateId != null) {
			WHERE(" space_template_id=#{spaceTemplateId} ");
		}
		
		if(startTime !=null && endTime !=null) {
			WHERE("set_time BETWEEN #{startTime} AND #{endTime}");
		}
		
		WHERE(" type =#{type} ");
		
		ORDER_BY("set_time desc");
		
		return SQL();
	}
}
