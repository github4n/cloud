package com.iot.building.ota.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import org.apache.ibatis.annotations.Param;

import com.iot.building.ota.entity.OtaFileInfo;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;

public class OtaSqlProvider {

	public String getOtaFileList(@Param("pageReq") OtaPageReq pageReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select DISTINCT");
        sql.append(" id, " +
        		   "product_id as productId, " +
        		   "tenant_id as tenantId,  " + 
        		   "location_id as locationId,  " + 
                   "version " 
        );
        sql.append(" from " + OtaFileInfo.TABLE_NAME+" AS ofi " );
        sql.append(" WHERE 1 = 1 ");
        
        if (pageReq != null) {
        	if (pageReq.getProductId() != null) {
            	sql.append(" " +
                        "AND ofi.product_id =#{pageReq.productId} ");
            }
        	
        	if (pageReq.getTenantId() != null) {
            	sql.append(" " +
                        "AND ofi.tenant_id =#{pageReq.tenantId} ");
    		}
            
        	if (pageReq.getOrgId() != null) {
            	sql.append(" " +
                        "AND ofi.org_id =#{pageReq.orgId} ");
    		}
        	
            if (pageReq.getLocationId() != null) {
            	sql.append(" " +
                        "AND ofi.location_id =#{pageReq.locationId} ");
    		}
        }

        sql.append( " order by ofi.product_id ");

        return sql.toString();
    }
    
    public String findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" id , " +
                "product_id as productId," +
                "version as version," +
                "tenant_id as tenantId," +
                "location_id as locationId," +
                "download_url as downloadUrl," +
                "md5," +
                "create_by as createBy," +
                "create_time as createTime," +
                "update_by as updateBy," + 
                "update_time as updateTime "  
        );
        sql.append(" from " + OtaFileInfo.TABLE_NAME );
        sql.append(" WHERE 1 = 1 ");
        
        if (otaFileInfoReq != null) {
        	if (otaFileInfoReq.getProductId() != null) {
            	sql.append(" " +
                        "AND product_id =#{productId} ");
            }
        	
        	if (otaFileInfoReq.getTenantId() != null) {
            	sql.append(" " +
                        "AND tenant_id =#{tenantId} ");
    		}
        	
        	if (otaFileInfoReq.getOrgId() != null) {
            	sql.append(" " +
                        "AND org_id =#{orgId} ");
    		}
            
            if (otaFileInfoReq.getLocationId() != null) {
            	sql.append(" " +
                        "AND location_id =#{locationId} ");
    		}
        }

        return sql.toString();
    }
    
    public String updateOtaFileInfo(@Param("otaFileInfoReq") OtaFileInfoReq otaFileInfoReq) {
        BEGIN();
        UPDATE("ota_file_info");

        if (otaFileInfoReq.getProductId() != null) {
            SET("product_id = #{otaFileInfoReq.productId}");
        }

        if (otaFileInfoReq.getVersion() != null) {
            SET("version = #{otaFileInfoReq.version}");
        }

        if (otaFileInfoReq.getUpdateBy() != null) {
            SET("update_by = #{otaFileInfoReq.updateBy}");
        }
        
        if (otaFileInfoReq.getDownloadUrl() != null) {
            SET("download_url = #{otaFileInfoReq.downloadUrl}");
        }
        
        if (otaFileInfoReq.getMd5() != null) {
            SET("md5 = #{otaFileInfoReq.md5}");
        }

        SET("update_time = now()");

        WHERE("id = #{otaFileInfoReq.id}");

        return SQL();
    }

}
