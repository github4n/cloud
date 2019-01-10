package com.iot.device.mapper.ota;


import com.iot.device.model.ota.StrategyReport;
import com.iot.device.vo.req.ota.StrategyReportResp;
import com.iot.device.vo.rsp.ota.FirmwareVersionResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface StrategyReportMapper {
    @Delete({
        "delete from ota_strategy_report",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_strategy_report (id, plan_id, ",
        "strategy_group, device_uuid, ",
        "model, upgrade_type, ",
        "target_version, original_version, ",
        "upgrade_result, reason, complete_time)",
        "values (#{id,jdbcType=BIGINT}, #{planId,jdbcType=BIGINT}, ",
        "#{strategyGroup,jdbcType=INTEGER}, #{deviceUuid,jdbcType=VARCHAR}, ",
        "#{model,jdbcType=VARCHAR}, #{upgradeType,jdbcType=CHAR}, ",
        "#{targetVersion,jdbcType=VARCHAR}, #{originalVersion,jdbcType=VARCHAR}, ",
        "#{upgradeResult,jdbcType=CHAR}, #{reason,jdbcType=VARCHAR},#{completeTime,jdbcType=TIMESTAMP})"
    })
    int insert(StrategyReport record);


    @Select({
        "select",
        "id, plan_id, strategy_group, device_uuid, model, upgrade_type, target_version, ",
        "original_version, upgrade_result, reason, complete_time",
        "from ota_strategy_report",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
        @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR),
        @Result(column="model", property="model", jdbcType=JdbcType.VARCHAR),
        @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.CHAR),
        @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="original_version", property="originalVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="upgrade_result", property="upgradeResult", jdbcType=JdbcType.CHAR),
        @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
        @Result(column="complete_time", property="completeTime", jdbcType=JdbcType.TIMESTAMP)
    })
    StrategyReport selectByPrimaryKey(Long id);


    @Update({
        "update ota_strategy_report",
        "set plan_id = #{planId,jdbcType=BIGINT},",
          "strategy_group = #{strategyGroup,jdbcType=INTEGER},",
          "device_uuid = #{deviceUuid,jdbcType=VARCHAR},",
          "model = #{model,jdbcType=VARCHAR},",
          "upgrade_type = #{upgradeType,jdbcType=CHAR},",
          "target_version = #{targetVersion,jdbcType=VARCHAR},",
          "original_version = #{originalVersion,jdbcType=VARCHAR},",
          "upgrade_result = #{upgradeResult,jdbcType=CHAR},",
          "complete_time = #{completeTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(StrategyReport record);

    @Delete({
            "delete from ota_strategy_report",
            "where device_uuid = #{uuid,jdbcType=VARCHAR}"
    })
    int deleteByUuid(@Param("uuid") String uuid);


    @Select({
            "select original_version from ota_strategy_report",
            "where plan_id = #{planId,jdbcType=BIGINT} and upgrade_result='Failed'"
    })
    List<String> selectFailUpgradeVersion(@Param("planId") Long planId);

    @Delete({
            "delete from ota_strategy_report",
            "where plan_id = #{planId,jdbcType=BIGINT}"
    })
    int deleteByPlanId(@Param("planId") Long planId);


    @Select({
            "select",
            "id, plan_id, strategy_group, device_uuid, model, upgrade_type, target_version, ",
            "original_version, upgrade_result, reason, complete_time",
            "from ota_strategy_report",
            "where plan_id = #{planId,jdbcType=BIGINT} and strategy_group = #{strategyGroup,jdbcType=INTEGER} order by complete_time asc "
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
            @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
            @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR),
            @Result(column="model", property="model", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.CHAR),
            @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_version", property="originalVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_result", property="upgradeResult", jdbcType=JdbcType.CHAR),
            @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
            @Result(column="complete_time", property="completeTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<StrategyReportResp> selectByGroup(@Param("planId") Long planId, @Param("strategyGroup") Integer strategyGroup);

    @Select({
            "select",
            "id, plan_id, strategy_group, device_uuid, model, upgrade_type, target_version, ",
            "original_version, upgrade_result, reason, complete_time",
            "from ota_strategy_report",
            "where device_uuid = #{deviceUuid,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
            @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
            @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR),
            @Result(column="model", property="model", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.CHAR),
            @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_version", property="originalVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_result", property="upgradeResult", jdbcType=JdbcType.CHAR),
            @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
            @Result(column="complete_time", property="completeTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<StrategyReportResp> selectStrategyReportByDevId(@Param("deviceUuid") String deviceUuid);


    @Select({
            "select",
            "id, plan_id, strategy_group, device_uuid, model, upgrade_type, target_version, ",
            "original_version, upgrade_result, reason, complete_time",
            "from ota_strategy_report",
            "where plan_id = #{planId,jdbcType=BIGINT} and strategy_group = #{strategyGroup,jdbcType=INTEGER} order by complete_time asc "
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
            @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
            @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR),
            @Result(column="model", property="model", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.CHAR),
            @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_version", property="originalVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="upgrade_result", property="upgradeResult", jdbcType=JdbcType.CHAR),
            @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
            @Result(column="complete_time", property="completeTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<StrategyReportResp> selectByGroupAsPage(com.baomidou.mybatisplus.plugins.Page<StrategyReportResp> page, @Param("planId") Long planId, @Param("strategyGroup") Integer strategyGroup);
}