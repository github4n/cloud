package com.iot.building.device.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.iot.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

import com.iot.building.device.vo.DeviceBusinessTypeReq;

/**
 * @Author: ljh
 * @Descrpiton:
 * @Date: 16:53 2018/5/16
 * @Modify by:
 */
public class DeviceBusinessTypeSqlProvider {

	public String selectDeviceBusinessTypeId(Long orgId, String businessType){
		
        StringBuilder sql = new StringBuilder();

        sql.append(" select ");

        sql.append(" id ," +
                   " business_type as businessType ," +
        		   " description "
        );
        
        sql.append(" FROM device_business_type ");
        
        sql.append(" WHERE business_type = #{businessType} ");
        
        if(orgId !=null) {
        	sql.append(" AND org_id = #{orgId} ");
        }
        
        return sql.toString();
    }
	
	public String selectDeviceBusinessTypeList(@Param("orgId") Long orgId, @Param("tenantId") Long tenantId, @Param("name") String name){
		
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");

        sql.append(" dbt.product_id AS productId, " + 
        		"	dbt.id , " + 
//        		"	p.device_type_id AS deviceTypeId, " + 
        		"	dbt.tenant_id AS tenantId, " + 
        		"	dbt.id AS businessTypeId, " + 
        		"	dbt.business_type AS businessType, " + 
        		"	dbt.is_home_show AS isHomeShow, " + 
        		"	dbt.description " 
        );
        
        sql.append(" FROM device_business_type dbt ");
        
//        sql.append(" LEFT JOIN product p ON dbt.product_id = p.id ");
        
//        sql.append(" AND dbt.tenant_id = p.tenant_id ");
        
        sql.append(" WHERE 1=1 ");
        
//        if(model !=null && StringUtils.isNotBlank(model)) {
//        	sql.append(" AND p.model = #{model} ");
//        }
        
        if(tenantId !=null) {
        	sql.append(" AND dbt.tenant_id = #{tenantId} ");
        }
        if(orgId !=null) {
        	sql.append(" AND dbt.org_id = #{orgId} ");
        }
        if(StringUtil.isNotEmpty(name)) {
            sql.append(" AND dbt.business_type like CONCAT(CONCAT('%', #{name}), '%') ");
        }
        
        sql.append(" order by CONVERT (dbt.business_type USING gbk) COLLATE gbk_chinese_ci ASC ");
        
        return sql.toString();
    }

    
	public String updateBusinessType(@Param("deviceBusinessTypeReq") DeviceBusinessTypeReq deviceBusinessTypeReq){
		 BEGIN();
	        UPDATE("device_business_type");

	        if (deviceBusinessTypeReq.getUpdateTime() != null) {
	        	 SET(" update_time = #{deviceBusinessTypeReq.updateTime}");
            }
            if (StringUtils.isNotBlank(deviceBusinessTypeReq.getBusinessType())) {
            	SET("business_type = #{deviceBusinessTypeReq.businessType}");
            }
            if (StringUtils.isNotBlank(deviceBusinessTypeReq.getDescription())) {
            	SET("description = #{deviceBusinessTypeReq.description}");
            }
            if (deviceBusinessTypeReq.getUpdateBy() != null) {
            	SET("update_by = #{deviceBusinessTypeReq.updateBy}");
            }
            if (deviceBusinessTypeReq.getIsDeleted() != null) {
            	SET("is_deleted = #{deviceBusinessTypeReq.isDeleted}");
            }
            if (deviceBusinessTypeReq.getProductId() != null) {
            	SET("product_id = #{deviceBusinessTypeReq.productId}");
            }
	       
	        WHERE("id = #{deviceBusinessTypeReq.id}");

	        return SQL();
		
//        StringBuilder sql = new StringBuilder();
//
//        sql.append(" update device_business_type ");
//
//        if (deviceBusinessTypeReq != null) {
//            sql.append(" set ");
//            if (deviceBusinessTypeReq.getUpdateTime() != null) {
//                sql.append(" update_time = #{updateTime}");
//            }
//            if (StringUtils.isNotBlank(deviceBusinessTypeReq.getBusinessType())) {
//                sql.append(", ");
//                sql.append("business_type = #{businessType}");
//
//            }
//            if (StringUtils.isNotBlank(deviceBusinessTypeReq.getDescription())) {
//                sql.append(", ");
//                sql.append("description = #{description}");
//            }
//            if (deviceBusinessTypeReq.getUpdateBy() != null) {
//                sql.append(", ");
//                sql.append("update_by = #{updateBy}");
//            }
//            if (deviceBusinessTypeReq.getIsDeleted() != null) {
//                sql.append(", ");
//                sql.append("is_deleted = #{isDeleted}");
//            }
//            if (deviceBusinessTypeReq.getProductId() != null) {
//                sql.append(", ");
//                sql.append("product_id = #{productId}");
//            }
//        }
//        return sql.toString();
    }
}
