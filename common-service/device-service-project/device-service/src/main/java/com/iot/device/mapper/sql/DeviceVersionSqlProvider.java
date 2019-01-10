package com.iot.device.mapper.sql;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:16 2018/5/25
 * @Modify by:
 */
public class DeviceVersionSqlProvider {

    public String selectOtaNewInfoListByProductId(@Param("productId") Long productId) {

        StringBuilder sql = new StringBuilder();

        sql.append(
                "SELECT "
                        + " * "
                        + " FROM "
                        + " device_version AS a, "
                        + " ( "
                        + " SELECT "
                        + " max(id) AS id "
                        + " FROM "
                        + " device_version AS b "
                        + " WHERE "
                        + " product_id = #{productId} "
                        + " GROUP BY "
                        + " fw_type "
                        + " ) AS b "
                        + "WHERE "
                        + " a.id = b.id ");
        return sql.toString();
    }
}
