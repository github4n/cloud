package com.iot.shcs.ifttt.mapper;

import com.iot.shcs.ifttt.entity.Automation;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * <p>
 * 联动表 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-17
 */
@Mapper
public interface AutomationMapper extends BaseMapper<Automation> {

    /**
     * 获取简单列表(只返回 id这一列)
     *
     * @param
     * @return
     */
    @SelectProvider(type = AutomationSqlProvider.class, method = "findSimpleList")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true)
    })
    List<Automation> findSimpleList(Automation record);

    /**
     * 根据 ids 获取列表
     *
     * @param ids
     * @return
     */
    @SelectProvider(type = AutomationSqlProvider.class, method = "listByIds")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "trigger_type", property = "triggerType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "space_id", property = "spaceId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "applet_id", property = "appletId", jdbcType = JdbcType.BIGINT),
            @Result(column = "is_multi", property = "isMulti", jdbcType = JdbcType.INTEGER),
            @Result(column = "direct_id", property = "directId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_type", property = "ruleType", jdbcType = JdbcType.INTEGER),
            @Result(column = "security_type", property = "securityType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "template_flag", property = "templateFlag", jdbcType = JdbcType.INTEGER),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "defer", property = "defer", jdbcType = JdbcType.INTEGER),
            @Result(column = "time_json", property = "timeJson", jdbcType = JdbcType.VARCHAR),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "dev_scene_id", property = "devSceneId", jdbcType = JdbcType.INTEGER),
            @Result(column = "dev_timer_id", property = "devTimerId", jdbcType = JdbcType.INTEGER),
    })
    List<Automation> listByIds(@Param("ids") List<Long> ids);

    @Select({
            "select count(1) ",
            "from automation ",
            "where user_id = #{userId,jdbcType=BIGINT} and name = #{name,jdbcType=VARCHAR}"
    })
    int checkAutoName(@Param("userId") Long userId, @Param("name") String name);

    @Select({
            "select id ",
            "from automation ",
            "where direct_id = #{directId,jdbcType=VARCHAR}"
    })
    List<Long> getByDirectId(@Param("directId") String directId);
}
