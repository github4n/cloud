package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradePlanDetail;
import com.iot.device.vo.req.ota.UpgradePlanDetailEditReq;
import com.iot.device.vo.rsp.ota.UpgradePlanDetailResp;
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
public interface UpgradePlanDetailMapper {

    @Insert({
        "insert into ota_upgrade_plan_detail (id, plan_id, ",
        "has_transition)",
        "values (#{id,jdbcType=BIGINT}, #{planId,jdbcType=BIGINT}, ",
        "#{hasTransition,jdbcType=BIT})"
    })
    int insertUpgradePlanDetail(UpgradePlanDetail record);

    @Select({
        "select",
        "id, plan_id, has_transition",
        "from ota_upgrade_plan_detail",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="has_transition", property="hasTransition", jdbcType=JdbcType.BIT)
    })
    UpgradePlanDetail selectByPrimaryKey(Long id);

    @Update({
        "update ota_upgrade_plan_detail",
        "set plan_id = #{planId,jdbcType=BIGINT},",
          "has_transition = #{hasTransition,jdbcType=BIT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UpgradePlanDetail record);

    @Select({
            "select",
            "id, plan_id, has_transition",
            "from ota_upgrade_plan_detail",
            "where plan_id = #{planId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
            @Result(column="has_transition", property="hasTransition", jdbcType=JdbcType.BIT)
    })
    List<UpgradePlanDetailResp> selectByplanId(Long planId);

    @Select({
            "select id",
            "from ota_upgrade_plan_detail",
            "where plan_id = #{planId,jdbcType=BIGINT}"
    })
    List<Long> getDetailIdByPlanId(Long planId);

    @Delete({
            "delete from ota_upgrade_plan_detail",
            "where plan_id = #{planId,jdbcType=BIGINT}"
    })
    int deleteByPlanId(Long planId);

    @Insert({ "<script>",
            "insert into ota_upgrade_plan_detail (id, plan_id,has_transition) values ",
            "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">",
            "(#{item.id,jdbcType=BIGINT}, #{item.planId,jdbcType=BIGINT},#{item.hasTransition,jdbcType=INTEGER})",
            "</foreach>",
            "</script>" })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int batchInsertUpgradePlanDetail(List<UpgradePlanDetailEditReq> upgradePlanDetailList);
}