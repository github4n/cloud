package com.iot.control.activity.mapper;

import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ActivityRecordMapper {
//	"select ls.id as templateId, st.template_type as templateType, ls.name as templateName, st.space_id, st.id as spaceTemplateId, st.properties",
//	"from space_template st",
//	"left join location_scene ls on st.template_id=ls.id",
//	"where st.template_type='location' and st.tenant_id = #{tenantId} and ls.tenant_id = #{tenantId}",
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
	List<ActivityRecordResp> queryActivityRecordByConditionWithMongo(@Param("tenantId") Long tenantId, @Param("spaceId") String spaceId, @Param("spaceTemplateId") String spaceTemplateId);
}
