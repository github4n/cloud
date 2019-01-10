package com.iot.building.device.mapper;

import com.iot.building.device.util.ListUtils;
import com.iot.device.vo.rsp.ProductResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import java.util.List;

public class DeviceSqlProvider {

    public String selectDevListByDeviceIds(List<String> deviceIds, @Param("isCheckUserNotNull") boolean isCheckUserNotNull, @Param("userId") Long userId) {
        StringBuilder sql = new StringBuilder();

        sql.append("select ss.* ");

        sql.append(" ,ds.online_status as onlineStatus ");

        sql.append(" ,ds.active_status as activeStatus ");

        sql.append(" ,ds.on_off as switchStatus ");

        sql.append(" ,dt.type as devType ");
        
        sql.append(" ,dbt.business_type as businessType ");

        sql.append(" from ( ");

        sql.append(" select ");

        sql.append(" p1.id, " +
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
                "p1.business_type_id AS businessTypeId," +
                "p1.reset_random AS resetRandom "
        );

        sql.append(" from " + "device" + " p1 ");
        if (!CollectionUtils.isEmpty(deviceIds)) {

            String deviceIdsStr = ListUtils.changeListToStrBySeparator(deviceIds, ",");
            sql.append(" " +
                    " WHERE p1.uuid IN (" + deviceIdsStr + ")" +
                    " ");
        }
        // 关联 状态表 获取状态信息
        sql.append(" ) as ss ");

        sql.append(" LEFT JOIN " + "device_status" + " AS ds ON (ss.deviceId = ds.device_id) ");
        sql.append(" LEFT JOIN " + "device_type" + " AS dt ON (ss.deviceTypeId = dt.id)");

        sql.append(" LEFT JOIN " + "user_device" + " AS ud ON (ss.deviceId = ud.device_id)");
        sql.append(" LEFT JOIN " + "device_business_type" + " AS dbt ON (ss.businessTypeId = dbt.id)");

        sql.append(" where 1=1 ");
        if (isCheckUserNotNull) {

            sql.append(" AND ud.user_id is not null");
        }
        if (userId != null && userId > 0) {
            sql.append(" AND ud.user_id =#{userId} ");
        }

        return sql.toString();
    }


    public String listProducts(@Param("productIds") List<Long> productIds) {
        StringBuilder sql = new StringBuilder();



        sql.append("select  t2.device_type_id AS deviceTypeId, " +
                "t2.product_name AS productName, " +
                "t3.type AS deviceTypeName, "+
                "t2.id AS id "
        );

        sql.append("from product t2 LEFT JOIN device_type t3 ON t3.id = t2.device_type_id ");
        String productIdsStr = ListUtils.changeListToStrBySeparator(productIds, ",");
        System.out.println("productIdsStr:"+productIdsStr);
        sql.append(" " +
                " WHERE t2.id IN (" + productIdsStr + ")" +
                " ");
        // 关联 状态表 获取状态信息

        return sql.toString();
    }

}
