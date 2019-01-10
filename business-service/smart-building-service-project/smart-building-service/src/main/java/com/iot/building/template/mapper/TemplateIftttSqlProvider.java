package com.iot.building.template.mapper;

import com.iot.building.template.domain.TemplateIfttt;
import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class TemplateIftttSqlProvider {

    public String insertSelective(TemplateIfttt record) {
        BEGIN();
        INSERT_INTO("template_ifttt");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getTemplateId() != null) {
            VALUES("template_id", "#{templateId,jdbcType=BIGINT}");
        }
        
        if (record.getRuleId() != null) {
            VALUES("rule_id", "#{ruleId,jdbcType=BIGINT}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateBy() != null) {
            VALUES("create_by", "#{createBy,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateBy() != null) {
            VALUES("update_by", "#{updateBy,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }
        if (record.getOrgId() != null) {
        	VALUES("org_id", "#{orgId,jdbcType=BIGINT}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(TemplateIfttt record) {
        BEGIN();
        UPDATE("template_ifttt");
        
        if (record.getTemplateId() != null) {
            SET("template_id = #{templateId,jdbcType=BIGINT}");
        }
        
        if (record.getRuleId() != null) {
            SET("rule_id = #{ruleId,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateBy() != null) {
            SET("update_by = #{updateBy,jdbcType=BIGINT}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }
}