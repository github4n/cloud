package com.iot.building.ifttt.mapper;

import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import java.util.List;
import java.util.Map;

public interface RuleTobMapper {
    @Delete({
            "delete from build_tob_rule",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert({
            "insert into ifttt_rule (id, name, ",
            "icon, type, status, ",
            "is_multi, location_id, ",
            "space_id, tenant_id, ",
            "user_id, direct_id, ",
            "org_id, create_time, ",
            "template_flag, product_id, ",
            "rule_type, security_type, ",
            "delay, template_id, ifttt_type)",
            "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
            "#{icon,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, #{status,jdbcType=TINYINT}, ",
            "#{isMulti,jdbcType=TINYINT}, #{locationId,jdbcType=BIGINT}, ",
            "#{spaceId,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
            "#{userId,jdbcType=BIGINT}, #{directId,jdbcType=VARCHAR}, ",
            "#{orgId,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{templateFlag,jdbcType=TINYINT}, #{productId,jdbcType=BIGINT}, ",
            "#{ruleType,jdbcType=TINYINT}, #{securityType,jdbcType=VARCHAR}, ",
            "#{delay,jdbcType=INTEGER}, #{templateId,jdbcType=BIGINT}, #{iftttType,jdbcType=VARCHAR})"
    })
    int insert(Rule record);

    @InsertProvider(type = RuleSqlTobProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(Rule record);

    @Select({
            "select",
            "id, name, icon, type, status, is_multi, location_id, space_id, tenant_id, user_id, ",
            "direct_id, create_time, update_time, template_flag, product_id, rule_type, ",
            "template_id, ifttt_type,appletId,upload_status ",
            "from build_tob_rule",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.TINYINT),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.TINYINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.TINYINT),
            @Result(column = "template_id", property = "templateId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ifttt_type", property = "iftttType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "appletId", property = "appletId", jdbcType = JdbcType.BIGINT),
            @Result(column = "upload_status", property = "uploadStatus", jdbcType = JdbcType.VARCHAR)
    })
    Rule selectByPrimaryKey(@Param("id") Long id);

    @Select({
            "select",
            "id, name, icon, type, status, is_multi, location_id, space_id, tenant_id, user_id, ",
            "direct_id, org_id, create_time, template_flag, product_id, rule_type, security_type, ",
            "delay, template_id, ifttt_type ",
            "from ifttt_rule",
            "where direct_id = #{directDeviceId,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.TINYINT),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.TINYINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.TINYINT),
            @Result(column = "security_type", property = "securityType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "template_id", property = "templateId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ifttt_type", property = "iftttType", jdbcType = JdbcType.VARCHAR)
    })
    List<Rule> getByDirectDeviceId(@Param("directDeviceId") String directDeviceId);

    @UpdateProvider(type = RuleSqlTobProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Rule record);

    @Update({
            "update ifttt_rule",
            "set name = #{name,jdbcType=VARCHAR},",
            "icon = #{icon,jdbcType=VARCHAR},",
            "type = #{type,jdbcType=VARCHAR},",
            "status = #{status,jdbcType=TINYINT},",
            "is_multi = #{isMulti,jdbcType=TINYINT},",
            "location_id = #{locationId,jdbcType=BIGINT},",
            "space_id = #{spaceId,jdbcType=BIGINT},",
            "tenant_id = #{tenantId,jdbcType=BIGINT},",
            "user_id = #{userId,jdbcType=BIGINT},",
            "direct_id = #{directId,jdbcType=VARCHAR},",
            "org_id = #{orgId,jdbcType=BIGINT},",
            "create_time = #{createTime,jdbcType=TIMESTAMP},",
            "update_time = #{updateTime,jdbcType=TIMESTAMP},",
            "template_flag = #{templateFlag,jdbcType=TINYINT},",
            "product_id = #{productId,jdbcType=BIGINT},",
            "rule_type = #{ruleType,jdbcType=TINYINT},",
            "security_type = #{securityType,jdbcType=VARCHAR},",
            "delay = #{delay,jdbcType=INTEGER},",
            "template_id = #{templateId,jdbcType=BIGINT},",
            "ifttt_type = #{iftttType,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Rule record);

    ///////////////////////////////////////以下为人工添加///////////////////////////////////////////////////

    @SelectProvider(type = RuleSqlTobProvider.class, method = "list")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.TINYINT),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.TINYINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.TINYINT),
            @Result(column = "security_type", property = "securityType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "template_id", property = "templateId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ifttt_type", property = "iftttType", jdbcType = JdbcType.VARCHAR)
    })
    List<Rule> list(RuleListReq req);

    /**
     *  获取简单列表(只返回 id这一列)
     * @param req
     * @return
     */
    @SelectProvider(type = RuleSqlTobProvider.class, method = "findSimpleList")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    List<Rule> findSimpleList(RuleListReq req);

    /**
     *  根据 ids 获取列表
     * @param ids
     * @return
     */
    @SelectProvider(type = RuleSqlTobProvider.class, method = "listByIds")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.TINYINT),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
           /* @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT),*/
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.TINYINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.TINYINT),
          /*  @Result(column = "security_type", property = "securityType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),*/
            @Result(column = "template_id", property = "templateId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ifttt_type", property = "iftttType", jdbcType = JdbcType.VARCHAR)
    })
    List<Rule> listByIds(@Param("ids") List<String> ids);

    @Select({
            "select",
            "id, name ",
            "from ifttt_rule",
            "where name=#{name,jdbcType=VARCHAR} ",
            "and user_id = #{userId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    List<Map<String, Object>> checkName(@Param("name") String name, @Param("userId") Long userId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_rule",
            "where user_id = #{userId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} and template_flag = 0"
    })
    int delByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    @Select({"SELECT id, name, type, status, is_multi, location_id, space_id, tenant_id, user_id," +
            " direct_id, org_id, rule_type, template_flag, template_id, ifttt_type " +
            " FROM " +
            " ifttt_rule " +
            " WHERE type = #{type} and template_flag = 0"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.TINYINT),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.TINYINT),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.TINYINT),
            @Result(column = "template_id", property = "templateId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ifttt_type", property = "iftttType", jdbcType = JdbcType.VARCHAR)
    })
    List<Rule> selectByType(@Param("type") String type);

    @Select({"SELECT id " +
            "FROM " +
            "ifttt_rule " +
            "WHERE template_id = #{templateId} and template_flag = 0"
    })
    List<Long> selectRuleIdByTemplateId(@Param("templateId") Long templateId);

    @Select({" SELECT b.time_zone_offset FROM ifttt_rule a " +
            " LEFT JOIN space b ON a.user_id = b.user_id " +
            " WHERE a.id = #{ruleId} AND b.default_space = 1"})
    Integer getTimeZoneOffsetByRuleId(@Param("ruleId") Long ruleId);

    @Select({
            "select",
            "id, name ",
            "from build_tob_rule",
            "where name=#{name,jdbcType=VARCHAR} ",
            "and tenant_id = #{tenantId,jdbcType=BIGINT}",
            "and location_id = #{locationId,jdbcType=BIGINT}"
    })
    List<RuleResp> getRuleListByName(SaveIftttReq ruleVO);

    @Select({
            "<script> ",
            " select id as id,name as name,deployMent_id as deployMentId,type as type,status as status,location_id as locationId, ",
            " space_id as spaceId,tenant_id as tenantId,user_id as userId,product_id as productId,template_id as templateId,appletId as appletId,upload_status as uploadStatus ",
            " from build_tob_rule where 1=1 ",
            "<if test=\"ruleVO.id != null\"> and id = #{ruleVO.id}</if>",
            "<if test=\"ruleVO.productId != null\"> and product_id = #{ruleVO.productId}</if>",
            "<if test=\"ruleVO.tenantId != null\"> and tenant_id = #{ruleVO.tenantId}</if>",
            "<if test=\"ruleVO.locationId != null\"> and location_id = #{ruleVO.locationId}</if>",
            "<if test=\"ruleVO.name != null\"> and name = #{ruleVO.name}</if>",
            "<if test=\"ruleVO.deployMentId != null\"> and deployMent_id = #{ruleVO.deployMentId}</if>",
            "<if test=\"ruleVO.spaceId != null\"> and space_id = #{ruleVO.spaceId}</if>",
            "<if test=\"ruleVO.templateId != null\"> and template_id = #{ruleVO.templateId}</if>",
            "<if test=\"ruleVO.appletId != null\"> and appletId = #{ruleVO.appletId}</if>",
            "<if test=\"ruleVO.uploadStatus != null\"> and upload_status = #{ruleVO.uploadStatus}</if>",
            "</script> "
    })
    List<RuleResp> getRuleList(@Param("ruleVO") SaveIftttReq ruleVO);
}