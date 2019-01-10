package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradePlan;
import com.iot.device.vo.req.ota.UpgradePlanEditReq;
import com.iot.device.vo.rsp.ota.UpgradePlanResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public interface UpgradePlanMapper {

    @Insert({
        "insert into ota_upgrade_plan (id, tenant_id, ",
        "product_id, plan_status, ",
        "upgrade_type, target_version, ",
        "create_time, create_by, ",
        "update_time, update_by,edited_times,strategy_switch,upgrade_scope)",
        "values (#{id,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
        "#{productId,jdbcType=BIGINT}, #{planStatus,jdbcType=CHAR}, ",
        "#{upgradeType,jdbcType=CHAR}, #{targetVersion,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=BIGINT}, " ,
        "#{editedTimes,jdbcType=INTEGER}, #{strategySwitch,jdbcType=INTEGER}, " ,
        "#{upgradeScope,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "upgradePlan.id")
    int insertUpgradePlan(UpgradePlan upgradePlan);

    @Select({
        "select",
        "id, tenant_id, product_id, plan_status, upgrade_type, target_version, create_time, ",
        "create_by, update_time, update_by,edited_times ,strategy_switch, upgrade_scope  ",
        "from ota_upgrade_plan ",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
        @Result(column="plan_status", property="planStatus", jdbcType=JdbcType.VARCHAR),
        @Result(column="upgrade_type", property="upgradeType", jdbcType=JdbcType.VARCHAR),
        @Result(column="target_version", property="targetVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edited_times", property="editedTimes", jdbcType=JdbcType.INTEGER),
        @Result(column="strategy_switch", property="strategySwitch", jdbcType=JdbcType.INTEGER),
        @Result(column="upgrade_scope", property="upgradeScope", jdbcType=JdbcType.INTEGER),
    })
    UpgradePlanResp selectByProductId(@Param("productId") Long productId);

    @Select({
            "select update_time as updateTime",
            "from ota_upgrade_plan",
            "where id = #{id,jdbcType=BIGINT} "
    })
    Date selectUpdateTime(@Param("id") Long id);

    @Update({
        "update ota_upgrade_plan",
        "set upgrade_type = #{upgradeType,jdbcType=VARCHAR},",
          "target_version = #{targetVersion,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "update_by = #{userId,jdbcType=BIGINT},",
          "edited_times = #{editedTimes,jdbcType=INTEGER}, ",
          "strategy_switch = #{strategySwitch,jdbcType=INTEGER}, ",
          "upgrade_scope = #{upgradeScope,jdbcType=INTEGER} ",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UpgradePlanEditReq record);

    @Update({
            "update ota_upgrade_plan",
            "set plan_status = #{planStatus,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updatePlanStatus(@Param("id") Long id, @Param("planStatus") String planStatus);

}