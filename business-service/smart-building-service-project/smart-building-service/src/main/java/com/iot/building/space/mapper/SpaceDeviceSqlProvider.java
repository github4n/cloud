package com.iot.building.space.mapper;

import com.iot.building.space.domain.SpaceDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public class SpaceDeviceSqlProvider {

    public String findSpaceIdByDeviceId(Map<String, Object> paramMap){
        StringBuilder sql = new StringBuilder();

        sql.append("select sd.id, sd.device_id as deviceId" +
                " ,sd.space_id AS spaceId ");
        sql.append(" ,sd.device_type_id as deviceCategoryId " +
                " ,sd.position ");
        sql.append(" ,sd.location_id as locationId ");
        sql.append(" from space_device sd");

        if (paramMap.get("userId") != null) {
            sql.append(" left join space s on s.id = sd.space_id");
        }

        sql.append(" where 1=1");

        if (paramMap.get("deviceId") != null) {
            sql.append(" and sd.device_id = #{deviceId}");
        }

        if (paramMap.get("userId") != null) {
            sql.append(" and s.user_id=#{userId}");
        }
        if (paramMap.get("tenantId") != null) {
            sql.append(" and sd.tenant_id=#{tenantId}");
        }


        return sql.toString();
    }

    public String updateByDeviceId(SpaceDevice spaceDevice) {

        StringBuilder sql = new StringBuilder();

        sql.append("update space_device  ");

        if (spaceDevice != null) {
            sql.append(" set ");
            if (spaceDevice.getUpdateTime() != null) {
                sql.append(" update_time = #{lastUpdateDate}");
            }
            if (spaceDevice.getLocationId() != null) {
                sql.append(", ");
                sql.append("location_id = #{locationId}");

            }
            if (spaceDevice.getSpaceId() != null) {
                sql.append(", ");
                sql.append("space_id = #{spaceId}");
            }
            if (spaceDevice.getDeviceCategoryId() != null) {
                sql.append(", ");
                sql.append("device_category_id = #{deviceCategoryId}");
            }
            if (spaceDevice.getPosition() != null) {
                sql.append(", ");
                sql.append("position = #{position}");
            }
            if (spaceDevice.getTenantId() != null) {
                sql.append(", ");
                sql.append("tenant_id = #{tenantId}");
            }

            if (spaceDevice.getDeviceTypeId() != null) {
                sql.append(", ");
                sql.append(" device_type_id = #{deviceTypeId}");
            }
        }

        sql.append(" where device_id = #{deviceId}");


        return sql.toString();
    }

    public String findSpaceDevBySpaceDevIds(@Param("tenantId") Long tenantId, List<Long> spaceDevIds) {
        StringBuilder sql = new StringBuilder();

        sql.append("select *  ");
        sql.append("from space_device  ");
        sql.append("where 1 = 1  ");
        if (tenantId != null) {
            sql.append(" and tenant_id = #{tenantId} ");
        }
        if (spaceDevIds != null) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < spaceDevIds.size(); i++) {
                str.append(spaceDevIds.get(i));
                if (i < spaceDevIds.size() - 1) {
                    str.append(",");
                }
            }
            if (str != null) {
                sql.append(" and id in(" + str + ")");
            }
        }
        return sql.toString();
    }

}
