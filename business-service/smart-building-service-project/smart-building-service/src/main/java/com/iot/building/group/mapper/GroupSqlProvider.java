package com.iot.building.group.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import org.apache.ibatis.annotations.Param;

import com.iot.building.group.vo.GroupReq;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/17 17:35
 * 修改人:
 * 修改时间：
 */
public class GroupSqlProvider {

    public  String updateByGroupId(@Param("groupReq") GroupReq groupReq) {
        BEGIN();
        UPDATE("group_info");

        if (groupReq.getName() != null) {
            SET(" name = #{groupReq.name} ");
        }
        
        if (groupReq.getModel() != null) {
            SET(" model = #{groupReq.model} ");
        }
        
        if (groupReq.getGatewayId() != null) {
            SET(" gateway_id = #{groupReq.gatewayId} ");
        }
        
        if (groupReq.getRemoteId() != null) {
            SET(" remote_id = #{groupReq.remoteId} ");
        }

        WHERE(" group_id = #{groupReq.groupId} ");
        return SQL();
    }

}
