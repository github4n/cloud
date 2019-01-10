package com.iot.building.template.mapper;

import com.iot.building.template.domain.TemplateDetail;
import com.iot.building.template.vo.rsp.DeviceTarValueResp;
import com.iot.building.template.vo.rsp.TemplateDetailResp;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface TemplateDetailMapper {

	/**
	 * 	插入模板详情
	 *
	 * @param templateDetail
	 * @return
	 */
	@Insert({
			"insert into template_detail (",
			"template_id,",
			"product_id,",
			//"device_category_id,",
			"device_type_id,",
			"target_value,",
			"create_time,",
			"update_time,",
			"create_by,",
			"update_by,",
			"business_type_id,",
			"tenant_id)",
			"values (#{templateId}, ",
			"#{productId},",
			//"#{deviceCategoryId},",
			"#{deviceTypeId},",
			"#{targetValue},",
			"#{createTime},",
			"#{updateTime},",
			"#{createBy},",
			"#{updateBy},",
			"#{businessTypeId},",
			"#{tenantId})"
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insert(TemplateDetail templateDetail);

	/**
	 * 	根据 templateId 删除模板详情
	 *
	 * @param templateId
	 */
	@Delete({
			"delete from template_detail",
			"where template_id = #{templateId}"
	})
	public void delTemplateDetail(@Param("templateId") Long templateId);

	/**
	 * 	根据 templateId获取TemplateDetail列表
	 *
	 * @param templateId
	 * @return
	 */
	@Select({
			"select",
			"id, ",
			"template_id as templateId, ",
			"product_id as productId, ",
			"device_type_id as deviceTypeId, ",
			"target_value as targetValue, ",
			"create_time as creatTime, ",
			"update_time as updateTime, ",
			"create_by as createBy, ",
			"update_by as updateBy, ",
			"tenant_id as tenantId, ",
			"business_type_id as businessTypeId ",
			"from template_detail",
			"where template_id = #{templateId} and tenant_id = #{tenantId}"
	})
	public List<TemplateDetail> findByTemplateId(@Param("templateId") Long templateId, @Param("tenantId") Long tenantId);

	/**
	 * 	根据 templateId、productId获取TemplateDetail
	 *
	 * @param templateId
	 * @param productId
	 * @return
	 */
	@Select({
			"select",
			"*",
			"from template_detail",
			"where template_id = #{templateId} and product_id = #{productId} and tenant_id = #{tenantId}"
	})
	public TemplateDetail getByTemplateIdAndProductId(@Param("templateId") Long templateId, @Param("productId") Long productId, @Param("tenantId") Long tenantId);

	/**
	 * 	查询房间中是否绑定模板
	 *
	 * @param templateId
	 * @return
	 */
	@Select({
			"select",
			"s.`name` ",
			"from space_template st ",
			"JOIN space s ON st.space_id = s.id ",
			"where st.template_id = #{templateId} ",
			"and st.template_type = #{templateType} "
	})
	public List<String> getRoomByTemplateId(@Param("templateId") Long templateId, @Param("templateType") String templateType);

	/**
	 *  获取 模板详情目标值
	 *
	 * @param templateId
	 * @return
	 */
	@Select({
			"select",
			"t.device_type_id AS deviceTypeId,",
			"t.target_value AS targetValue",
			"from template_detail t",
			"where t.template_id = #{templateId}"
	})
	public List<DeviceTarValueResp> findDeviceTargetValueList(@Param("templateId") Long templateId);

	/**
	 * 	根据 templateId 获取模板详情列表
	 *
	 * @param templateId
	 * @return
	 */
	@Select({
			"select",
			"t.id ,",
			"t.template_id AS templateId ,",
			"t.product_id AS productId,",
			"t.device_type_id AS deviceTypeId,",
			"t.business_type_id AS businessTypeId,",
			"t.target_value AS targetValue",
			"from template_detail t",
			"where t.template_id = #{templateId}"
	})
	public List<TemplateDetailResp> findTemplateDetailList(@Param("templateId") Long templateId);
}
