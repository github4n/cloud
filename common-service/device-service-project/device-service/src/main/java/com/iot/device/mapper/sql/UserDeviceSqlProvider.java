//package com.iot.device.mapper.sql;
//
//import com.iot.device.comm.utils.ListUtils;
//import com.iot.device.model.Device;
//import com.iot.device.model.DeviceStatus;
//import com.iot.device.model.Product;
//import com.iot.device.model.UserDevice;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 16:53 2018/4/17
// * @Modify by:
// */
//public class UserDeviceSqlProvider {
//
//    public String selectUserDeviceListByUserIdAndDeviceIds(@Param("userId") Long userId, @Param("deviceIds") List<String> deviceIds) {
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("select  ");
//        sql.append(" p1.id as id, " +
//                "p1.uuid as deviceId, " +
//                "p1.name, " +
//                "p1.icon as icon, " +
//                "p1.mac as mac," +
//                "p1.tenant_id AS tenantId," +
//                "p1.create_time AS createTime," +
//                "p1.last_update_date AS lastUpdateDate," +
//                "p1.is_direct_device AS isDirectDevice," +
//                "p1.parent_id AS parentId," +
//                "p1.device_type_id AS deviceTypeId," +
//                "p1.ip as ip," +
//                "p1.reality_id as realityId," +
//                "p1.extra_name as extraName," +
//                "device_type_id AS deviceTypeId," +
//                "p1.product_id AS productId," +
//                "p1.reset_random AS resetRandom, " +
//                "p2.password"
//        );
//
//        sql.append(" ,ds.online_status as onlineStatus ");
//
//        sql.append(" ,ds.active_status as activeStatus ");
//
//        sql.append(" ,ds.on_off as switchStatus ");
//
//        sql.append(" FROM " + Device.TABLE_NAME + " as p1 LEFT JOIN " + UserDevice.TABLE_NAME + " p2 ON (p2.device_id = p1.uuid) ");
//
//        //关联状态表
//        sql.append(" LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) ");
//
//        sql.append(" " +
//                "WHERE p2.user_id = #{userId} ");
//
//        if (!CollectionUtils.isEmpty(deviceIds)) {
//
//            String deviceIdsStr = ListUtils.changeListToStrBySeparator(deviceIds, ",");
//            sql.append(" " +
//                    "AND p1.uuid IN (" + deviceIdsStr + ")" +
//                    "");
//        }
//        sql.append(" " +
//                "ORDER BY p2.id DESC " +
//                "");
//
//        return sql.toString();
//    }
//
//
//    public String selectUserDeviceProductListByUserId() {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select  ");
//        sql.append(" p1.id as id, " +
//                "p1.uuid as deviceId, " +
//                "p1.name as deviceName, " +
//                "p1.parent_id AS parentId," +
//                "p1.device_type_id AS deviceTypeId"
//        );
//        sql.append(" ,p.id AS productId ");
//        sql.append(" ,p.product_name AS productName ");
//        sql.append(" from ");
//        sql.append(" " + UserDevice.TABLE_NAME + " as ud ");
//        sql.append(" LEFT JOIN " + Device.TABLE_NAME + " as p1  ON (ud.device_id = p1.uuid) ");
//        sql.append(" LEFT JOIN " + Product.TABLE_NAME + " as p ON (p.id = p1.product_id)  ");
//        sql.append(" " +
//                "WHERE ud.user_id = #{userId} ");
//
//
//        return sql.toString();
//    }
//
//    public String updateeventNotifEnabled() {
//        StringBuilder sql = new StringBuilder();
//        sql.append("update  ")
//                .append(" " + UserDevice.TABLE_NAME + " ")
//                .append(" set event_notify_enabled = #{eventNotifyEnabled} WHERE user_id = #{userId} and device_id = #{deviceId}"
//                );
//        return sql.toString();
//    }
//
//    public String selectEventNotifEnabled() {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select  event_notify_enabled ")
//                .append(" from " + UserDevice.TABLE_NAME + " ")
//                .append(" WHERE user_id = #{userId} and device_id = #{deviceId}"
//                );
//        return sql.toString();
//    }
//
//    public String getRootUserIdByDeviceId(){
//        StringBuilder sql = new StringBuilder();
//        sql.append("select  user_id ")
//                .append(" from " + UserDevice.TABLE_NAME + " ")
//                .append(" WHERE user_type = 'root' and device_id = #{deviceId}");
//        return sql.toString();
//    }
//
//}
