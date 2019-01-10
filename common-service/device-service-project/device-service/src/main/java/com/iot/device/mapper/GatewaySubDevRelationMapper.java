package com.iot.device.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.GatewaySubDevRelation;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface GatewaySubDevRelationMapper extends BaseMapper<GatewaySubDevRelation> {
   @Insert({
           "insert into gateway_subdev_relation(tenant_id, parDev_id, subDev_id, create_by, create_time, update_by, update_time,is_deleted)" +
                   "values(#{gatewaySubDevRelationReq.tenantId}, #{gatewaySubDevRelationReq.parDevId}," +
                   "#{gatewaySubDevRelationReq.subDevId},  #{gatewaySubDevRelationReq.createBy}," +
                   "#{gatewaySubDevRelationReq.createTime}, #{gatewaySubDevRelationReq.updateBy}," +
                   "#{gatewaySubDevRelationReq.updateTime}, #{gatewaySubDevRelationReq.isDeleted})"
           })
   int insert(@Param("gatewaySubDevRelationReq") GatewaySubDevRelationReq gatewaySubDevRelationReq);

   @Delete("<script>"+
           "delete from gateway_subdev_relation where id in"+
           "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" +
           "#{id}"+
           "</foreach>"+
           "</script>")
   int deleteById(@Param("ids") List<Long> ids);

   @Select({"SELECT * FROM gateway_subdev_relation where parDev_id=#{parDevId} and tenant_id = #{tenantId}"})
   @Results({
     @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
     @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
     @Result(column="parDev_id", property="parDevId", jdbcType=JdbcType.BIGINT),
     @Result(column="subDev_id", property="subDevId", jdbcType=JdbcType.BIGINT),
     @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
     @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
     @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
     @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
     @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
     @Result(column="is_deleted", property="isDeleted", jdbcType=JdbcType.CHAR)
   })
   List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(@Param("parDevId") Long parDevId,@Param("tenantId") Long tenantId);

   @Select("select pardev_id from gateway_subdev_relation where subdev_id=#{subDevId} and tenant_id = #{tenantId}")
   List<Long> parentProductIds(@Param("subDevId") Long subDevId, @Param("tenantId") Long tenantId);
}
