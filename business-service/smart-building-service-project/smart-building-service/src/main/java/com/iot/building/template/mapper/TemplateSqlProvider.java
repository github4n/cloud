package com.iot.building.template.mapper;

import com.iot.building.template.domain.Template;
import com.iot.building.template.vo.req.TemplateReq;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import java.util.List;
import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class TemplateSqlProvider {

//    public String findTemplateList(@Param("name") String name, @Param("templateType") String templateType, @Param("tenantId") Long tenantId,@Param("locationId") Long locationId) {
//        BEGIN();
//
//        SELECT("t.id, "
//        		+ "name, "
//        		+ "product_id as productId, "
//        		+ "location_id as locationId, "
//        		+ "tenant_id as tenantId, "
//        		+ "space_id as spaceId, "
//        		+ "template_type as templateType, "
//        		+ "create_time as createTime, "
//        		+ "update_time as updateTime, "
//        		+ "deploy_id as deployId, "
//        		+ "create_by as createBy, "
//        		+ "update_by as updateBy");
//
//        FROM("template t");
//
//        if (StringUtils.isNotBlank(name)) {
//            WHERE("t.`name` like CONCAT(CONCAT('%',#{name}),'%')");
//        }
//
//        if (StringUtils.isNotBlank(templateType)) {
//            WHERE("t.template_type = #{templateType}");
//        }
//        if (locationId !=null) {
//        	WHERE("t.location_id = #{locationId}");
//        }
//
//        WHERE("t.tenant_id = #{tenantId}");
//
//        return SQL();
//    }

    public String getCountByNameAndTemplateTypeAndTemplateId(@Param("name") String name, @Param("templateType") String templateType,
                                                             @Param("tenantId") Long tenantId, @Param("orgId") Long orgId,@Param("templateId") Long templateId){
        BEGIN();

        SELECT("count(*)");

        FROM("template t");

        if (StringUtils.isNotBlank(name)) {
            WHERE("t.`name` = #{name}");
        }

        if (StringUtils.isNotBlank(templateType)) {
            WHERE("t.template_type = #{templateType}");
        }

        if(templateId != null){
            WHERE("t.id != #{templateId}");
        }

        WHERE("t.tenant_id = #{tenantId}");
        WHERE("t.org_id=#{orgId}");

        return SQL();
    }

    public String updateTemplate(@Param("template") Template template){
        BEGIN();

        UPDATE("template");

        if (template.getProductId() != null) {
            SET("product_id = #{productId}");
        }

        if (template.getLocationId() != null) {
            SET("location_id = #{template.locationId}");
        }

        if (template.getSpaceId() != null) {
            SET("space_id = #{template.spaceId}");
        }

        if (StringUtils.isNotBlank(template.getName())) {
            SET("name = #{template.name}");
        }

        if (StringUtils.isNotBlank(template.getTemplateType())) {
            SET("template_type = #{template.templateType}");
        }

        if (template.getUpdateBy() != null) {
            SET("update_by = #{template.updateBy}");
        }
        
        if (template.getDeployId() != null) {
            SET("deploy_id = #{template.deployId}");
        }
        
        if (template.getShortcut() != null) {
        	SET("shortcut = #{template.shortcut}");
        }
        
        if (template.getSilenceStatus() != null) {
        	SET("silence_status = #{template.silenceStatus}");
        }

        SET("update_time = now()");

        WHERE("id = #{template.id}");

        return SQL();
    }
    
    public String findSpaceTemplateList(@Param("templateReq") TemplateReq templateReq){

    	StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append("t.id, "
        		+ "t.name, "
        		+ "t.product_id as productId, "
        		+ "t.location_id as locationId, "
        		+ "t.tenant_id as tenantId, "
        		+ "t.org_id as orgId, "
        		+ "t.space_id as spaceId, "
        		+ "t.template_type as templateType, "
        		+ "t.create_time as createTime, "
        		+ "t.update_time as updateTime, "
        		+ "t.create_by as createBy, "
        		+ "t.update_by as updateBy "
        );

        sql.append(" from template t ");

        sql.append(" where 1=1 " );
        
        if (templateReq != null) {
        	if (!StringUtils.isEmpty(templateReq.getName())) {
        		sql.append("AND t.name like CONCAT(CONCAT('%', #{templateReq.name}), '%')   ");
        	}
        	if (templateReq.getTenantId()!=null) {
        		sql.append(" AND  t.tenant_id = #{templateReq.tenantId}");
        	}
        	if (templateReq.getOrgId()!=null) {
        		sql.append(" AND  t.org_id = #{templateReq.orgId}");
        	}
        	if (!CollectionUtils.isEmpty(templateReq.getTemplateList())) {
        		String templatesStr = changeListToStrBySeparator(templateReq.getTemplateList(), ",");
              if (templateReq.getMountLogo() == 0) {
                    sql.append(" " +
                               "AND t.id NOT IN (" + templatesStr + ")" +
                               " ");
    			}else {
    				sql.append(" " +
                            "AND t.id IN (" + templatesStr + ")" +
                            " ");
				}
            }else if (CollectionUtils.isEmpty(templateReq.getTemplateList())) {
            	if (templateReq.getMountLogo() == 1) {
                    sql.append(" " +
                               "AND t.id IN (null)" +
                               " ");
    			}
			}
        }
        return sql.toString();
    }
    
    public String findTemplateList(@Param("templateReq") TemplateReq templateReq){
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("select ");
    	sql.append("t.id, "
    			+ "t.name, "
    			+ "t.product_id as productId, "
    			+ "t.location_id as locationId, "
    			+ "t.tenant_id as tenantId, "
    			+ "t.deploy_id as deployId, "
    			+ "t.space_id as spaceId, "
    			+ "t.template_type as templateType, "
    			+ "t.create_time as createTime, "
    			+ "t.update_time as updateTime, "
    			+ "t.create_by as createBy, "
    			+ "t.update_by as updateBy, "
    			+ "t.shortcut as shortcut, "
    			+ "t.silence_status as silenceStatus "
    			);
    	
    	sql.append(" from template t ");
    	
    	sql.append(" where 1=1 " );
    	
    	if (templateReq != null) {
    		if (!StringUtils.isEmpty(templateReq.getName())) {
    			sql.append(" AND t.name like CONCAT(CONCAT('%', #{templateReq.name}), '%')   ");
    		}
    		if (templateReq.getTenantId()!=null) {
    			sql.append(" AND  t.tenant_id = #{templateReq.tenantId}");
    		}
    		if (templateReq.getLocationId()!=null) {
    			sql.append(" AND  t.location_id = #{templateReq.locationId}");
    		}
    		if (templateReq.getDeployId()!=null) {
    			sql.append(" AND  t.deploy_id = #{templateReq.deployId}");
    		}
    		if (templateReq.getOrgId()!=null) {
    			sql.append(" AND  t.org_id = #{templateReq.orgId}");
    		}
    		if (StringUtils.isNotBlank(templateReq.getTemplateType())) {
    			sql.append(" AND  t.template_type = #{templateReq.templateType}");
    		}
    		if (templateReq.getShortcut() !=null) {
    			sql.append(" AND  t.shortcut = #{templateReq.shortcut}");
    		}
    		if (!CollectionUtils.isEmpty(templateReq.getTemplateList())) {
    			String templatesStr = changeListToStrBySeparator(templateReq.getTemplateList(), ",");
    			if (templateReq.getMountLogo() == 0) {
    				sql.append(" " +
    						"AND t.id NOT IN (" + templatesStr + ")" +
    						" ");
    			}else {
    				sql.append(" " +
    						"AND t.id IN (" + templatesStr + ")" +
    						" ");
    			}
    		}
//    		else if (CollectionUtils.isEmpty(templateReq.getTemplateList())) {
//    			if (templateReq.getMountLogo() == 1) {
//    				sql.append(" " +
//    						"AND t.id IN (null)" +
//    						" ");
//    			}
//    		}
    	}
    	sql.append(" order by CONVERT (t.name USING gbk) COLLATE gbk_chinese_ci ASC");
    	return sql.toString();
    }
    
    public String findSameNameOrDeployTemplate(@Param("templateReq") TemplateReq templateReq){
    	StringBuilder sql = new StringBuilder();
    	sql.append("SELECT ");
    	sql.append("t.id, "
    			+ "t.name, "
    			+ "t.product_id as productId, "
    			+ "t.location_id as locationId, "
    			+ "t.tenant_id as tenantId, "
    			+ "t.deploy_id as deployId, "
    			+ "t.space_id as spaceId, "
    			+ "t.template_type as templateType, "
    			+ "t.create_time as createTime, "
    			+ "t.update_time as updateTime, "
    			+ "t.create_by as createBy, "
    			+ "t.update_by as updateBy, "
    			+ "t.shortcut as shortcut "
    			);
    	sql.append(" FROM template t ");
    	sql.append(" WHERE 1=1 AND t.name=#{templateReq.name}" );//OR t.deploy_id = #{templateReq.deployId}
    	if (templateReq.getTenantId()!=null) {
			sql.append(" AND  t.tenant_id = #{templateReq.tenantId}");
		}
		if (templateReq.getLocationId()!=null) {
			sql.append(" AND  t.location_id = #{templateReq.locationId}");
		}
		if (templateReq.getOrgId()!=null) {
			sql.append(" AND  t.org_id = #{templateReq.orgId}");
		}
    	return sql.toString();
    }
    
    /**
     * List转换String
     * @param dataList
     * @param separator
     * @return
     */
    public static<T> String changeListToStrBySeparator(List<T> dataList,String separator){
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (T data : dataList
                 ) {
                Object temp=null;
                if (data instanceof String) {
                    temp = "'"+data+"'";
                }else{
                    temp = data;
                }
                sb.append(temp).append(separator);
            }
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }
    
}
