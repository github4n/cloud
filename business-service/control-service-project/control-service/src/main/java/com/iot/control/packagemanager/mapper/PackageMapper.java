package com.iot.control.packagemanager.mapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *@description 套包mapper
 *@author wucheng
 *@create 2018/11/21 16:58
 *@return
 */
@Mapper
public interface PackageMapper{
    @Select("SELECT id, name, product_id as productId, tenant_id as tenantId, create_by as createBy, " +
            " update_by as updateBy, create_time as createTime, update_time as updateTime, icon, " +
            " description,package_id as packageId, package_type as packageType from package where id = #{id} and tenant_id = #{tenantId}")
    PackageResp selectById(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert("insert into package(name, product_id, tenant_id, create_by, " +
            " update_by, create_time, update_time, icon, description, package_id, package_type) values (" +
            " #{req.name}, #{req.productId}, #{req.tenantId}, #{req.createBy}, " +
            " #{req.updateBy}, #{req.createTime}, #{req.updateTime, jdbcType=TIMESTAMP}, #{req.icon}, " +
            " #{req.description}, #{req.packageId}, #{req.packageType})")
    @Options(useGeneratedKeys=true, keyProperty="req.id", keyColumn="id")
    Long addPackage(@Param("req") PackageReq req);

    @Select("SELECT id, name, product_id as productId, tenant_id as tenantId, create_by as createBy, " +
            " update_by as updateBy, create_time as createTime, update_time as updateTime, icon, " +
            " description, package_id as packageId, package_type as packageType from package where 1=1 ${ew.sqlSegment}")
    List<PackageResp> getPagePackage(@Param("page") Page<PackageReq> page, @Param("ew") EntityWrapper ew);

    @Update("<script>" +
            " update package " +
            " <set>" +
            " <if test=\"req.name != null and req.name != ''\"> name = #{req.name},</if>" +
            " <if test=\"req.icon != null and req.icon != ''\"> icon = #{req.icon},</if>" +
            " <if test='req.packageType != null'> package_type = #{req.packageType},</if>" +
            " <if test='req.packageId != null'> package_id = #{req.packageId},</if>" +
            " <if test='req.productId != null'> product_id = #{req.productId},</if>" +
            " <if test='req.tenantId != null'> tenant_id = #{req.tenantId},</if>" +
            " <if test='req.updateBy != null'> update_by = #{req.updateBy},</if>" +
            " <if test='req.updateTime != null'> update_time = #{req.updateTime},</if>" +
            " <if test=\"req.description != null and req.description != ''\"> description = #{req.description},</if>" +
            " </set>" +
            " where id = #{req.id}" +
            "</script>")
    int updatePackageById(@Param("req") PackageReq req);

    @Delete("<script> " +
            " delete from package where id in " +
            " <foreach  collection='ids' index='index' item='id' open='(' separator=',' close=')'>" +
            "    #{id}" +
            " </foreach>" +
            "</script>")
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     *@description 根据租户id，获取该租户套包基本信息
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/13 15:39
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageResp>
     */
    @Select("SELECT id as packageId, name as packageName, icon, description, package_type as packageType from package where  tenant_id = #{tenantId}")
    List<PackageBasicResp> getPackageInfo(@Param("tenantId") Long tenantId);

}
