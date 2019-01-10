package com.iot.device.mapper.ota;

import com.iot.device.model.ota.StrategyDetail;
import com.iot.device.vo.req.ota.StrategyDetailReq;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface StrategyDetailMapper {
    @Delete({
        "delete from ota_strategy_detail",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_strategy_detail (id, plan_id, ",
        "strategy_group, batch_num, ",
        "device_uuid)",
        "values (#{id,jdbcType=BIGINT}, #{planId,jdbcType=BIGINT}, ",
        "#{strategyGroup,jdbcType=INTEGER}, #{batchNum,jdbcType=BIGINT}, ",
        "#{deviceUuid,jdbcType=VARCHAR})"
    })
    int insert(StrategyDetailReq record);


    @Select({
        "select",
        "id, plan_id, strategy_group, batch_num, device_uuid",
        "from ota_strategy_detail",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="strategy_group", property="strategyGroup", jdbcType=JdbcType.INTEGER),
        @Result(column="batch_num", property="batchNum", jdbcType=JdbcType.BIGINT),
        @Result(column="device_uuid", property="deviceUuid", jdbcType=JdbcType.VARCHAR)
    })
    StrategyDetail selectByPrimaryKey(Long id);


    @Update({
        "update ota_strategy_detail",
        "set plan_id = #{planId,jdbcType=BIGINT},",
          "strategy_group = #{strategyGroup,jdbcType=INTEGER},",
          "batch_num = #{batchNum,jdbcType=BIGINT},",
          "device_uuid = #{deviceUuid,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(StrategyDetail record);


    @Select({
            "select device_uuid from ota_strategy_detail ",
            " where plan_id = #{planId,jdbcType=BIGINT}"
    })
    List<String> selectUuidByPlanId(@Param("planId") Long planId);

    @Select({
            "SELECT e.device_id FROM device_extend e,ota_strategy_detail d WHERE e.batch_num=d.batch_num ",
            " AND plan_id = #{planId,jdbcType=BIGINT}  "
    })
    List<String> selectUuidByBatchNumAndPlanId(@Param("planId") Long planId);

    @Select({
            "select batch_num from ota_strategy_detail ",
            " where plan_id = #{planId,jdbcType=BIGINT}"
    })
    List<Long> selectBatchByPlanId(@Param("planId") Long planId);

    @Select({
            "select device_uuid from ota_strategy_detail ",
            " where plan_id = #{planId,jdbcType=BIGINT} AND strategy_group = #{strategyGroup,jdbcType=INTEGER} "
    })
    List<String> selectUuidByPlanIdAndGroup(@Param("planId") Long planId,@Param("strategyGroup") Integer strategyGroup);

    @Delete({
            "delete from ota_strategy_detail",
            "where plan_id = #{planId,jdbcType=BIGINT} "
    })
    int deleteByPlanId(@Param("planId") Long planId);

    @Select({
            "select device_uuid from ota_strategy_detail ",
            " where plan_id = #{planId,jdbcType=BIGINT} and strategy_group is not null "
    })
    List<String> selectStrategyUuidByPlanId(@Param("planId") Long planId);

}