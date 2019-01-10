package com.iot.building.template.mapper;

import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SpaceTemplateMapper {
	
	/**
	 * 	根据 条件 获取 空间-模板 关系
	 * @return
	 */
	@Select(
		"select                                                                                                                                       "+
		"tab.id, 																      "+
		"tab.space_id as spaceId, 														      "+
		"tab.template_id as templateId, 													      "+
		"tab.location_id as locationId, 													      "+
		"tab.tenant_id as tenantId, 														      "+
		"tab.org_id as orgId, 														      "+
		"tab.template_type as templateType, 													      "+
		"tab.loop_type as loopType, 														      "+
		"tab.start_cron as startCron, 														      "+
		"tab.start_time as startTime, 														      "+
		"tab.end_time as endTime, 														      "+
		"tab.run_time as runTime, 														      "+
		"tab.business as business, 													      "+
		"tab.template_name as templateName, 													      "+
		"tab.week as week 													      "+
		"from space_template tab																	      "+
		"where tab.location_id=${locationId} and tab.org_id=#{orgId} and space_id=#{spaceId} and ((start_time BETWEEN #{startTime} AND #{endTime})  "+
		"or (end_time BETWEEN #{startTime} AND #{endTime})	  "+
		"or (start_time <= #{startTime} AND #{endTime} <= end_time)	)  "
	)
	public List<SpaceTemplateResp> findByValidityDate(SpaceTemplateReq spaceTemplateReq);
	
	@Select(
			"select                                                                                                                                       "+
			"tab.id, 																      "+
			"tab.space_id as spaceId, 														      "+
			"tab.template_id as templateId, 													      "+
			"tab.location_id as locationId, 													      "+
			"tab.tenant_id as tenantId, 												"+
		    "tab.org_id as orgId, 													      "+
			"tab.template_type as templateType, 													      "+
			"tab.loop_type as loopType, 														      "+
			"tab.start_cron as startCron, 														      "+
			"tab.start_time as startTime, 														      "+
			"tab.end_time as endTime, 														      "+
			"tab.run_time as runTime, 														      "+
			"tab.business as business, 													      "+
			"tab.template_name as templateName, 													      "+
			"tab.week as week 													      "+
			"from space_template tab																	      "+
			"where tab.location_id=${locationId} and tab.org_id=#{orgId} and space_id=#{spaceId} and ((start_time BETWEEN #{startTime} AND #{endTime})  "+
			"or (end_time BETWEEN #{startTime} AND #{endTime})	  "+
			"or (start_time >= #{startTime} AND #{endTime} >= end_time)	)  "
		)
    public List<SpaceTemplateResp> findFuture(SpaceTemplateReq spaceTemplateReq);


	/**
	 * 	根据 条件 获取 空间-模板 关系
	 * @return
	 */
	@Select(
		"<script>" +
		"select                                                                                                                                       "+
		"tab.id, 																      "+
		"tab.space_id as spaceId, 														      "+
		"tab.template_id as templateId, 													      "+
		"tab.location_id as locationId, 													      "+
		"tab.tenant_id as tenantId, 														      "+
		"tab.template_type as templateType, 													      "+
		"tab.loop_type as loopType, 														      "+
		"tab.start_cron as startCron, 														      "+
		"tab.end_cron as endCron, 														      "+
		"tab.properties as properties, 														      "+
		"tab.create_by as createBy, 														      "+
		"tab.templateName as templateName 													      "+
		"from 																	      "+
		"(																	      "+
		"select st.*, ir.name as templateName from space_template st left join ifttt_rule ir on st.template_id=ir.id where st.template_type='ifttt'   "+
		"UNION																	      "+
		"select st.*, t.name as templateName from space_template st left join template t on st.template_id=t.id where st.template_type='scene'	      "+
		") tab 																	      "+
		"where 1=1 																      "+
		"<if test=\"id != null\">														      "+
		"and tab.id = #{id} 															      "+
		"</if>																	      "+
		"<if test=\"spaceId != null\">														      "+
		"and tab.space_id = #{spaceId}	 													      "+
		"</if>																	      "+
		"<if test=\"templateId != null\">													      "+
		"and tab.template_id = #{templateId}													      "+
		"</if>																	      "+
		"<if test=\"tenantId != null\">														      "+
		"and tab.tenant_id = #{tenantId}													      "+
		"</if>																	      "+
		"<if test=\"orgId != null\">														      "+
		"and tab.org_id = #{orgId}													      "+
		"</if>																	      "+
		"<if test=\"templateType != null\">													      "+
		"and tab.template_type = #{templateType}												      "+
		"</if>																	      "+
		"<if test=\"spaceIds != null\">													      "+
		"and tab.space_id in 															      "+
		"<foreach collection=\"spaceIds\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">					      "+
		"#{item}																      "+
		"</foreach>																      "+
		"</if>	"+
		"</script>"
	)
	public List<SpaceTemplateResp> findByCondition(SpaceTemplateReq spaceTemplateReq);

	/**
	 * 	根据 条件 获取 空间-模板 关系
	 * @return
	 */
	@Select(
		"select                                                                                                                                       "+
		"tab.id, 																      "+
		"tab.space_id as spaceId, 														      "+
		"tab.template_id as templateId, 													      "+
		"tab.location_id as locationId, 													      "+
		"tab.tenant_id as tenantId, 														      "+
		"tab.org_id as orgId, 														      "+
		"tab.template_type as templateType, 													      "+
		"tab.loop_type as loopType, 														      "+
		"tab.start_cron as startCron, 														      "+
		"tab.end_cron as endCron, 														      "+
		"tab.properties as properties, 														      "+
		"tab.create_by as createBy 														      "+
		"from space_template tab																	      "+
		"where tab.id = #{id}" 															     
	)
	public SpaceTemplateResp findById(SpaceTemplateReq spaceTemplateReq);

	/**
	 * 	根据 条件 获取 模板IdList
	 * @param spaceTemplateReq
	 * @return
	 */
	@Select(
		"<script>" +
		"select "  +
		"template_id as templateId "+
		"from space_template t "+
		"where 1=1 "+
		"<if test=\"id != null\">"+
		"and t.id = #{id}"+
		"</if>"+
		"<if test=\"spaceId != null\">"+
		"and t.space_id = #{spaceId}"+
		"</if>"+
		"<if test=\"templateId != null\">"+
		"and t.template_id = #{templateId}"+
		"</if>"+
		"<if test=\"tenantId != null\">"+
		"and t.tenant_id = #{tenantId}"+
		"</if>"+
		"<if test=\"orgId != null\">"+
		"and t.org_id = #{orgId}"+
		"</if>"+
		"<if test=\"templateType != null\">"+
		"and t.template_type = #{templateType}"+
		"</if> "+ 
		"</script>"
	)
	public List<Long> findTemplateIdListByCondition(SpaceTemplateReq spaceTemplateReq);

	/**
	 * 	新增空间-模板
	 *
	 * @return
	 */
	@Insert({
			"insert into space_template (",
			"template_id,",
			"location_id,",
			"space_id,",
			"tenant_id,",
			"org_id,",
			"template_type,",
			"loop_type,",
			"start_cron,",
			"end_cron,",
			"properties,",
			"create_by,",
			"create_time,",
			"start_time,",
			"end_time,",
			"run_time,",
			"business,",
			"template_name,",
			"week",
			")",
			"values (",
			"#{templateId}, ",
			"#{locationId},",
			"#{spaceId},",
			"#{tenantId},",
			"#{orgId},",
			"#{templateType},",
			"#{loopType},",
			"#{startCron},",
			"#{endCron},",
			"#{properties},",
			"#{createBy},",
			"NOW(),",
			"#{startTime},",
			"#{endTime},",
			"#{runTime},",
			"#{business},",
			"#{templateName},",
			"#{week}",
			")"
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void spaceTemplateSave(SpaceTemplateReq spaceTemplateReq);

	/**
	 * 	删除空间-模板关系
	 *
	 * @return
	 */
	@Delete("<script>" +
			"delete from space_template "+
			"where 1=1 "+ 
			"<if test=\"id != null\">"+
			"and id = #{id}"+
			"</if>"+
			"<if test=\"spaceId != null\">"+
			"and space_id = #{spaceId}"+
			"</if>"+
			"<if test=\"templateId != null\">"+
			"and template_id = #{templateId}"+
			"</if>"+
			"<if test=\"templateType != null\">"+
			"and template_type = #{templateType}"+
			"</if>"+
			"</script>"
	)
	int spaceTemplateDelete(SpaceTemplateReq spaceTemplateReq);
	
	/**
	 * 	修改模板ID
	 *
	 * @return
	 */
	@Update({
		"<script>" +
			"update space_template "+
				"<set>	  "+ 
					"<if test=\"spaceId != null\">"+
						"space_id = #{spaceId},"+
					"</if>"+
					"<if test=\"templateId != null\">"+
						" template_id = #{templateId},"+
					"</if>"+
					"<if test=\"templateType != null\">"+
						" template_type = #{templateType},"+
					"</if>"+
					"<if test=\"loopType != null\">"+
						" loop_type = #{loopType},"+
					"</if>"+
					"<if test=\"startCron != null\">"+
						" start_cron = #{startCron},"+
					"</if>"+
					"<if test=\"endCron != null\">"+
						" end_cron = #{endCron},"+
					"</if>"+
					"<if test=\"properties != null\">"+
						" properties = #{properties},"+
					"</if>"+
					"<if test=\"startTime != null\">"+
					" start_time = #{startTime},"+
					"</if>"+
					"<if test=\"endTime != null\">"+
					" end_time = #{endTime},"+
					"</if>"+
					"<if test=\"business != null\">"+
					" business = #{business},"+
					"</if>"+
					"<if test=\"runTime != null\">"+
					" run_time = #{runTime},"+
					"</if>"+
					"<if test=\"templateName != null\">"+
					" template_name = #{templateName},"+
					"</if>"+
					"<if test=\"week != null\">"+
					" week = #{week},"+
					"</if>"+
				"</set>	"+
			 "where id = #{id}"		+
		"</script>"
    })
    int spaceTemplateUpdateTemplateId(SpaceTemplateReq spaceTemplateReq);

	/**
	 * 	根据 条件 获取 模板IdList
	 * @param spaceTemplateReq
	 * @return
	 */
	@Select(
		"SELECT "  +
		"tab.id, 																      "+
		"tab.space_id as spaceId, 														      "+
		"tab.template_id as templateId, 													      "+
		"tab.location_id as locationId, 													      "+
		"tab.tenant_id as tenantId, 														      "+
		"tab.template_type as templateType, 													      "+
		"tab.loop_type as loopType, 														      "+
		"tab.start_cron as startCron, 														      "+
		"tab.start_time as startTime, 														      "+
		"tab.end_time as endTime, 														      "+
		"tab.run_time as runTime, 														      "+
		"tab.business as business, 													      "+
		"tab.template_name as templateName, 		"+											     
		"tab.week as week 	"+							
		"from space_template tab,activity_record ad "+
		"WHERE tab.id=ad.space_template_id AND tab.week LIKE CONCAT('%',#{week},'%') "+
		"AND tab.space_id=#{spaceId} "+
		"AND ad.set_time <= #{startTime} ORDER BY ad.set_time DESC limit 1"
	)
	public SpaceTemplateResp findHistoryByCondition(SpaceTemplateReq spaceTemplateReq);
}





