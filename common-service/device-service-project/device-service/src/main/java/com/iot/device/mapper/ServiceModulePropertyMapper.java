package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 模组-属性表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceModulePropertyMapper extends BaseMapper<ServiceModuleProperty> {

    @Select({
            "select a.*,b.id as propertyId,b.status as status from service_module_property a join service_to_property b on a.id=b.module_property_id",
            "where b.service_module_id=#{serviceModuleId} and a.tenant_id=#{tenantId} and isnull(a.parent_id) order by b.create_time desc"
    })
    public List<ServiceModuleProperty> listByServiceModuleId(@Param("serviceModuleId") Long serviceModuleId, @Param("tenantId") Long tenantId);


    @Select({
            "select a.*,b.id as propertyId, b.param_type as propertyParamType from service_module_property a join module_action_to_property b on a.id=b.module_property_id",
            "where b.module_action_id=#{actionModuleId} and a.tenant_id=#{tenantId} and isnull(a.parent_id) order by b.create_time desc"
    })
    public List<ServiceModuleProperty> listByActionModuleId(@Param("actionModuleId") Long actionModuleId,@Param("tenantId") Long tenantId);

    @Select({
            "select a.*,b.id as propertyId from service_module_property a join module_event_to_property b on a.id=b.event_property_id ",
            "where b.module_event_id=#{eventModuleId} and a.tenant_id=#{tenantId} and isnull(a.parent_id) order by b.create_time desc"
    })
    public List<ServiceModuleProperty> listByEventModuleId(@Param("eventModuleId") Long eventModuleId,@Param("tenantId") Long tenantId);


    /**
     * @despriction：支持ifttt的方法
     * @author  yeshiyuan
     * @created 2018/10/23 11:40
     * @return
     */
    @Select("<script>" +
            " select id,name,ifttt_type,portal_ifttt_type from service_module_property " +
            " where  ifttt_type !=0 " +
            " and id in " +
            " (" +
            "   select module_property_id from service_to_property where service_module_id in " +
            "   <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "            #{id,jdbcType=BIGINT}" +
            "   </foreach>" +
            " )" +
            "</script>")
    List<ServiceModuleProperty> supportIftttProperty(@Param("ids") List<Long> serviceModuleIds);

    @Update("update service_module_property set portal_ifttt_type = #{portalIftttType}," +
            " update_by = #{updateBy} , update_time = #{updateTime}" +
            " where id = #{id} and tenant_id = #{tenantId}")
    int updatePortalIftttType(@Param("id") Long id, @Param("tenantId") Long tenantId, @Param("portalIftttType") Integer portalIftttType,
                              @Param("updateBy") Long updateBy, @Param("updateTime")Date updateTime);
    @Select("<script>" +
            " select id, name, parent_id as parentId, code, req_param_type as reqParamType," +
            " return_type as returnType, param_type as paramType, min_value as minVal," +
            " max_value as maxVal, allowed_values as allowedValues" +
            " from service_module_property " +
            " where 1=1 " +
            " <choose>" +
            "     <when test=\"iftttType == 'if'\">" +
            "        and ifttt_type in (1,3) " +
            "     </when>" +
            "     <when test=\"iftttType == 'then'\">" +
            "        and ifttt_type in (2,3) " +
            "     </when>" +
            " </choose>" +
            " and id in " +
            " (" +
            "   select module_property_id from service_to_property where service_module_id in " +
            "   <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "            #{id,jdbcType=BIGINT}" +
            "   </foreach>" +
            " )" +

            "</script>")
    List<PropertyResp> queryDetailByModuleIdAndIfttt(@Param("ids") List<Long> serviceModuleIds, @Param("iftttType") String iftttType);

    @Select("<script>" +
            " select id, name, code, req_param_type as reqParamType,parent_id as parentId," +
            " return_type as returnType, param_type as paramType, min_value as minVal," +
            " max_value as maxVal, allowed_values as allowedValues, parent_id as parentId " +
            " from service_module_property " +
            " where " +
            " <choose>" +
            "     <when test=\"iftttType == 'if'\">" +
            "        ifttt_type in (1,3) " +
            "     </when>" +
            "     <when test=\"iftttType == 'then'\">" +
            "        ifttt_type in (2,3) " +
            "     </when>" +
            " </choose>" +
            " and id in " +
            "   <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "            #{id,jdbcType=BIGINT}" +
            "   </foreach>" +
            "</script>")
    List<PropertyResp> queryDetailByIdAndIfttt(@Param("ids") List<Long> ids, @Param("iftttType") String iftttType);
}
