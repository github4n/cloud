package com.iot.device.mapper.ota;


import com.iot.device.model.ota.StrategyConfig;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface StrategyConfigMapper {
    @Delete({
        "delete from ota_strategy_config",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_strategy_config (id, tenant_id, ",
        "plan_id, strategy_group, ",
        "upgrade_total, threshold, ",
        "trigger_action, create_by, ",
        "create_time, update_by, ",
        "update_time)",
        "values (#{id,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
        "#{planId,jdbcType=BIGINT}, #{strategyGroup,jdbcType=INTEGER}, ",
        "#{upgradeTotal,jdbcType=INTEGER}, #{threshold,jdbcType=INTEGER}, ",
        "#{triggerAction,jdbcType=TINYINT}, #{createBy,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(StrategyConfig record);

    @Select({
        "select",
        "id, tenant_id, plan_id, strategy_group, upgrade_total, threshold, trigger_action, ",
        "create_by, create_time, update_by, update_time, is_deleted",
        "from ota_strategy_config",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
        @Result(column="upgrade_total", property="upgradeTotal", jdbcType=JdbcType.INTEGER),
        @Result(column="threshold", property="threshold", jdbcType=JdbcType.INTEGER),
        @Result(column="trigger_action", property="triggerAction", jdbcType=JdbcType.TINYINT),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_deleted", property="isDeleted", jdbcType=JdbcType.CHAR)
    })
    StrategyConfig selectByPrimaryKey(Long id);


    @Update({
        "update ota_strategy_config",
        "set tenant_id = #{tenantId,jdbcType=BIGINT},",
          "plan_id = #{planId,jdbcType=BIGINT},",
          "strategy_group = #{strategyGroup,jdbcType=INTEGER},",
          "upgrade_total = #{upgradeTotal,jdbcType=INTEGER},",
          "threshold = #{threshold,jdbcType=INTEGER},",
          "trigger_action = #{triggerAction,jdbcType=TINYINT},",
          "create_by = #{createBy,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_by = #{updateBy,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "is_deleted = #{isDeleted,jdbcType=CHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(StrategyConfig record);

    @Select({
            "select",
            "id, tenant_id, plan_id, strategy_group, upgrade_total, threshold, trigger_action ",
            "from ota_strategy_config",
            "where plan_id = #{planId,jdbcType=BIGINT} AND  tenant_id = #{tenantId,jdbcType=BIGINT} AND is_deleted = 'valid' ORDER BY strategy_group ASC "
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
            @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
            @Result(column="upgrade_total", property="upgradeTotal", jdbcType=JdbcType.INTEGER),
            @Result(column="threshold", property="threshold", jdbcType=JdbcType.INTEGER),
            @Result(column="trigger_action", property="triggerAction", jdbcType=JdbcType.TINYINT),
    })
    List<StrategyConfig> selectByPlanId(@Param("planId") Long planId, @Param("tenantId") Long tenantId);


    @Delete({
            "delete from ota_strategy_config",
            "where plan_id = #{planId,jdbcType=BIGINT} AND  tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPlanId(@Param("planId") Long planId, @Param("tenantId") Long tenantId);
}