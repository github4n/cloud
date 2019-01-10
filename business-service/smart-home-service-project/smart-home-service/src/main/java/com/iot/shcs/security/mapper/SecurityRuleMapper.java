package com.iot.shcs.security.mapper;

import com.iot.shcs.security.vo.SecurityRule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SecurityRuleMapper {

    @Delete({
            "delete from security_rule",
            "where security_id = #{securityId,jdbcType=BIGINT} and type = #{type,jdbcType=VARCHAR} "
    })
    int deleteByPrimaryKey(@Param("securityId") Long securityId, @Param("type") String type);


//    @InsertProvider(type = SecurityRuleSqlProvider.class, method = "insertBySpaceId")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    int insertSelective(SecurityRule record);

    @InsertProvider(type = SecurityRuleSqlProvider.class, method = "insertNewRule")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertNewRule(SecurityRule record);

    @Select({
            "select",
            "id, security_id, tenant_id, type, enabled, defer, delay, `if`,`then`, user_id, ",
            "create_time, update_time",
            "from security_rule",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "security_id", property = "securityId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "enabled", property = "enabled", jdbcType = JdbcType.INTEGER),
            @Result(column = "defer", property = "defer", jdbcType = JdbcType.INTEGER),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "if", property = "ifCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "then", property = "thenCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
    })
    SecurityRule selectByPrimaryKey(@Param("id") Long id);


    @Select({
            "select",
            "id, security_id, tenant_id, type, enabled, defer, delay, `if`,`then`, ",
            "create_time, update_time",
            "from security_rule",
            "where security_id = #{securityId,jdbcType=BIGINT} and type=#{type,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "security_id", property = "securityId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "enabled", property = "enabled", jdbcType = JdbcType.INTEGER),
            @Result(column = "defer", property = "defer", jdbcType = JdbcType.INTEGER),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "if", property = "ifCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "then", property = "thenCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
    })
    SecurityRule selectBySecurityIdAndType(@Param("securityId") Long securityId,@Param("type")String type);

    ///////////////////////////////////////以下为人工添加///////////////////////////////////////////////////

    @SelectProvider(type = SecurityRuleSqlProvider.class, method = "list")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "security_id", property = "securityId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "enabled", property = "enabled", jdbcType = JdbcType.INTEGER),
            @Result(column = "defer", property = "defer", jdbcType = JdbcType.INTEGER),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "if", property = "ifCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "then", property = "thenCondition", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
    })
    List<SecurityRule> list(SecurityRule req);

    /**
     *  获取简单列表(只返回 id这一列)
     * @param req
     * @return
     */
    @SelectProvider(type = SecurityRuleSqlProvider.class, method = "findSimpleList")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    List<SecurityRule> findSimpleList(SecurityRule req);

    @UpdateProvider(type = SecurityRuleSqlProvider.class, method = "updateBySecurityIdAndType")
    int updateBySecurityIdAndType(SecurityRule record);


}
