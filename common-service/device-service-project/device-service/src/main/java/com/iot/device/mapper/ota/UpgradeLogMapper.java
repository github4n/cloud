package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradeLog;
import com.iot.device.vo.req.ota.UpgradeLogReq;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Mapper
public interface UpgradeLogMapper {
    @Delete({
        "delete from ota_upgrade_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_upgrade_log (id, ",
        "plan_id, device_uuid, ",
        "upgrade_type, target_version, ",
        "original_version, upgrade_result, ",
        "complete_time,upgrade_msg)",
        "values (#{id,jdbcType=BIGINT}, ",
        "#{planId,jdbcType=BIGINT}, #{deviceUuid,jdbcType=VARCHAR}, ",
        "#{upgradeType,jdbcType=CHAR}, #{targetVersion,jdbcType=VARCHAR}, ",
        "#{originalVersion,jdbcType=VARCHAR}, #{upgradeResult,jdbcType=CHAR}, ",
        "#{completeTime,jdbcType=TIMESTAMP}, #{upgradeMsg,jdbcType=VARCHAR})"
    })
    int addUpgradeLog(UpgradeLog record);

    @Select({
        "select",
        "id, plan_id, device_uuid, upgrade_type, target_version, original_version, ",
        "upgrade_result, complete_time, upgrade_msg ",
        "from ota_upgrade_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results(id="upgradeLog",value={
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR),
        @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.CHAR),
        @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="original_version", property="originalVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="upgrade_result", property="upgradeResult", jdbcType=JdbcType.CHAR),
        @Result(column="complete_time", property="completeTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="upgrade_msg", property="upgradeMsg", jdbcType=JdbcType.VARCHAR)
    })
    UpgradeLog selectByPrimaryKey(Long id);

    @Update({
        "update ota_upgrade_log",
        "set ",
          "plan_id = #{planId,jdbcType=BIGINT},",
          "device_uuid = #{deviceUuid,jdbcType=VARCHAR},",
          "upgrade_type = #{upgradeType,jdbcType=CHAR},",
          "target_version = #{targetVersion,jdbcType=VARCHAR},",
          "original_version = #{originalVersion,jdbcType=VARCHAR},",
          "upgrade_result = #{upgradeResult,jdbcType=CHAR},",
          "complete_time = #{completeTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UpgradeLog record);

    @Select({
            "select",
            "t1.id as id, t1.plan_id as planId, t1.device_uuid as deviceUuid, t1.upgrade_type as upgradeType, t1.target_version as targetVersion, t1.original_version as originalVersion, ",
            "t1.upgrade_result as upgradeResult, t1.complete_time as completeTime, t1.upgrade_msg as upgradeMsg ",
            "from ota_upgrade_log t1",
            "inner join ota_upgrade_plan t2 ON t1.plan_id = t2.id",
            "where t1.upgrade_result = #{upgradeResult,jdbcType=VARCHAR} and t2.product_id = #{productId,jdbcType=BIGINT} "
    })
    List<UpgradeLog> getUpgradeLog(com.baomidou.mybatisplus.plugins.Page<UpgradeLog> page ,UpgradeLogReq upgradeLogReq);

    @Select({
            "select",
            "t1.id as id, t1.plan_id as planId, t1.device_uuid as deviceUuid, t1.upgrade_type as upgradeType, t1.target_version as targetVersion, t1.original_version as originalVersion, ",
            "t1.upgrade_result as upgradeResult, t1.complete_time as completeTime, t1.upgrade_msg as upgradeMsg ",
            "from ota_upgrade_log t1",
            "where t1.device_uuid = #{deviceId,jdbcType=VARCHAR} and t1.original_version = #{version,jdbcType=VARCHAR} "
    })
    List<UpgradeLog> getUpgradeLogBydeviceId(@Param("deviceId") Long deviceId, @Param("version") String version);
}