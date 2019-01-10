package com.iot.device.mapper.sql;

import com.iot.device.model.DeviceState;
import com.iot.device.model.DeviceStatus;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:12 2018/4/25
 * @Modify by:
 */
public class DeviceStateSqlProvider {

    public String selectPropertyName(){
        StringBuilder sql = new StringBuilder();

        sql.append("select ");

        sql.append(" MAX(property_name) AS propertyName ");
        sql.append("from "+ DeviceState.TABLE_NAME+" AS ds ");

        sql.append(" where 1=1 ");

        sql.append(" AND device_id =#{deviceId}");
        sql.append(" AND group_id =#{spaceId}");

        sql.append(" GROUP BY property_name  ");
        return sql.toString();
    }

    public String selectDataReport(){


        StringBuilder sql = new StringBuilder();

        sql.append("select ");

        sql.append(" MAX(property_name) AS propertyName ");
        sql.append(" ,MAX(property_value) AS propertyValue ");
        sql.append(" ,groupTime ");

        sql.append(" from ( ");

        sql.append("  select ds.*, DATE_FORMAT(ds.log_date,#{dateFormat}) as groupTime ");
        sql.append("from "+ DeviceState.TABLE_NAME+" AS ds ");
        sql.append(" where 1=1 ");
        sql.append(" AND ds.device_id =#{deviceId}");
        sql.append(" AND ds.group_id =#{spaceId}");
        sql.append(" AND ds.property_name =#{propertyName}");

        sql.append(" ) as tab ");
        sql.append(" where  ");
        sql.append(" AND tab.groupTime >=#{dateStart}");

        sql.append(" AND tab.groupTime <=#{dateEnd}");

        sql.append(" GROUP BY groupTime  ");

        sql.append(" ORDER BY groupTime  ");
        return sql.toString();
    }
}
