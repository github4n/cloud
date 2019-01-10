package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 租户表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {


    /**
     * @despriction：通过租户名称查询租户id
     * @author  yeshiyuan
     * @created 2018/10/30 16:55
     * @return
     */
    @Select("select id from tenant where name like concat('%',#{name},'%')")
    List<Long> searchTenantIdsByName(@Param("name") String name);

    /**
     * 描述：查询租户code条数
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param code 租户code
     * @return long
     */
    @Select("<script>" +
            "select count(1) from tenant where code = #{code}" +
            "<if test=\"tenantId!=null and tenantId!=''\"> and id != #{tenantId}</if>" +
            "</script>")
    long getTenantCountByCode(@Param("code") String code, @Param("tenantId") Long tenantId);

    /**
     * 描述：更改租户code
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param code 租户code
     * @param tenantId 租户id
     * @return long
     */
    @Update("update tenant set code = #{code} where id = #{tenantId}")
    long updateTenantCode(@Param("code") String code, @Param("tenantId") Long tenantId);

}
