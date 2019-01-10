package com.iot.control.space.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:57 2018/5/10
 * @Modify by:
 */
public class SpaceSqlProvider {

    public String findSpaceListTest() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("select " +
                "s.id as id " +
                "from space s");

        return stringBuilder.toString();
    }
    
    public String findSpaceByLocationId(@Param("locationId") Long locationId,@Param("name") String name) {
        StringBuilder sql = new StringBuilder();
        sql.append("select                                            " +
                "id, 						   " +
                "create_time as createTime, 			   " +
                "update_time as lastUpdateDate, 		   " +
                "icon, 						   " +
                "position, 					   " +
                "name,						   " +
                "parent_id as parentId,				   " +
                "user_id as userId, 				   " +
                "location_id as locationId,			   " +
                "create_by as createBy, 			   " +
                "update_by as updateBy, 			   " +
                "type,						   " +
                "sort,						   " +
                "tenant_id as tenantId				   " +
                "from space					   " +
                "where location_id = #{locationId}");
                if(name!=null && !name.equals("")) {
                    sql.append("and name like CONCAT(CONCAT('%', #{name}), '%')   ");
                }
                sql.append(" order by type DESC,sort desc			   ");

        return sql.toString();
    }
}
