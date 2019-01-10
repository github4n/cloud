package com.iot.shcs.security.mapper;

import com.iot.shcs.security.domain.Security;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface SecurityMapper {

    @Delete({
            "delete from security",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
            "insert into security (space_id, ",
            "arm_mode, password, ",
            "create_time, ",
            "update_time, create_by, ",
            "update_by, tenant_id, ",
            "org_id)",
            "values (#{spaceId,jdbcType=BIGINT}, ",
            "#{armMode,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
            "#{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT}, ",
            "#{updateBy,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
            "#{orgId,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Security record);

    @InsertProvider(type = SecuritySqlProvider.class, method = "insertSelective")
    int insertSelective(Security record);

    @Select({
            "select",
            "id, space_id, arm_mode, password, create_time, update_time, create_by, ",
            "update_by, tenant_id, org_id",
            "from security",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "arm_mode", property = "armMode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_by", property = "createBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "update_by", property = "updateBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT)
    })
    Security selectByPrimaryKey(Long id);

    /**
     * 根据 spaceId 获取 Security
     *
     * @param spaceId
     * @return
     */
    @Select({
            "select",
            "id, space_id, arm_mode, password, create_time, update_time, create_by, ",
            "update_by, tenant_id, org_id",
            "from security",
            "where space_id = #{spaceId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "arm_mode", property = "armMode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_by", property = "createBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "update_by", property = "updateBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT)
    })
    public Security getBySpaceId(@Param("spaceId") Long spaceId);

    @Select({
            "select",
            "id, space_id, arm_mode, password, create_time, update_time, create_by, ",
            "update_by, tenant_id, org_id",
            "from security",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "arm_mode", property = "armMode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_by", property = "createBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "update_by", property = "updateBy", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "org_id", property = "orgId", jdbcType = JdbcType.BIGINT)
    })
    public Security getSecurityById(@Param("id") Long id);

    @UpdateProvider(type = SecuritySqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Security record);

    @Update({
            "update security",
            "set space_id = #{spaceId,jdbcType=BIGINT},",
            "arm_mode = #{armMode,jdbcType=VARCHAR},",
            "password = #{password,jdbcType=VARCHAR},",
            "create_time = #{createTime,jdbcType=TIMESTAMP},",
            "update_time = #{updateTime,jdbcType=TIMESTAMP},",
            "create_by = #{createBy,jdbcType=BIGINT},",
            "update_by = #{updateBy,jdbcType=BIGINT},",
            "tenant_id = #{tenantId,jdbcType=BIGINT},",
            "org_id = #{orgId,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Security record);


    /**
     * 更新 安防模式
     *
     * @param spaceId
     * @param updateBy
     * @param armMode
     */
    @Update({
            "update security set",
            "arm_mode = #{armMode,jdbcType=VARCHAR},",
            "update_time = now(),",
            "update_by = #{updateBy,jdbcType=BIGINT}",
            "where space_id = #{spaceId,jdbcType=BIGINT} and tenant_id=#{tenantId,jdbcType=BIGINT}"
    })
    public void updateArmModeBySpaceId(@Param("spaceId") Long spaceId, @Param("tenantId") Long tenantId,@Param("updateBy") Long updateBy, @Param("armMode") String armMode);

    /**
     * 修改 安防密码
     *
     * @param securityId
     * @param updateBy
     * @param password
     */
    @Update({
            "update security set",
            "password = #{password,jdbcType=VARCHAR},",
            "update_time = now(),",
            "update_by = #{updateBy,jdbcType=BIGINT}",
            "where id = #{securityId,jdbcType=BIGINT}"
    })
    public void updatePasswordById(@Param("securityId") Long securityId, @Param("updateBy") Long updateBy, @Param("password") String password);

    /**
     * 安防信息恢复默认值
     *
     * @param security
     */
    @UpdateProvider(type = SecuritySqlProvider.class, method = "securityResetFactory")
    public void securityResetFactory(Security security);
}