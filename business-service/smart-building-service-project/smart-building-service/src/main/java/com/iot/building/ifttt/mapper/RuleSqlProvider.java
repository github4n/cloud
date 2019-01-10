package com.iot.building.ifttt.mapper;

import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class RuleSqlProvider {

    public String insertSelective(Rule record) {
        BEGIN();
        INSERT_INTO("ifttt_rule");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }

        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getIcon() != null) {
            VALUES("icon", "#{icon,jdbcType=VARCHAR}");
        }

        if (record.getDeployMentId() != null) {
            VALUES("deployMent_id", "#{deployMentId,jdbcType=BIGINT}");
        }

        if (record.getType() != null) {
            VALUES("type", "#{type,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=TINYINT}");
        }

        if (record.getIsMulti() != null) {
            VALUES("is_multi", "#{isMulti,jdbcType=TINYINT}");
        }

        if (record.getLocationId() != null) {
            VALUES("location_id", "#{locationId,jdbcType=BIGINT}");
        }

        if (record.getSpaceId() != null) {
            VALUES("space_id", "#{spaceId,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }

        if (record.getDirectId() != null) {
            VALUES("direct_id", "#{directId,jdbcType=VARCHAR}");
        }

        if (record.getOrgId() != null) {
            VALUES("org_id", "#{orgId,jdbcType=BIGINT}");
        }

        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getTemplateFlag() != null) {
            VALUES("template_flag", "#{templateFlag,jdbcType=TINYINT}");
        }

        if (record.getProductId() != null) {
            VALUES("product_id", "#{productId,jdbcType=BIGINT}");
        }

        if (record.getRuleType() != null) {
            VALUES("rule_type", "#{ruleType,jdbcType=TINYINT}");
        }

        if (record.getSecurityType() != null) {
            VALUES("security_type", "#{securityType,jdbcType=VARCHAR}");
        }

        if (record.getDelay() != null) {
            VALUES("delay", "#{delay,jdbcType=INTEGER}");
        }
        
        if (record.getTemplateId() != null) {
        	VALUES("template_id", "#{templateId,jdbcType=BIGINT}");
        }
        
        if (record.getIftttType() != null) {
        	VALUES("ifttt_type", "#{iftttType,jdbcType=VARCHAR}");
        }
        
        if (record.getShortcut() != null) {
        	VALUES("shortcut", "#{shortcut}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(Rule record) {
        BEGIN();
        UPDATE("ifttt_rule");

        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }

        if (record.getIcon() != null) {
            SET("icon = #{icon,jdbcType=VARCHAR}");
        }

        if (record.getDeployMentId() != null) {
            SET("deployMent_id = #{deployMentId,jdbcType=BIGINT}");
        }

        if (record.getType() != null) {
            SET("type = #{type,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=TINYINT}");
        }

        if (record.getIsMulti() != null) {
            SET("is_multi = #{isMulti,jdbcType=TINYINT}");
        }

        if (record.getLocationId() != null) {
            SET("location_id = #{locationId,jdbcType=BIGINT}");
        }

        if (record.getSpaceId() != null) {
            SET("space_id = #{spaceId,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        if (record.getUserId() != null) {
            SET("user_id = #{userId,jdbcType=BIGINT}");
        }

        if (record.getDirectId() != null) {
            SET("direct_id = #{directId,jdbcType=VARCHAR}");
        }

        if (record.getOrgId() != null) {
            SET("org_id = #{orgId,jdbcType=BIGINT}");
        }

        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getTemplateFlag() != null) {
            SET("template_flag = #{templateFlag,jdbcType=TINYINT}");
        }

        if (record.getProductId() != null) {
            SET("product_id = #{productId,jdbcType=BIGINT}");
        }

        if (record.getRuleType() != null) {
            SET("rule_type = #{ruleType,jdbcType=TINYINT}");
        }

        if (record.getSecurityType() != null) {
            SET("security_type = #{securityType,jdbcType=VARCHAR}");
        }

        if (record.getDelay() != null) {
            SET("delay = #{delay,jdbcType=INTEGER}");
        }
        
        if (record.getTemplateId() != null) {
        	SET("template_id = #{templateId,jdbcType=BIGINT}");
        }
        if (record.getShortcut() != null) {
        	SET("shortcut = #{shortcut}");
        }

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }

    public String list(RuleListReq req) {
        BEGIN();
        SELECT("id, name, icon, type, status, is_multi, location_id, space_id, tenant_id, user_id, " +
                "direct_id, org_id, create_time, template_flag, product_id, rule_type, security_type, " +
                "delay, template_id, ifttt_type ");
        FROM("ifttt_rule");

        if (req.getName() != null) {
            WHERE("name like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')");
        }

        if (req.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        if (req.getOrgId() != null) {
            WHERE("org_id = #{orgId,jdbcType=BIGINT}");
        }

        if (req.getLocationId() != null) {
            WHERE("location_id = #{locationId,jdbcType=BIGINT}");
        }

        if (req.getSpaceId() != null) {
            WHERE("space_id = #{spaceId,jdbcType=BIGINT}");
        }

        if (req.getUserId() != null) {
            WHERE("user_id = #{userId,jdbcType=BIGINT}");
        }

        if (req.getIsMulti() != null) {
            WHERE("is_multi = #{isMulti,jdbcType=BIGINT}");
        }

        if (req.getSecurityType() != null) {
            WHERE("security_type = #{securityType,jdbcType=VARCHAR}");
        }

        if (req.getRuleType() != null) {
            WHERE("rule_type = #{ruleType,jdbcType=TINYINT}");
        }

        if (req.getTemplateFlag() != null) {
            WHERE("template_flag = #{templateFlag,jdbcType=TINYINT}");
        }
        
        if (req.getType() != null) {
            WHERE("type = #{type,jdbcType=VARCHAR}");
        }
        
        if (req.getIftttType() != null) {
            WHERE("ifttt_type = #{iftttType,jdbcType=VARCHAR}");
        }

        if (req.getTemplateId() != null) {
            WHERE("template_id = #{templateId,jdbcType=BIGINT}");
        }

        ORDER_BY("create_time desc");

        return SQL();
    }

    /**
     *  获取简单列表(只返回 id这一列)
     * @param req
     * @return
     */
    public String findSimpleList(RuleListReq req) {
        BEGIN();
        SELECT("id, tenant_id ");
        FROM("ifttt_rule");

        if (req.getUserId() != null) {
            WHERE("user_id = #{userId,jdbcType=BIGINT}");
        }
        if (req.getDeployMentId() != null) {
            WHERE("deployMent_id = #{deployMentId,jdbcType=BIGINT}");
        }

        if (req.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        if (req.getOrgId() != null) {
            WHERE("org_id = #{orgId,jdbcType=BIGINT}");
        }

        if (req.getLocationId() != null) {
            WHERE("location_id = #{locationId,jdbcType=BIGINT}");
        }

        if (req.getSpaceId() != null) {
            WHERE("space_id = #{spaceId,jdbcType=BIGINT}");
        }else {
            WHERE("space_id IS NULL");
        }


        if (StringUtil.isNotEmpty(req.getName())) {
            WHERE("name like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')");
        }

        if (req.getIsMulti() != null) {
            WHERE("is_multi = #{isMulti,jdbcType=BIGINT}");
        }

        if (req.getSecurityType() != null) {
            WHERE("security_type = #{securityType,jdbcType=VARCHAR}");
        }

        if (req.getRuleType() != null) {
            WHERE("rule_type = #{ruleType,jdbcType=TINYINT}");
        }

        if (req.getTemplateFlag() != null) {
            WHERE("template_flag = #{templateFlag,jdbcType=TINYINT}");
        }

        if (req.getType() != null) {
            WHERE("type = #{type,jdbcType=VARCHAR}");
        }

        if (req.getTemplateId() != null) {
            WHERE("template_id = #{templateId,jdbcType=BIGINT}");
        }

        if (req.getIftttType() != null) {
            WHERE("ifttt_type = #{iftttType,jdbcType=VARCHAR}");
        }
        if (req.getShortcut() != null) {
        	WHERE("shortcut = #{shortcut,jdbcType=INTEGER}");
        }

        ORDER_BY("create_time desc");

        return SQL();
    }

    /**
     * 根据 ids 获取列表
     * @param ids
     * @return
     */
    public String listByIds(@Param("ids") List<String> ids) {
        BEGIN();
        SELECT("id, name, icon, deployMent_id,type, status, is_multi, location_id, space_id, tenant_id, user_id, " +
                "direct_id, org_id, create_time, update_time, template_flag, product_id, rule_type, security_type, " +
                "delay, template_id, ifttt_type ");
        FROM("ifttt_rule");

        if (CollectionUtils.isNotEmpty(ids)) {
            WHERE(" id IN ("+buildInStr(ids)+")");
        } else {
            WHERE(" id = -1");
        }
        ORDER_BY("CONVERT (name USING gbk) COLLATE gbk_chinese_ci ASC");
        return SQL();
    }

    // 构建in语句
    private String buildInStr(List<String> dataList) {
        StringBuilder inStr = new StringBuilder();
        for(int i=0;i<dataList.size();i++) {
            if(i==(dataList.size()-1)) {
                inStr.append("'"+dataList.get(i)+"'");
            }else {
                inStr.append("'"+dataList.get(i)+"'"+",");
            }
        }
        return inStr.toString();
    }
}