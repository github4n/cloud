package com.iot.shcs.security.mapper;

import com.iot.shcs.security.vo.SecurityRule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

public class SecurityRuleSqlProvider {
    public String insertNewRule(SecurityRule record) {
        BEGIN();
        INSERT_INTO("security_rule");
        if (record.getType() != null) {
            VALUES("type", "#{type,jdbcType=VARCHAR}");
        }
        if (record.getSecurityId() != null) {
            VALUES("security_id", "#{securityId,jdbcType=BIGINT}");
        }
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        if (record.getDelay() != null) {
            VALUES("delay", "#{delay,jdbcType=INTEGER}");
        }
        if (record.getDefer() != null) {
            VALUES("defer", "#{defer,jdbcType=INTEGER}");
        }
        if(record.getEnabled()!=null){
            VALUES("enabled", "#{enabled,jdbcType=INTEGER}");
        }
        if(record.getTenantId()!=null){
            ;VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }
        if(record.getIfCondition()!=null){
            VALUES("`if`", "#{ifCondition,jdbcType=VARCHAR}");
        }
        if(record.getThenCondition()!=null){
            VALUES("`then`", "#{thenCondition,jdbcType=VARCHAR}");
        }
        return SQL();
    }

    public String updateBySecurityIdAndType(SecurityRule record) {
        BEGIN();
        UPDATE("security_rule");
        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        if(record.getEnabled()!=null){
            SET("enabled = #{enabled,jdbcType=INTEGER}");
        }

        if (record.getDelay() != null) {
            SET("delay = #{delay,jdbcType=INTEGER}");
        }

        if(record.getDefer()!=null){
            SET("defer = #{defer,jdbcType=INTEGER}");
        }


        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

        if(record.getIfCondition()!=null){
            SET("`if` = #{ifCondition,jdbcType=VARCHAR}");
        }
        if(record.getThenCondition()!=null){
            SET("`then` = #{thenCondition,jdbcType=VARCHAR}");
        }

        WHERE("security_id = #{securityId,jdbcType=BIGINT} and type = #{type,jdbcType=VARCHAR}");

        return SQL();
    }

    public String list(SecurityRule req) {
        BEGIN();
        SELECT("id,security_id,tenant_id,type,enabled,defer,delay,`if`,`then`,create_time,update_time ");
        FROM("security_rule");

        if (req.getId() != null) {
            WHERE("id = #{id,jdbcType=BIGINT}");
        }
        if (req.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        if (req.getSecurityId() != null) {
            WHERE("security_id = #{securityId,jdbcType=BIGINT}");
        }

        if (req.getType() != null) {
            WHERE("type = #{type,jdbcType=VARCHAR}");
        }


        ORDER_BY("create_time desc");

        return SQL();
    }

    /**
     *  获取简单列表(只返回 id这一列)
     * @param req
     * @return
     */
    public String findSimpleList(SecurityRule req) {
        BEGIN();
        SELECT("id, tenant_id ");
        FROM("security_rule");

        if (req.getId() != null) {
            WHERE("id = #{id,jdbcType=BIGINT}");
        }

        if (req.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        ORDER_BY("create_time desc");

        return SQL();
    }


}
