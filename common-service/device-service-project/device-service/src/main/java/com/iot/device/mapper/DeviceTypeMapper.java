package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.model.DeviceCatalog;
import com.iot.device.model.DeviceType;
import com.iot.device.model.SmartDeviceType;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    @Select(
            value = "SELECT "
                    + " a.id, a.name, a.device_catalog_id, a.tenant_id, a.update_time,"
                    + " a.create_by, a.is_deleted, a.vender_flag, a.type, a.description,"
                    + " a.img, a.whether_soc,a.ifttt_type, b.name as deviceCatalogName"
                    + " FROM "
                    + " " + DeviceType.TABLE_NAME + " a "
                    + " LEFT JOIN " + DeviceCatalog.TABLE_NAME + " b ON a.device_catalog_id = b.id "
                    + " where 1 = 1 "
                    + " ${ew.sqlSegment} "
    )
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
//            @Result(property = "deviceCatalogName", column = "device_catalog_name"),
            @Result(property = "deviceCatalogId", column = "device_catalog_id"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "tenantId", column = "tenant_id"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "updateBy", column = "update_by"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "venderFlag", column = "vender_flag"),
            @Result(property = "type", column = "type"),
            @Result(property = "description", column = "description"),
            @Result(property = "img", column = "img"),
            @Result(property = "whetherSoc", column = "whether_soc"),
            @Result(property = "iftttType", column = "ifttt_type"),
            @Result(property = "smart", column = "id",javaType=List.class,
                    many = @Many(select = "com.iot.device.mapper.DeviceTypeMapper.selectSmartType"))
    })
    List<DeviceType> selectDeviceTypePage(@Param("page") Page<DeviceType> page, @Param("ew") EntityWrapper ew);

    @Select({
            "SELECT                                                     " +
                    " * " +
                    " FROM " +
                    " " + SmartDeviceType.TABLE_NAME +
                    " WHERE device_type_id = #{deviceTypeId}    "
    })
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "code", column = "smart_type"),
            @Result(property = "smart", column = "smart")
    })
    List<DataPointReq.SmartWraper> selectSmartType(@Param("deviceTypeId") Long deviceTypeId);

    @Update("update device_type set network_type=#{networkType} where id = #{deviceTypeId}")
    int updateNetworkType(@Param("deviceTypeId") Long deviceTypeId, @Param("networkType") String networkType);

    @Select("select network_type from device_type where id = #{deviceTypeId}")
    String getNetworkType(@Param("deviceTypeId") Long deviceTypeId);

    @Select("<script>"
            + "SELECT "
            + " a.id, a.name, a.device_catalog_id as deviceCatalogId,a.img, " +
            "   a.tenant_id as tenantId, a.description, "
            + " a.create_by, a.ifttt_type as iftttType, b.name as deviceCatalogName "
            + " FROM device_type a "
            + " LEFT JOIN device_catalog b ON a.device_catalog_id = b.id "
            + " where a.id in " +
            " <foreach collection='ids' item='deviceTypeId' open='(' close=')' separator=','>" +
            " #{deviceTypeId} " +
            " </foreach> "+
            "</script>")
    List<DeviceTypeResp> getByIds(@Param("ids") List<Long> deviceTypeIds);


    /**
     * @despriction：根据ifttt类型过滤
     * @author  yeshiyuan
     * @created 2018/11/23 17:21
     */
    @Select("<script>"
            + "SELECT "
            + " id, name "
            + " FROM device_type  "
            + " where id in " +
            " <foreach collection='ids' item='deviceTypeId' open='(' close=')' separator=','>" +
            " #{deviceTypeId} " +
            " </foreach> " +
            " <choose>" +
            "   <when test=\"iftttType == 'if'\">" +
            "   and ifttt_type in (1,3) " +
            "  </when>" +
            "   <when test=\"iftttType == 'then'\">" +
            "   and ifttt_type in (2,3) " +
            "   </when>" +
            " </choose>"+
            "</script>")
    List<DeviceTypeResp> getByIdsAndIfffType(@Param("ids") List<Long> ids, @Param("iftttType") String iftttType);

    @Select("<script>"
            + "SELECT a.name  FROM device_type a  where a.id in " +
            " <foreach collection='ids' item='deviceTypeId' open='(' close=')' separator=','>" +
            " #{deviceTypeId} " +
            " </foreach> "+
            "</script>")
    List<String> getDeviceTypeNameByIds(@Param("ids") List<Long> deviceTypeIds);

    /**
     *@description 根据设备类型id获取设备类型名称
     *@author wucheng
     *@params [deviceTypeIds]
     *@create 2018/12/12 10:53
     *@return java.util.List<com.iot.device.vo.rsp.DeviceTypeResp>
     */
    @Select("<script>"
            + "SELECT a.id, a.name  FROM device_type a  where a.id in " +
            " <foreach collection='ids' item='deviceTypeId' open='(' close=')' separator=','>" +
            " #{deviceTypeId} " +
            " </foreach> "+
            "</script>")
    List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(@Param("ids") List<Long> deviceTypeIds);
 }