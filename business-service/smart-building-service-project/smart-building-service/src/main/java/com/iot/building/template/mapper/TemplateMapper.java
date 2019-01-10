package com.iot.building.template.mapper;

import com.iot.building.template.domain.Template;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.SceneTemplateResp;
import com.iot.building.template.vo.rsp.TemplateResp;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface TemplateMapper {

    /**
     * 	根据 templateId 获取 template
     * @param templateId
     * @return
     */
    @Select({
            "select",
            "*",
            "from template t",
            "where t.id = #{templateId}"
    })
    public Template getTemplateById(@Param("templateId") Long templateId);


    /**
     * 	根据 模板名(模糊查询)、templateType 查询获取 TemplateVO列表
     *
     * @param name
     * @return
     */
//    @SelectProvider(type = TemplateSqlProvider.class, method = "findTemplateList")
//    public List<TemplateResp> findTemplateList(@Param("name") String name, @Param("templateType") String templateType,
//                                               @Param("tenantId") Long tenantId, @Param("locationId") Long locationId);

    /**
     * 	根据 模板名(等号查询)、templateType、tenantId、templateId 查询数量
     *
     * @param name
     * @return
     */
    @SelectProvider(type = TemplateSqlProvider.class, method = "getCountByNameAndTemplateTypeAndTemplateId")
    public int getCountByNameAndTemplateTypeAndTemplateId(@Param("name") String name, @Param("templateType") String templateType,
                                                          @Param("tenantId") Long tenantId,@Param("orgId") Long orgId, @Param("templateId") Long templateId);

    /**
     * 	插入模板
     *
     * @param template
     * @return
     */
    @Insert({
            "insert into template (",
            "product_id,",
            "location_id,",
            "space_id,",
            "name,",
            "template_type,",
            "create_time,",
            "update_time,",
            "create_by,",
            "update_by,",
            "deploy_id,",
            "tenant_id,",
            "org_id,",
            "shortcut,silence_status)",
            "values (#{productId}, ",
            "#{locationId},",
            "#{spaceId},",
            "#{name},",
            "#{templateType},",
            "#{createTime},",
            "#{updateTime},",
            "#{createBy},",
            "#{updateBy},",
            "#{deployId},",
            "#{tenantId},",
            "#{orgId},",
            "#{shortcut},#{silenceStatus})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Template template);

    /**
     * 	更新模板
     *
     * @param template
     */
    @UpdateProvider(type = TemplateSqlProvider.class, method = "updateTemplate")
    public void updateTemplate(@Param("template") Template template);

    /**
     * 	删除模板
     *
     * @param templateId
     * @return
     */
    @Delete({
            "delete from template",
            "where id = #{templateId}"
    })
    public void delTemplate(@Param("templateId") Long templateId);

    /**
     * 	根据 productId获取 Template(理论上 一个productId 只有一条记录)
     *
     * @param productId
     * @param tenantId
     * @return
     */
    @Select({
            "select",
            "*",
            "from template",
            "where product_id = #{productId} and tenant_id = #{tenantId} and org_id=#{orgId}"
    })
    public Template getByProductId(@Param("productId") Long productId,@Param("orgId") Long orgId, @Param("tenantId") Long tenantId);


    /**
     * 	根据 templateId 获取 情景模板
     * @param templateId
     * @return
     */
    @Select({
            "select",
            "t.id AS templateId,",
            "t.`name` AS name,",
            "t.deploy_id AS deployId,",
            "t.shortcut AS shortcut,",
            "t.silence_status AS silenceStatus,",
            "t.org_id AS orgId,",
            "t.tenant_id AS tenantId,",
            "t.location_id AS locationId",
            "from template t",
            "where t.id = #{templateId}"
    })
    public SceneTemplateResp getTemplate(@Param("templateId") Long templateId);

    /**
     * 	统计 模板名 数量
     *
     * @param name	模板名
     * @return
     */
    @Select({
            "select",
            "count(1)",
            "from template",
            "where name = #{name} and tenant_id = #{tenantId} and org_id=#{orgId}"
    })
    public int countByTemplateName(@Param("name") String name,@Param("tenantId") Long tenantId,@Param("orgId") Long orgId);
    
    /**
     * 	根据 模板名(模糊查询)、templateType 查询获取 TemplateVO列表
     *
     * @return
     */
    @SelectProvider(type = TemplateSqlProvider.class, method = "findSpaceTemplateList")
    public List<TemplateResp> findSpaceTemplateList(@Param("templateReq") TemplateReq templateReq);
    
    @SelectProvider(type = TemplateSqlProvider.class, method = "findTemplateList")
    public List<TemplateResp> findTemplateList(@Param("templateReq") TemplateReq templateReq);
   
    @SelectProvider(type = TemplateSqlProvider.class, method = "findSameNameOrDeployTemplate")
    public TemplateResp findSameNameOrDeployTemplate(@Param("templateReq") TemplateReq templateReq);
}
