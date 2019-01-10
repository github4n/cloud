package com.iot.tenant.mapper;

import com.iot.tenant.entity.DeviceNetworkStepTenant;
import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：device_network_step_tenant sql语句
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:51
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:51
 * 修改描述：
 */
@Mapper
public interface DeviceNetworkStepTenantMapper {

    @Insert("<script>" +
            " insert into device_network_step_tenant(tenant_id,app_id,product_id,network_type_id,step,icon,create_time,create_by,is_help)" +
            " select #{req.tenantId,jdbcType=BIGINT},#{req.appId,jdbcType=BIGINT},#{req.productId,jdbcType=BIGINT}, " +
            " network_type_id, step, CONCAT('default_',icon), #{createTime},#{req.userId},is_help from device_network_step_base " +
            " where device_type_id = #{req.deviceTypeId} and data_status = 'valid'" +
            " and network_type_id in " +
            " <foreach collection='req.networkTypeIds' item='networkTypeId' open='(' close=')' separator=','>" +
            "  #{networkTypeId,jdbcType=BIGINT}" +
            "</foreach>" +
            "</script>")
    int copyNetworkStep(@Param("req") CopyNetworkStepReq req, @Param("createTime")Date createTime);

    @Insert("<script>" +
            " insert into device_network_step_tenant(tenant_id,app_id,product_id,network_type_id,step,icon,create_time,create_by,is_help)" +
            " select tenant_id, app_id, product_id, " +
            " network_type_id, step, CONCAT('default_',icon), #{createTime},#{req.userId},is_help from device_network_step_tenant " +
            " where app_id = #{req.oldAppId} and tenant_id = #{req.tenantId,jdbcType=BIGINT} and product_id = #{req.productId,jdbcType=BIGINT}" +
            "</script>")
    int copyNetworkStepTenant(@Param("req") CopyNetworkStepReq req, @Param("createTime")Date createTime, @Param("oldAppId") Long oldAppId);

    @Delete("delete from device_network_step_tenant where tenant_id = #{tenantId,jdbcType=BIGINT} and app_id = #{appId,jdbcType=BIGINT} " +
            "and product_id = #{productId,jdbcType=BIGINT}")
    int deleteByTenantAndAppAndProduct(@Param("tenantId") Long tenantId, @Param("appId") Long appId, @Param("productId") Long productId);

    @Insert("<script>" +
            "insert into device_network_step_tenant (tenant_id,app_id,product_id,network_type_id,step,icon,create_time,create_by,is_help)" +
            "values " +
            " <foreach collection='list' item='item' separator=','>" +
            "  (" +
            "    #{item.tenantId,jdbcType=BIGINT}," +
            "    #{item.appId,jdbcType=BIGINT}," +
            "    #{item.productId,jdbcType=BIGINT}," +
            "    #{item.networkTypeId}," +
            "    #{item.step,jdbcType=INTEGER}," +
            "    #{item.icon}," +
            "    #{item.createTime}," +
            "    #{item.createBy}," +
            "    #{item.isHelp}" +
            "  )" +
            " </foreach>" +
            "</script>")
    int batchInsert(@Param("list")List<DeviceNetworkStepTenant> list);

    @Select("select network_type_id as networkTypeId,step,icon,is_help as isHelp " +
            " from device_network_step_tenant where tenant_id = #{tenantId,jdbcType=BIGINT} and app_id = #{appId,jdbcType=BIGINT}" +
            " and product_id = #{productId,jdbcType=BIGINT} " +
            " order by network_type_id, step asc")
    List<DeviceNetworkStepTenant> findByTenantIdAndAppIdAndProductId(@Param("tenantId") Long tenantId, @Param("appId") Long appId, @Param("productId") Long productId);

    @Select("select count(1) " +
            " from device_network_step_tenant where tenant_id = #{tenantId,jdbcType=BIGINT} and app_id = #{appId,jdbcType=BIGINT}" +
            " and product_id = #{productId,jdbcType=BIGINT} " )
    int checkExist(@Param("tenantId") Long tenantId, @Param("appId") Long appId, @Param("productId") Long productId);

    @Delete("delete from device_network_step_tenant where tenant_id = #{tenantId,jdbcType=BIGINT} and app_id = #{appId,jdbcType=BIGINT} ")
    int deleteAppNetworkData(@Param("tenantId") Long tenantId, @Param("appId") Long appId);
}
