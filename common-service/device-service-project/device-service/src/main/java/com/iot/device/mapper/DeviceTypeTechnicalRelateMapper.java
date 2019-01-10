package com.iot.device.mapper;

import com.iot.device.model.DeviceTypeTechnicalRelate;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：devicetype_technical_relate sql
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 19:19
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 19:19
 * 修改描述：
 */
@Mapper
public interface DeviceTypeTechnicalRelateMapper {

    @Delete("delete from devicetype_technical_relate where device_type_id = #{deviceTypeId}")
    int deleteByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId);

    @Insert("<script>" +
            "insert into devicetype_technical_relate(device_type_id, technical_scheme_id, create_time, create_by)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            " (" +
            "  #{item.deviceTypeId}, " +
            "  #{item.technicalSchemeId}," +
            "  #{item.createTime}," +
            "  #{item.createBy}" +
            " )" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<DeviceTypeTechnicalRelate> list);


    @Select("select technical_scheme_id from devicetype_technical_relate where device_type_id = #{deviceTypeId}")
    List<Long> findTechnicalIdsByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId);

    /**
      * @despriction：设备支持的配网类型
      * @author  yeshiyuan
      * @created 2018/10/15 20:31
      * @return
      */
    /*@Select("SELECT id,network_name from network_type where id in ( " +
            " SELECT network_type_id from technical_network_relate where technical_scheme_id in ( " +
            "  SELECT technical_scheme_id from devicetype_technical_relate " +
            "    where device_type_id = #{deviceTypeId}" +
            " )" +
            ")")
    List<NetworkTypeResp> deviceSupportNetworkType(@Param("deviceTypeId") Long deviceTypeId);*/

    /**
      * @despriction：方案支持的配网类型
      * @author  yeshiyuan
      * @created 2018/10/15 20:56
      * @return
      */
    @Select("SELECT id,network_name,type_code from network_type where id in ( " +
            "  SELECT network_type_id from technical_network_relate " +
            "  where technical_scheme_id = #{technicalId}" +
            ")")
    List<NetworkTypeResp> technicalSupportNetworkType(@Param("technicalId") Long technicalId);

    @Select("<script>" +
            "SELECT id,network_name from network_type where id in ( " +
            "  SELECT DISTINCT network_type_id from technical_network_relate " +
            "  where technical_scheme_id in " +
            " <foreach collection='technicalIds' item='technicalId' open='(' close=')' separator=','>" +
            "   #{technicalId}" +
            " </foreach>" +
            ")" +
            "</script>")
    List<NetworkTypeResp> findNetworkByTechnicalIds(@Param("technicalIds") List<Long> technicalIds);
}
