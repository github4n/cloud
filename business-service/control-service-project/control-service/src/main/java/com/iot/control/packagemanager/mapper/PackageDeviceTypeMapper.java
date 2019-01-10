package com.iot.control.packagemanager.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.packagemanager.entity.PackageDeviceType;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface PackageDeviceTypeMapper extends BaseMapper<PackageDeviceType> {

    @Delete("delete from package_device_type where package_id = #{packageId}")
    int deleteByPackageId(@Param("packageId") Long packageId);

    /**
     *@description 根据套包id批量删除
     *@author wucheng
     *@params [packageIds]
     *@create 2018/12/6 14:08
     *@return int
     */
    @Delete("<script> " +
            "  delete from package_device_type " +
            "  where package_id in " +
            "  <foreach index='index' collection='packageIds' item='packageId' open='(' separator=',' close=')'>" +
            "    #{packageId} " +
            "  </foreach>" +
            "</script>")
    int batchDeleteByPackageId(@Param("packageIds") List<Long> packageIds);

    @Insert("<script>" +
            "insert into package_device_type(package_id, device_type_id, create_by, create_time)" +
            " values " +
            "<foreach collection='reqs' item='a' separator=','>" +
            " (#{a.packageId},#{a.deviceTypeId},#{a.createBy},#{a.createTime})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("reqs") List<PackageDeviceType> reqs);

    /**
     * @despriction：通过套包id找到设备类型
     * @author  yeshiyuan
     * @created 2018/11/21 10:40
     */
    @Select("select device_type_id as deviceTypeId  from package_device_type where package_id = #{packageId}")
    List<Long> getDeviceTypesByPackageId(@Param("packageId") Long packageId);

    /**
     *@description 获取套包设备详细信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 10:30
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp>
     */
    @Select("SELECT a.id as packageId,a.name as packageName, a.package_type as packageType, b.device_type_id as deviceTypeId" +
            " FROM package  a LEFT JOIN package_device_type b ON a.id = b.package_id where a.id = #{packageId} and a.tenant_id = #{tenantId}")
    List<PackageDeviceTypeInfoResp> getPackageDeviceTypeInfo(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);
}
