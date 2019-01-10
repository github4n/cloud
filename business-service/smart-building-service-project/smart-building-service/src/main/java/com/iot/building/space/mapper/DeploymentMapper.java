package com.iot.building.space.mapper;

import java.util.List;

import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;

public interface DeploymentMapper {

	@Select("<script>"+
			"select                                                "+
			"d.id as id,						       "+
			"d.name as name,						       "+
			"d.type as type,					       "+
			"d.tenant_id as tenantId,					       "+
			"d.org_id as orgId,					       "+
			"d.create_time as createTime,					       "+
			"d.update_time as updateTime,						       "+
			"d.create_by as createBy,					       "+
			"d.update_by as updateBy,					       "+
			"d.start_time as startTime,					       "+
			"d.end_time as endTime,					       "+
			"d.location_id as locationId					       "+
			"from build_calendar d 					       "+
			"WHERE d.org_id=#{orgId} and d.tenant_id =#{tenantId} and d.location_id=#{locationId}  "+
			"<if test=\"name != null and name !=''\">	       "+
			"and d.name like CONCAT(CONCAT('%', #{name}), '%')   "+
			"</if>		"+
			"order by CONVERT (d.name USING gbk) COLLATE gbk_chinese_ci ASC			       "+
			"</script>				       "
	)
	List<CalendarResp> findCalendarList(CalendarResp calendarResp) throws Exception;
	
	@Select(
			"select                                                "+
			"count(d.id) from 	build_calendar	d			       "+
			"WHERE  d.org_id=#{orgId} and d.tenant_id =#{tenantId} "+
			"  AND #{currentTime} between d.start_time and d.end_time AND type=#{type} "+
			"  AND d.location_id=#{locationId} "
			)
	Integer countExistCalendar(@Param("currentTime")Long currentTime,@Param("type")int type,
			@Param("locationId")Long locationId,@Param("orgId")Long orgId,@Param("tenantId")Long tenantId);

	@Delete({
			"delete from build_calendar",
			"where id = #{id} and tenant_id = #{tenantId} and org_id=#{orgId} "
	})
	public int deleteCalendar(@Param("tenantId") Long tenantId,@Param("orgId") Long orgId,@Param("id") Long id);


	@Update({
			"<script> " +
					"update build_calendar " +
					"		<set> " +
					"			<if test=\"req.name != null\"> " +
					"				name = #{req.name}, " +
					"			</if> " +
					"			<if test=\"req.type != null\"> " +
					"				type = #{req.type}, " +
					"			</if> " +
					"			<if test=\"req.tenantId != null\"> " +
					"				tenant_id = #{req.tenantId}, " +
					"			</if> " +
					"			<if test=\"req.updateBy != null\"> " +
					"				update_by = #{req.updateBy}, " +
					"			</if> " +
					"			<if test=\"req.updateTime != null\"> " +
					"				update_time = #{req.updateTime}, " +
					"			</if> " +
					"			<if test=\"req.startTime != null\"> " +
					"				start_time = #{req.startTime}, " +
					"			</if> " +
					"			<if test=\"req.endTime != null\"> " +
					"				end_time = #{req.endTime}, " +
					"			</if> " +
					"			<if test=\"req.locationId != null\"> " +
					"				location_id = #{req.locationId}, " +
					"			</if> " +
					"		</set> " +
					"		where id = #{req.id}" +
					"</script>                                                        "
	})
	int updateCalendar(@Param("req") CalendarReq req);


	@Insert({
			"insert into build_calendar (" ,
			"				id, " ,
			"				name, " ,
			"				type, " ,
			"				tenant_id, " ,
			"				create_time, " ,
			"				update_time, " ,
			"				create_by, " ,
			"				update_by, " ,
			"				start_time, " ,
			"				end_time, " ,
			"				location_id,org_id) values(" ,
			"				#{req.id}, " ,
			"				#{req.name}, " ,
			"				#{req.type}, " ,
			"				#{req.tenantId}, " ,
			"				#{req.createTime}, " ,
			"				#{req.updateTime}, " ,
			"				#{req.createBy}, " ,
			"				#{req.updateBy}, " ,
			"				#{req.startTime}, " ,
			"				#{req.endTime}, " ,
			"				#{req.locationId},",
			"				#{req.orgId} )"
	})
	int addCalendar(@Param("req") CalendarReq req);

	
    @Select("<script>"+
    		"select id,deploy_name as deployName ,location_id as locationId , "
    		+ "tenant_id as tenantId,create_by as createBy,create_time as createTime ,"
    		+ "update_by as updateBy ,update_time as updateTime from deployment where  "
    		+ "tenant_id=#{req.tenantId} and org_id=#{req.orgId} "
    		+"<if test=\"req.locationId != null and req.locationId !=''\">"
    		+" and location_id =#{req.locationId}   "
			+"</if>		"
			+"<if test=\"req.deployName != null and req.deployName !=''\">"
			+" and deploy_name like CONCAT(CONCAT('%', #{req.deployName}), '%')   "
			+"</if>		"
			+" order by CONVERT (deploy_name USING gbk) COLLATE gbk_chinese_ci ASC"
    		+"</script>")
    List<DeploymentResp> getDeploymentList(@Param("req") DeploymentReq req);
    
    @Select(
    		"select id,deploy_name as deployName ,location_id as locationId , "
    		+ "create_by as createBy,create_time as createTime ,"
    		+ "update_by as updateBy ,update_time as updateTime ," +
    		" tenant_id as tenantId,org_id as orgId from deployment where id=#{id}"
    		)
    DeploymentResp findById(Long id);
    
    @Select(
    		"select id,deploy_name as deployName ,location_id as locationId , "
    				+ "create_by as createBy,create_time as createTime ,"
    				+ "update_by as updateBy ,update_time as updateTime ," +
    				" tenant_id as tenantId from deployment where deploy_name=#{name} and org_id=#{orgId}"
    		)
    DeploymentResp findByName(@Param("orgId") Long orgId,@Param("name")String name);
    
    @Insert({
        "insert into deployment (" ,
        "				id, " ,
        "				deploy_name, " ,
        "				location_id, " ,
        "				create_by, " ,
        "				create_time, " ,
        "				update_by, " ,
        "				update_time, " ,
        "				tenant_id,org_id) values(" ,
        "				#{req.id}, " ,
        "				#{req.deployName}, " ,
        "				#{req.locationId}, " ,
        "				#{req.createBy}, " ,
        "				#{req.createTime}, " ,
        "				#{req.updateBy}, " ,
        "				#{req.updateTime}, " ,
        "				#{req.tenantId},#{req.locationId} )" 
    })
    int save(@Param("req") DeploymentReq req);
    
    @Update({
        "<script> " +
                "update deployment " +
                "		<set> " +
                "			<if test=\"req.deployName != null\"> " +
                "				deploy_name = #{req.deployName}, " +
                "			</if> " +
                "			<if test=\"req.locationId != null\"> " +
                "				location_id = #{req.locationId}, " +
                "			</if> " +
                "			<if test=\"req.updateBy != null\"> " +
                "				update_by = #{req.updateBy}, " +
                "			</if> " +
                "			<if test=\"req.updateTime != null\"> " +
                "				update_time = #{req.updateTime}, " +
                "			</if> " +
                "			<if test=\"req.tenantId != null\"> " +
                "				tenant_id = #{req.tenantId}, " +
                "			</if> " +
                "		</set> " +
                "		where id = #{req.id}" +
                "</script>                                                        "
	})
	int update(@Param("req") DeploymentReq req);
    
    @Delete({
		"delete from deployment",
		"where id = #{req.id}"
	})
	public int delDeploymentById(@Param("req") DeploymentReq req);


	@Select("<script>"+
			"select                                                "+
			"d.id as id,						       "+
			"d.name as name,						       "+
			"d.type as type,					       "+
			"d.tenant_id as tenantId,					       "+
			"d.create_time as createTime,					       "+
			"d.update_time as updateTime,						       "+
			"d.create_by as createBy,					       "+
			"d.update_by as updateBy,					       "+
			"d.start_time as startTime,					       "+
			"d.end_time as endTime,					       "+
			"d.location_id as locationId,					       "+
			"d.org_id as orgId					       "+
			"from build_calendar d 					       "+
			"WHERE 1=1      "+
			"and d.id = #{id}   "+
			"order by d.create_time	desc			       "+
			"</script>				       "
	)
	CalendarResp getCalendarIofoById(Long id);

	@Select("<script>"+
			"select                                                "+
			"d.id as id,						       "+
			"d.name as name,						       "+
			"d.type as type,					       "+
			"d.tenant_id as tenantId,					       "+
			"d.create_time as createTime,					       "+
			"d.update_time as updateTime,						       "+
			"d.create_by as createBy,					       "+
			"d.update_by as updateBy,					       "+
			"d.start_time as startTime,					       "+
			"d.end_time as endTime,					       "+
			"d.location_id as locationId					       "+
			"from build_calendar d 					       "+
			"WHERE d.tenant_id=#{tenantId} and d.org_id=#{orgId} and d.location_id=#{locationId}   "+
			"<if test=\"name != null and name !=''\">	       "+
			"and d.name = #{name}   "+
			"</if>		"+
			"order by CONVERT (d.name USING gbk) COLLATE gbk_chinese_ci ASC			       "+
			"</script>				       "
	)
	List<CalendarResp> findSameName(CalendarResp calendarResp);
}