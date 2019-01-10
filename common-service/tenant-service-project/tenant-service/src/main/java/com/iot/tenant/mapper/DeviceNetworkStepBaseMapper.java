package com.iot.tenant.mapper;

import com.iot.tenant.entity.DeviceNetworkStepBase;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:39
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:39
 * 修改描述：
 */
@Mapper
public interface DeviceNetworkStepBaseMapper {

    @Delete("delete from device_network_step_base where device_type_id = #{deviceTypeId,jdbcType=BIGINT} and data_status = 'valid' " +
            " and network_type_id = #{networkTypeId,jdbcType=BIGINT}")
    int deleteByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId, @Param("networkTypeId") Long networkTypeId);

    @Insert("<script>" +
            "insert into device_network_step_base(device_type_id,network_type_id,step,icon,data_status,create_time,create_by,is_help) " +
            "values " +
            "<foreach collection='list' item='base' separator=','>" +
            " ( " +
            "   #{base.deviceTypeId, jdbcType=BIGINT}," +
            "   #{base.networkTypeId, jdbcType=BIGINT}," +
            "   #{base.step}," +
            "   #{base.icon}," +
            "   #{base.dataStatus}," +
            "   #{base.createTime}," +
            "   #{base.createBy}," +
            "   #{base.isHelp}" +
            "  )" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<DeviceNetworkStepBase> list);

    @Select("<script>" +
            " select device_type_id as deviceTypeId, network_type_id as networkTypeId,step,icon, is_help as isHelp " +
            " from device_network_step_base where device_type_id = #{deviceTypeId,jdbcType=BIGINT} and data_status = 'valid' " +
            "<if test=\"networkTypeId!=null and networkTypeId!=''\">" +
            " and network_type_id = #{networkTypeId,jdbcType=BIGINT}" +
            "</if>" +
            " order by network_type_id, step asc" +
            "</script>")
    List<DeviceNetworkStepBase> queryNetworkStepBase(@Param("deviceTypeId") Long deviceTypeId, @Param("networkTypeId") Long networkTypeId);

    /**
     * @despriction：查询某设备类型支持的配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 13:53
     */
    @Select("select network_type_id from device_network_step_base where device_type_id = #{deviceTypeId,jdbcType=BIGINT} and data_status = 'valid' group by network_type_id")
    List<Long> supportNetworkType(@Param("deviceTypeId") Long deviceTypeId);
}
