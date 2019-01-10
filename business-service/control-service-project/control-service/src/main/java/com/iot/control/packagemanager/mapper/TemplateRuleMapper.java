package com.iot.control.packagemanager.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.packagemanager.entity.TemplateRule;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TemplateRuleMapper extends BaseMapper<TemplateRule>{

    @Delete("delete from template_rule where package_id = #{packageId,jdbcType=BIGINT} and tenant_id = #{tenantId}")
    int deleteByPackageIdAndTenantId(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);


    @Select("select count(id) from template_rule where package_id = #{packageId,jdbcType=BIGINT} and tenant_id = #{tenantId}")
    int getTotalNumber(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);

    /**
     *@description 修改策略的json、ruleName、updateTime
     *@author wucheng
     *@params [t]
     *@create 2018/12/11 15:41
     *@return int
     */
    @Update("<script>" +
            " update template_rule " +
            " <set>" +
            " <if test=\"json != null and json !=''\">json = #{json},</if>" +
            " <if test=\"ruleName != null and ruleName != ''\">rule_name = #{ruleName},</if>" +
            " <if test=\"updateTime != null\">update_time = #{updateTime},</if>" +
            " </set>" +
            " where id = #{id}" +
            "</script>")
    int updateTemplateRuleById(UpdateTemplateRuleReq t);

    /**
     *@description 根据id，租户id获取策略详情
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/12 16:05
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleResp
     */
    @Select("select id, package_id as packageId, tenant_id as tenantId, type, json, create_time as createTime," +
            " update_time as updateTime, rule_name as ruleName from template_rule where id = #{id} and tenant_id = #{tenantId}")
    TemplateRuleResp getTemplateRuleById(@Param("id") Long id, @Param("tenantId") Long tenantId);
}
