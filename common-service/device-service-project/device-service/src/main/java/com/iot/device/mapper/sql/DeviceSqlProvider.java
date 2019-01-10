package com.iot.device.mapper.sql;

import com.iot.device.comm.utils.ListUtils;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceStatus;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.model.ota.OtaFileInfo;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:53 2018/4/17
 * @Modify by:
 */
public class DeviceSqlProvider {
	
    public String selectDeviceListByIsDirectDeviceAndVenderCode(@Param("isDirectDevice") Integer isDirectDevice,
    		                                                    @Param("tenantId") Long tenantId,
    		                                                    @Param("locationId") Long locationId,@Param("venderFlag") String venderFlag) {
        StringBuilder sql = new StringBuilder();


        sql.append(" select ");

        sql.append(" p1.id as id, " +
                "p1.uuid as uuid, " +
                "p1.name as name, " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p2.vender_flag AS venderFlag," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
                
        );

        sql.append(" ,p2.type as businessType ");

        sql.append(" ,ds.online_status as onlineStatus ");

        sql.append(" ,ds.active_status as activeStatus ");

        sql.append(" ,ds.on_off as switchStatus ");


        sql.append(" FROM "+ Device.TABLE_NAME+" as p1 LEFT JOIN "+ DeviceType.TABLE_NAME+" p2 ON (p2.id = p1.device_type_id) ");

        //关联状态表
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");

        sql.append(" WHERE p1.is_direct_device=#{isDirectDevice} ");

        sql.append(" and  p1.tenant_id = #{tenantId}");
        
        if (locationId != 0L) {
        	sql.append(" and  p1.location_id = #{locationId}");
		}
        if (org.apache.commons.lang.StringUtils.isNotBlank(venderFlag)) {
        	sql.append(" and  p2.vender_flag = #{venderFlag}");
        }

        return sql.toString();
    }


    public String selectDeviceListByParentId(@Param("parentId") String parentId){

        StringBuilder sql = new StringBuilder();
        sql.append(" select ");

        sql.append("d.id, " +
                "d.uuid as deviceId, " +
                "d.name, " +
                "d.mac as macAddress, " +
                "d.ip as ipAddress, " +
                "d.icon as deviceIcon, " +
                "d.is_direct_device isDirectDevice, " +
                "d.parent_id as parentId, " +
                "d.reset_random as resetRandom ");

        sql.append(" ,ds.online_status as onlineStatus ");

        sql.append(" ,ds.active_status as activeStatus ");

        sql.append(" ,ds.on_off as switchStatus ");

        sql.append(" from "+Device.TABLE_NAME+" AS d  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (d.uuid = ds.device_id) ");

        sql.append(" where d.parent_id = #{parentId} ");

        return sql.toString();
    }

    public String selectDirectDevicePage(DevicePageReq pageReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.name as deviceName, " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");

        sql.append( " WHERE " +
                " p1.is_direct_device = 1 ");

        if (pageReq != null) {
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
            if (pageReq.getTenantId()!=null){
                sql.append(" AND p1.tenant_id =#{tenantId} ");
            }
        }



        return sql.toString();
    }
    
    public String selectDirectDevicePageToCenter(DevicePageReq pageReq){

    	StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.icon as icon, " +
                "p1.name, " +
                "p1.mac as mac," +
                "p1.hw_version as hwVersion," +
                "p1.version," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom," + 
                "CONCAT(IFNULL(p1.parent_id,''),p1.uuid) as sort "  
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as devType ");
        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id AND p1.tenant_id = ds.tenant_id) ");
        sql.append(" LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p1.device_type_id = dt.id) ");
        sql.append( " WHERE p1.name != '' ");
        
        if (pageReq != null) {
        	if (pageReq.getTenantId()!=null){
        		sql.append(" AND p1.tenant_id =#{tenantId} ");
        	} 
        	if (pageReq.getLocationId()!=null){
        		sql.append(" AND p1.location_id =#{locationId} ");
        	}
        	if (pageReq.getOrgId()!=null){
        		sql.append(" AND p1.org_id =#{orgId} ");
        	}
        	if (!StringUtils.isEmpty(pageReq.getDeviceId())){
        		sql.append(" AND (p1.parent_id =#{deviceId} OR p1.uuid = #{deviceId}) ");
        	}
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
            if (pageReq.getDeviceTypeId()!=null){
        		sql.append(" AND p1.device_type_id =#{deviceTypeId} ");
        	}
            if (pageReq.getBusinessTypeId()!=null){
        		sql.append(" AND p1.business_type_id =#{businessTypeId} ");
        	}
            if (pageReq.getProductId()!=null){
        		sql.append(" AND p1.product_id =#{productId} ");
        	}
            if (pageReq.getIsCheckVersion()!=null){
        		sql.append(" AND ((p1.hw_version != p1.version or p1.version is null or p1.version = '') and p1.hw_version != '') ");
        	}
        }

        sql.append( " order by sort");

        return sql.toString();
    }

    public String selectUnDirectDevicePage(DevicePageReq pageReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.name , " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as businessType ");

        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
        sql.append(" LEFT JOIN "+DeviceType.TABLE_NAME+" AS dt ON (p1.device_type_id = dt.id)");

        sql.append(" where 1=1 " );
        sql.append(" AND  p1.is_direct_device = 0 ");

        if (pageReq != null) {
            if (!StringUtils.isEmpty(pageReq.getDeviceId())) {
                sql.append(" AND p1.parent_id = #{deviceId} ");
            }
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
            if (pageReq.getTenantId()!=null) {
                sql.append(" AND  p1.tenant_id = #{tenantId}");

            }
        }

        return sql.toString();
    }

    public String selectAllUnDirectDevicePage(DevicePageReq pageReq){

    	StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.name , " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as businessType ");

        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
        sql.append(" LEFT JOIN "+DeviceType.TABLE_NAME+" AS dt ON (p1.device_type_id = dt.id)");

        sql.append(" where 1=1 " );
        sql.append(" AND  p1.is_direct_device = 0 ");
        
        if (pageReq != null) {
            if (!StringUtils.isEmpty(pageReq.getDeviceId())) {
                sql.append(" AND p1.parent_id = #{deviceId} ");
            }
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
            if (pageReq.getTenantId()!=null) {
                sql.append(" AND  p1.tenant_id = #{tenantId}");
            }
            if (!CollectionUtils.isEmpty(pageReq.getMountDeviceList())) {
            	String deviceIdsStr = ListUtils.changeListToStrBySeparator(pageReq.getMountDeviceList(), ",");
                sql.append(" " +
                           "AND p1.uuid NOT IN (" + deviceIdsStr + ")" +
                           " ");
			}
        }

        return sql.toString();
    }


    public String findDeviceListByIds(@Param("deviceIds") String deviceIds) {
        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id, " +
                "p1.uuid as deviceId, " +
                "p1.name as deviceName, " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as businessType ");

        sql.append(" from " + Device.TABLE_NAME + " AS p1  ");
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
        sql.append(" LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p1.device_type_id = dt.id)");

        sql.append(" where 1=1 ");

        if (!StringUtils.isEmpty(deviceIds)) {

            sql.append(" " +
                    "AND p1.id IN (" + deviceIds + ")" +
                    "");
        }

        return sql.toString();
    }
    
    
    public String getCountByDeviceIdsAndBusinessTypesAndSwitch(@Param("req") DeviceBusinessTypeIDSwitchReq req) {
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT count(d.uuid)  ");
    	
    	sql.append(" from device d LEFT JOIN device_business_type dbt ON d.business_type_id=dbt.id LEFT JOIN");
    	sql.append(" device_status ds ON d.uuid=ds.device_id where 1=1 ");
    	
    	if(req.getSwitchStatus() !=null) {
    		sql.append(" and ds.on_off="+ req.getSwitchStatus());
    	}
//    	String businessTypes="";
//    	if (!CollectionUtils.isEmpty(req.getBusinessTypeList())) {
//    		businessTypes=inStr(req.getBusinessTypeList());
//    		sql.append(" and dbt.business_type IN (" + businessTypes + ")" + "");
//    	}
    	String deviceIds="";
    	if (!CollectionUtils.isEmpty(req.getDeviceIds())) {
    		deviceIds=inStr(req.getDeviceIds());
    		sql.append(" and d.uuid IN (" + deviceIds + ")" + "");
    	}
    	if(req.getTenantId() !=null) {
    		sql.append(" and d.tenant_id="+ req.getTenantId());
    		sql.append(" and dbt.tenant_id="+ req.getTenantId());
    		sql.append(" and ds.tenant_id="+ req.getTenantId());
    	}
    	if(req.getLocationId() !=null) {
    		sql.append(" and d.location_id="+ req.getLocationId());
    	}
    	return sql.toString();
    }
    
    public String getCountByDeviceIdsAndTypesAndSwitch(@Param("req") DeviceBusinessTypeIDSwitchReq req) {
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT count(d.uuid)  ");
    	
    	sql.append(" from device d LEFT JOIN device_type dt ON d.device_type_id=dt.id LEFT JOIN");
    	sql.append(" device_status ds ON d.uuid=ds.device_id where 1=1 ");
    	
    	if(req.getSwitchStatus() !=null) {
    		sql.append(" and ds.on_off="+ req.getSwitchStatus());
    	}
    	String businessTypes="";
    	if (!CollectionUtils.isEmpty(req.getDeviceTypeList())) {
    		businessTypes=inStr(req.getDeviceTypeList());
    		sql.append(" and dt.type IN (" + businessTypes + ")" + "");
    	}
    	String deviceIds="";
    	if (!CollectionUtils.isEmpty(req.getDeviceIds())) {
    		deviceIds=inStr(req.getDeviceIds());
    		sql.append(" and d.uuid IN (" + deviceIds + ")" + "");
    	}
    	if(req.getTenantId() !=null) {
    		sql.append(" and d.tenant_id="+ req.getTenantId());
    		sql.append(" and dt.tenant_id="+ req.getTenantId());
    		sql.append(" and ds.tenant_id="+ req.getTenantId());
    	}
    	if(req.getLocationId() !=null) {
    		sql.append(" and d.location_id="+ req.getLocationId());
    	}
    	return sql.toString();
    }
   
    
    
    public String findDeviceByCondition(@Param("req") DeviceBusinessTypeIDSwitchReq req) {
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT   " + 
    			"   d.id," + 
    			"	d.business_type_id AS businessTypeId, " + 
    			"	dbt.business_type AS businessType, " + 
    			"	d.create_time AS createTime, " + 
    			"	d.device_type_id AS deviceTypeId, " + 
    			"	dt.type as devType, " + 
    			"	d.icon, " + 
    			"	d.ip, " + 
    			"	d.is_direct_device AS isDirectDevice, " + 
    			"	d.last_update_date AS lastUpdateDate, " + 
    			"	d.location_id AS locationId, " + 
    			"	d.mac, " + 
    			"	d.`name` , " + 
    			"	d.parent_id AS parentId, " + 
    			"	d.product_id AS productId, " + 
    			"	d.tenant_id AS tenantId, " + 
    			"	d.uuid AS deviceId, " + 
    			"	d.ssid, " + 
    			"	d.version, " + 
    			"	ds.on_off AS switchStatus ");
    	
    	sql.append(" FROM device d LEFT JOIN device_business_type dbt ON d.device_type_id = dbt.id");
    	sql.append(" LEFT JOIN device_type dt ON d.device_type_id = dt.id ");
    	sql.append(" LEFT JOIN device_status ds ON d.id = ds.device_id where 1=1 ");
    	
    	if(req.getSwitchStatus() !=null) {
    		sql.append(" and ds.on_off="+ req.getSwitchStatus());
    	}
    	if(req.getIsDirectDevice() !=null) {
    		sql.append(" and d.is_direct_device="+ req.getIsDirectDevice());
    	}
    	String businessTypes="";//业务类型
    	if (!CollectionUtils.isEmpty(req.getBusinessTypeList())) {
    		businessTypes=inStr(req.getBusinessTypeList());
    		sql.append(" and dbt.business_type IN (" + businessTypes + ")" + "");
    	}
    	String deviceIds="";//设备ID
    	if (!CollectionUtils.isEmpty(req.getDeviceIds())) {
    		deviceIds=inStr(req.getDeviceIds());
    		sql.append(" and d.uuid IN (" + deviceIds + ")" + "");
    	}
    	String types="";//功能类型
    	if (!CollectionUtils.isEmpty(req.getDeviceTypeList())) {
    		types=inStr(req.getDeviceTypeList());
    		sql.append(" and dt.type IN (" + types + ")" + "");
    	}
    	if(req.getTenantId() !=null) {
    		sql.append(" and d.tenant_id="+ req.getTenantId());
    		sql.append(" and dt.tenant_id="+ req.getTenantId());
    		sql.append(" and ds.tenant_id="+ req.getTenantId());
    	}
    	if(req.getLocationId() !=null) {
    		sql.append(" and d.location_id="+ req.getLocationId());
    	}
    	
    	return sql.toString();
    }
    
    private String inStr(List<String> list) {
    	String inStr="";
    	for(int i=0;i<list.size();i++) {
    		if(i==(list.size()-1)) {
    			inStr+="'"+list.get(i)+"'";
			}else {
				inStr+="'"+list.get(i)+"'"+",";
			}
    	}
    	return inStr;
    }


    public String findDevPageByProductId(@Param("productId") Long productId) {
        StringBuilder sql = new StringBuilder();

        sql.append("select ss.* ");

        sql.append(" ,ds.online_status as onlineStatus ");

        sql.append(" ,ds.active_status as activeStatus ");

        sql.append(" ,ds.on_off as switchStatus ");


        sql.append(" from ( ");

        sql.append(" select ");

        sql.append(" p1.id, " +
                "p1.uuid as deviceId, " +
                "p1.name as deviceName, " +
                "p1.icon as icon, " +
                "p1.mac as mac," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom "
        );
        sql.append(" ,dt.type as devType ");

        sql.append(" from " + Device.TABLE_NAME + " p1 ");

        sql.append(" LEFT JOIN " + Product.TABLE_NAME + " AS p ON (p1.product_id = p.id) ");


        sql.append(" LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p.device_type_id = dt.id)");
        sql.append(" WHERE 1=1 ");

        if (productId != null) {

            sql.append(" " +
                    "AND p1.product_id =#{productId}");
        }
        // 关联 状态表 获取状态信息
        sql.append(" ) as ss ");

        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (ss.deviceId = ds.device_id) ");


        return sql.toString();
    }
    
    public String selectAllDeviceToCenter(DevicePageReq pageReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.icon as icon, " +
                "p1.name, " +
                "p1.mac as mac," +
                "p1.hw_version as hwVersion," +
                "p1.version," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom," + 
                "CONCAT(IFNULL(p1.parent_id,''),p1.uuid) as sort "  
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as devType ");
//        sql.append(" ,ds.online_status as onlineStatus ");
//        sql.append(" ,ds.active_status as activeStatus ");
//        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
        sql.append(" LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p1.device_type_id = dt.id) ");
        sql.append( " WHERE p1.name != '' ");
        
        if (pageReq != null) {
        	if (pageReq.getTenantId()!=null){
        		sql.append(" AND p1.tenant_id =#{tenantId} ");
        	} 
        	if (pageReq.getLocationId()!=null){
        		sql.append(" AND p1.location_id =#{locationId} ");
        	}
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
        }

        sql.append( " order by sort");

        return sql.toString();
    }
    
    public String getOtaFileList(@Param("pageReq") OtaPageReq pageReq){

        StringBuilder sql = new StringBuilder();

        sql.append("select DISTINCT");
        sql.append(" ofi.product_id as productId, " +
                "pd.product_name as productName," +
                "ofi.version as version, " + 
                "pd.model as model " 
        );
        sql.append(" from " + OtaFileInfo.TABLE_NAME+" AS ofi " );
        sql.append(" JOIN " + Device.TABLE_NAME + " AS dev ON ofi.product_id = dev.product_id ");
        sql.append(" AND ofi.tenant_id = dev.tenant_id " );
        sql.append(" AND ofi.location_id = dev.location_id " );
        sql.append(" JOIN " + Product.TABLE_NAME + " AS pd ON ofi.product_id = pd.id ");
//        sql.append(" AND ofi.tenant_id = pd.tenant_id " );
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
    
    public String getGatewayAndSubDeviceList(DevicePageReq pageReq){

    	StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append(" p1.id as id, " +
                "p1.uuid as deviceId, " +
                "p1.icon as icon, " +
                "p1.name, " +
                "p1.mac as mac," +
                "p1.hw_version as hwVersion," +
                "p1.version," +
                "p1.tenant_id AS tenantId," +
                "p1.create_time AS createTime," +
                "p1.last_update_date AS lastUpdateDate," +
                "p1.is_direct_device AS isDirectDevice," +
                "p1.parent_id AS parentId," +
                "p1.device_type_id AS deviceTypeId," +
                "p1.ip as ip," +
                "p1.reality_id as realityId," +
                "p1.extra_name as extraName," +
                "p1.product_id AS productId," +
                "p1.reset_random AS resetRandom " 
        );
        sql.append(" ,ds.online_status as onlineStatus ");
        sql.append(" ,ds.active_status as activeStatus ");
        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" ,dt.type as devType ");
//        sql.append(" ,ds.online_status as onlineStatus ");
//        sql.append(" ,ds.active_status as activeStatus ");
//        sql.append(" ,ds.on_off as switchStatus ");
        sql.append(" from "+Device.TABLE_NAME+" AS p1  " );
        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
        sql.append(" LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p1.device_type_id = dt.id) ");
        sql.append( " WHERE p1.name != '' ");
        
        if (pageReq != null) {
        	if (pageReq.getTenantId()!=null){
        		sql.append(" AND p1.tenant_id =#{tenantId} ");
        	} 
        	if (pageReq.getOrgId()!=null){
        		sql.append(" AND p1.org_id =#{orgId} ");
        	}
        	if (pageReq.getLocationId()!=null){
        		sql.append(" AND p1.location_id =#{locationId} ");
        	}
            if (!StringUtils.isEmpty(pageReq.getDeviceName())) {
                sql.append(" AND p1.name like CONCAT(CONCAT('%', #{deviceName}), '%') ");
            }
            if (pageReq.getDeviceId()!=null){
        		sql.append(" AND (p1.parent_id = #{deviceId} OR p1.uuid = #{deviceId})");
        	} 
        }
        sql.append(" order by p1.is_direct_device desc ");
        return sql.toString();
    }

    public String getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq){

        StringBuilder sql = new StringBuilder();
        sql.append(" select ");

        sql.append("id, " +
                "uuid, " +
                "name ");

        sql.append(" from " + Device.TABLE_NAME );
        
        sql.append(" where 1 = 1 ");
        
        if (commDeviceInfoReq != null) {
        	if (commDeviceInfoReq.getTenantId()!=null){
        		sql.append(" AND tenant_id =#{tenantId} ");
        	} 
        	if (commDeviceInfoReq.getOrgId()!=null){
        		sql.append(" AND org_id =#{orgId} ");
        	} 
        	if (commDeviceInfoReq.getLocationId()!=null){
        		sql.append(" AND location_id =#{locationId} ");
        	}
            if (commDeviceInfoReq.getParentId()!=null){
        		sql.append(" AND parent_id = #{parentId} ");
        	} 
        }

        return sql.toString();
    }
}
