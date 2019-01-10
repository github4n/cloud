package com.iot.device.mapper.sql;

import com.iot.device.model.DataPoint;
import com.iot.device.model.DeviceTypeDataPoint;
import com.iot.device.model.ProductDataPoint;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 21:26 2018/4/19
 * @Modify by:
 */
public class DataPointSqlProvider {
    public String selectDataPointsByProductId(@Param("productId") Long productId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT" +
                "    dp.id AS dataPointId," +
                "    dp.data_type AS dataType," +
                "    dp.label_name AS dataPointName," +
                "    dp.property AS property," +
                "    dp.property_code AS propertyCode," +
                "    dp.`mode` AS model," +
                "    dp.icon_name AS iconName," +
                "    dp.description AS description " +
                "    from  "+ ProductDataPoint.TABLE_NAME+" AS dt" +
                "    LEFT JOIN "+ DataPoint.TABLE_NAME+" AS dp ON (dp.id = dt.data_point_id)" +
                "    where dt.product_id = #{productId}");
        return sql.toString();
    }
    public String findDataPointListByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId){
    	StringBuilder sql = new StringBuilder();
    	sql.append("SELECT" +
    			"    dp.id AS dataPointId," +
    			"    dp.data_type AS dataType," +
    			"    dp.label_name AS dataPointName," +
    			"    dp.property AS property," +
    			"    dp.property_code AS propertyCode," +
    			"    dp.`mode` AS model," +
    			"    dp.icon_name AS iconName," +
    			"    dp.description AS description " +
    			"    from  "+ DeviceTypeDataPoint.TABLE_NAME+" AS dt" +
    			"    LEFT JOIN "+ DataPoint.TABLE_NAME+" AS dp ON (dp.id = dt.data_point_id)" +
    			"    where dt.device_type_id = #{deviceTypeId}");
    	return sql.toString();
    }

}
