package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 模组-事件表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceModuleEventMapper extends BaseMapper<ServiceModuleEvent> {


    @Select({
            "select a.*,b.id as eventId,b.status as status from service_module_event a join service_to_event b on a.id=b.module_event_id",
            "where b.service_module_id=#{serviceModuleId} and a.tenant_id=#{tenantId} and isnull(a.parent_id) order by b.create_time desc"
    })
    public List<ServiceModuleEvent> listByServiceModuleId(@Param("serviceModuleId") Long serviceModuleId, @Param("tenantId") Long tenantId);

    /**
     * @despriction：支持ifttt的事件
     * @author  yeshiyuan
     * @created 2018/10/23 11:40
     * @return
     */
    @Select("<script>" +
            " select id,name,ifttt_type,portal_ifttt_type from service_module_event " +
            " where ifttt_type !=0 " +
            " and id in " +
            " (" +
            "   select module_event_id from service_to_event where service_module_id in " +
            "   <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "            #{id,jdbcType=BIGINT}" +
            "   </foreach>" +
            " )" +
            "</script>")
    List<ServiceModuleEvent> supportIftttEvent(@Param("ids") List<Long> serviceModuleIds);

    @Update("update service_module_event set portal_ifttt_type = #{portalIftttType}," +
            " update_by = #{updateBy} , update_time = #{updateTime}" +
            " where id = #{id} and tenant_id = #{tenantId}")
    int updatePortalIftttType(@Param("id") Long id, @Param("tenantId") Long tenantId, @Param("portalIftttType") Integer portalIftttType,
                              @Param("updateBy") Long updateBy, @Param("updateTime")Date updateTime);


    @Select("<script>" +
            " select id, name, code, params,parent_id as parentId " +
            " from service_module_event " +
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
            "   select module_event_id from service_to_event where service_module_id in " +
            "   <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "            #{id,jdbcType=BIGINT}" +
            "   </foreach>" +
            " )" +
            "</script>")
    List<EventResp> queryDetailByModuleIdAndIfttt(@Param("ids") List<Long> serviceModuleIds, @Param("iftttType") String iftttType);
}
