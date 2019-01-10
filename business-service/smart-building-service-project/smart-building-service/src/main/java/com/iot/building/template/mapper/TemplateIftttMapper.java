package com.iot.building.template.mapper;

import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.template.domain.TemplateIfttt;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import java.util.List;

public interface TemplateIftttMapper {
    @Delete({
        "delete from template_ifttt",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into template_ifttt (id, template_id, ",
        "rule_id, create_time, ",
        "create_by, update_time, ",
        "update_by,tenant_id,org_id)",
        "values (#{id,jdbcType=BIGINT}, #{templateId,jdbcType=BIGINT}, ",
        "#{ruleId,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{createBy,jdbcType=BIGINT}, #{updateTime,jdbcType=TIMESTAMP}, ",
        "#{updateBy,jdbcType=BIGINT},#{tenantId,jdbcType=BIGINT},#{orgId,jdbcType=BIGINT})"
    })
    int insert(TemplateIfttt record);

    @InsertProvider(type=TemplateIftttSqlProvider.class, method="insertSelective")
    int insertSelective(TemplateIfttt record);

    @Select({
        "select",
        "id, template_id, rule_id, create_time, create_by, update_time, update_by, tenant_id,org_id",
        "from template_ifttt",
        "where rule_id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="template_id", property="templateId", jdbcType=JdbcType.BIGINT),
        @Result(column="rule_id", property="ruleId", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT)
    })
    TemplateIfttt selectByPrimaryKey(Long id);

    @UpdateProvider(type=TemplateIftttSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TemplateIfttt record);

    @Update({
        "update template_ifttt",
        "set template_id = #{templateId,jdbcType=BIGINT},",
          "rule_id = #{ruleId,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "update_by = #{updateBy,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(TemplateIfttt record);


    //////////////////////////////////////////////////以下为手动添加///////////////////////////////////////////////////////////////

    /**
     *  根据 templateId、tenantId 获取
     * @param templateId
     * @param tenantId
     * @return
     */
    @Select({
            "select",
            "ir.*",
            "from template_ifttt ti",
            "left join ifttt_rule ir on ir.id = ti.rule_id",
            "where ti.template_id = #{templateId}",
            "and ir.tenant_id = #{tenantId} and ti.org_id=#{orgId}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="template_id", property="templateId", jdbcType=JdbcType.BIGINT),
            @Result(column="rule_id", property="ruleId", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT)
    })
    List<RuleResp> getRuleByTemplateId(@Param("templateId") Long templateId, @Param("tenantId") Long tenantId,@Param("orgId") Long orgId);

    @Select({
            "select",
            "id, template_id, rule_id, create_time, create_by, update_time, update_by, tenant_id, org_id ",
            "from template_ifttt",
            "where template_id = #{templateId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} and org_id = #{orgId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="template_id", property="templateId", jdbcType=JdbcType.BIGINT),
            @Result(column="rule_id", property="ruleId", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT)
    })
    List<TemplateIfttt> selectByTemplateId(@Param("templateId") Long templateId, @Param("tenantId") Long tenantId, @Param("orgId") Long orgId);

    @Select({
            "select ",
            "id, template_id, rule_id, create_time, create_by, update_time, update_by, tenant_id,org_id ",
            "from template_ifttt ",
            "where template_id = #{templateId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="template_id", property="templateId", jdbcType=JdbcType.BIGINT),
            @Result(column="rule_id", property="ruleId", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT)
    })
    List<TemplateIfttt> queryByTemplateId(@Param("templateId") Long templateId);
}