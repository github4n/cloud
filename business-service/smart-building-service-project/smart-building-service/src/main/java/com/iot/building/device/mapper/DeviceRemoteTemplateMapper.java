package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.device.vo.DeviceRemoteTemplate;
import com.iot.building.device.vo.DeviceRemoteTemplatePageReq;
import com.iot.building.device.vo.DeviceRemoteTemplateSimpleResp;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteTemplateMapper extends BaseMapper<DeviceRemoteTemplate> {
    @Insert("INSERT INTO  device_remote_template( " +
            "    tenant_id  , " +
            "    name  , " +
            "    business_type_id  , " +
            "    type  , " +
            "    data_status  , " +
            "    create_by  , " +
            "    create_time  , " +
            "    update_by  , " +
            "    update_time   " +
            " ) " +
            " VALUES " +
            "  ( " +
            "   #{deviceRemoteTemplate.tenantId,jdbcType=BIGINT}, " +
            "   #{deviceRemoteTemplate.name,jdbcType=VARCHAR}, " +
            "   #{deviceRemoteTemplate.businessTypeId,jdbcType=BIGINT}, " +
            "   #{deviceRemoteTemplate.type,jdbcType=BIGINT}, " +
            "   1, " +
            "   #{deviceRemoteTemplate.createBy,jdbcType=VARCHAR}, " +
            "   now(), " +
            "   #{deviceRemoteTemplate.updateBy,jdbcType=VARCHAR}, " +
            "    now() " +
            "  ) ")
    @Options(useGeneratedKeys = true, keyColumn = "deviceRemoteTemplate.id", useCache = false)
    @SelectKey(before = false, keyProperty = "deviceRemoteTemplate.id", statementType = StatementType.PREPARED
            , statement = "SELECT LAST_INSERT_ID() AS id", resultType =
            Long.class)
    Long insertGetId(@Param("deviceRemoteTemplate") DeviceRemoteTemplate deviceRemoteTemplate);


    @Update({"<script>",
            "update device_remote_template" +
                    "	set " +
                    "	 <if test=\"deviceRemoteTemplate.tenantId != null\"> tenant_id=#{  deviceRemoteTemplate.tenantId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"deviceRemoteTemplate.type != null\"> type=#{  deviceRemoteTemplate.type,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"deviceRemoteTemplate.name != null\"> name=#{  deviceRemoteTemplate.name,jdbcType=VARCHAR},</if>" +
                    "	 <if test=\"deviceRemoteTemplate.businessTypeId != null\"> business_type_id=#{ deviceRemoteTemplate.businessTypeId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"deviceRemoteTemplate.updateBy != null\"> update_by=#{ deviceRemoteTemplate.updateBy,jdbcType=VARCHAR},</if>" +
                    "   update_time=now()," +
                    "    id = #{deviceRemoteTemplate.id,jdbcType=BIGINT}" +
                    "   where id = #{deviceRemoteTemplate.id,jdbcType=BIGINT} ",
            "</script>"})
    int updateDeviceRemoteTemplate(@Param("deviceRemoteTemplate") DeviceRemoteTemplate deviceRemoteTemplate);

    @Update("update  device_remote_template  set data_status = 0,update_by=#{userId} WHERE id =#{id}")
    void deleteDeviceRemoteTemplateById(@Param("id")Long id,@Param("userId")Long userId);

    @Select({"select * " +
            "from device_remote_template " +
            "where business_type_id = #{businessTypeId,jdbcType=BIGINT} and data_status=1"})
    DeviceRemoteTemplate findByBusinessType(@Param("businessTypeId") Long businessTypeId);

    @Select({"select * " +
            "from device_remote_template " +
            "where id = #{id,jdbcType=BIGINT} and data_status=1"})
    DeviceRemoteTemplate findById(Long id);


    @Select({"select * " +
            "from device_remote_template " +
            "where business_type_id = #{businessTypeId,jdbcType=BIGINT} and data_status=1"})
    DeviceRemoteTemplate findByBusinessTypeId(Long businessTypeId);

    @Select({"SELECT   " +
            "    drt.id id,   " +
            "    drt.create_time create_time,   " +
            "    dbt.description business_type_name,   " +
            "    drt.name name   " +
            " FROM   " +
            "    device_business_type dbt,   " +
            "    device_remote_template drt   " +
            "WHERE   " +
            "    drt.data_status = 1 and   " +
            "    drt.business_type_id=dbt.id   " +
            "    order by drt.create_by desc"})
    List<DeviceRemoteTemplateSimpleResp> findDeviceRemoteTemplatePage(Pagination pagination, DeviceRemoteTemplatePageReq pageReq);
    @Select({"<script>" +
            " select business_type_id " +
            " from device_remote_template " +
            " where data_status = 1 " +
            " <if test=\"tenantId != null\"> and tenant_id=#{tenantId}</if> " +
            " </script>"})
    List<Long> listDeviceRemoteBusinessType(Long tenantId);
}
