package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradePlanLog;
import com.iot.device.vo.req.ota.UpgradePlanReq;
import com.iot.device.vo.rsp.ota.UpgradePlanLogResp;
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
public interface UpgradePlanLogMapper {
    @Delete({
        "delete from ota_upgrade_plan_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_upgrade_plan_log (id, plan_id, ",
        "tenant_id, operation_type, ",
        "create_time, create_by)",
        "values (#{id,jdbcType=BIGINT}, #{planId,jdbcType=BIGINT}, ",
        "#{tenantId,jdbcType=BIGINT}, #{operationType,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT})"
    })
    int insertUpgradePlanLog(UpgradePlanLog record);

    @Select({
        "select",
        "id, plan_id, tenant_id, operation_type, create_time, create_by",
        "from ota_upgrade_plan_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results(id="upgradePlanLog",value={
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="plan_id", property="planId", jdbcType=JdbcType.BIGINT),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="operation_type", property="operationType", jdbcType=JdbcType.CHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT)
    })
    UpgradePlanLog selectByPrimaryKey(Long id);

    @Update({
        "update ota_upgrade_plan_log",
        "set plan_id = #{planId,jdbcType=BIGINT},",
          "tenant_id = #{tenantId,jdbcType=BIGINT},",
          "operation_type = #{operationType,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "create_by = #{createBy,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UpgradePlanLog record);

    @Select({
            "select",
            "t3.product_name as productName, t1.operation_type as operationType, t1.create_time as createTime, t1.create_by as createBy",
            "from ota_upgrade_plan_log t1 ",
            "inner join ota_upgrade_plan t2 ON t1.plan_id = t2.id",
            "inner join product t3 ON t2.product_id = t3.id",
            "where t1.tenant_id = #{tenantId,jdbcType=BIGINT} and t2.product_id = #{productId,jdbcType=BIGINT} ORDER BY t1.create_time DESC "
    })
    List<UpgradePlanLogResp> getUpgradePlanLog(com.baomidou.mybatisplus.plugins.Page<UpgradePlanLogResp> page,UpgradePlanReq upgradePlanReq);
}