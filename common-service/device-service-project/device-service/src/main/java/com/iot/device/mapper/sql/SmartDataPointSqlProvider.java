package com.iot.device.mapper.sql;

import org.apache.ibatis.annotations.Param;

import com.iot.device.model.SmartDataPoint;

public class SmartDataPointSqlProvider {

	public String updateByIdAndDataPointId(@Param("et") SmartDataPoint sm) {
		StringBuilder sql = new StringBuilder();
        sql.append(" update ");
        sql.append(SmartDataPoint.TABLE_NAME);
        
        	
        sql.append(" set  property_code = #{et.propertyCode},");
        sql.append(" smart_code = #{et.smartCode},");
        sql.append(" update_by = #{et.updateBy},");
        sql.append(" update_time = #{et.updateTime}");

        sql.append(" WHERE id = #{et.id} and  data_point_id = #{et.dataPointId}");

        return sql.toString();
	}
}
