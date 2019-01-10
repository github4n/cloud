package com.iot.user.mapper;

import com.google.common.base.Strings;
import com.iot.user.entity.User;
import com.iot.user.vo.UserSearchReq;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;

public class UserSqlProvider {

    public String insertSelective(User record) {
        BEGIN();
        INSERT_INTO("user");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getUserName() != null) {
            VALUES("user_name", "#{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            VALUES("nickname", "#{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getState() != null) {
            VALUES("state", "#{state,jdbcType=TINYINT}");
        }
        
        if (record.getPassword() != null) {
            VALUES("password", "#{password,jdbcType=VARCHAR}");
        }

        if (record.getUuid() != null) {
            VALUES("uuid", "#{uuid,jdbcType=VARCHAR}");
        }

        if (record.getMqttPassword() != null) {
            VALUES("mqtt_password", "#{mqttPassword,jdbcType=VARCHAR}");
        }
        
        if (record.getTel() != null) {
            VALUES("tel", "#{tel,jdbcType=VARCHAR}");
        }
        
        if (record.getHeadImg() != null) {
            VALUES("head_img", "#{headImg,jdbcType=VARCHAR}");
        }

        if (record.getBackground() != null) {
            VALUES("background", "#{background,jdbcType=VARCHAR}");
        }
        
        if (record.getEmail() != null) {
            VALUES("email", "#{email,jdbcType=VARCHAR}");
        }

        if (record.getLocationId() != null) {
            VALUES("location_id", "#{locationId,jdbcType=BIGINT}");
        }
        
        if (record.getAddress() != null) {
            VALUES("address", "#{address,jdbcType=VARCHAR}");
        }

        if (record.getUserLevel() != null) {
            VALUES("user_level", "#{userLevel,jdbcType=INTEGER}");
        }
        
        if (record.getAdminStatus() != null) {
            VALUES("admin_status", "#{adminStatus,jdbcType=INTEGER}");
        }
        
        if (record.getCompany() != null) {
            VALUES("company", "#{company,jdbcType=VARCHAR}");
        }
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUserStatus() != null) {
            VALUES("user_status", "#{userStatus,jdbcType=VARCHAR}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(User record) {
        BEGIN();
        UPDATE("user");
        
        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getUserName() != null) {
            SET("user_name = #{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            SET("nickname = #{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getState() != null) {
            SET("state = #{state,jdbcType=TINYINT}");
        }
        
        if (record.getPassword() != null) {
            SET("password = #{password,jdbcType=VARCHAR}");
        }

        if (record.getUuid() != null) {
            SET("uuid = #{uuid,jdbcType=VARCHAR}");
        }
        
        if (record.getMqttPassword() != null) {
            SET("mqtt_password = #{mqttPassword,jdbcType=VARCHAR}");
        }
        
        if (record.getTel() != null) {
            SET("tel = #{tel,jdbcType=VARCHAR}");
        }
        
        if (record.getHeadImg() != null) {
            SET("head_img = #{headImg,jdbcType=VARCHAR}");
        }

        if (record.getBackground() != null) {
            SET("background = #{background,jdbcType=VARCHAR}");
        }

        if (record.getEmail() != null) {
            SET("email = #{email,jdbcType=VARCHAR}");
        }

        if (record.getLocationId() != null) {
            SET("location_id = #{locationId,jdbcType=BIGINT}");
        }
        
        if (record.getAddress() != null) {
            SET("address = #{address,jdbcType=VARCHAR}");
        }

        if (record.getUserLevel() != null) {
            SET("user_level = #{userLevel,jdbcType=INTEGER}");
        }
        
        if (record.getAdminStatus() != null) {
            SET("admin_status = #{adminStatus,jdbcType=INTEGER}");
        }
        
        if (record.getCompany() != null) {
            SET("company = #{company,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUserStatus() != null) {
            SET("user_status = #{userStatus,jdbcType=VARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    public String queryList(UserSearchReq req) {
        BEGIN();
        SELECT("id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, " +
                "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ");
        FROM("user");
        if (!Strings.isNullOrEmpty(req.getUserName())) {
            WHERE(" user_name like CONCAT('%',#{userName,jdbcType=VARCHAR},'%')");
        }
        if (req.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        if (req.getLocationId() != null) {
            WHERE("location_id = #{locationId,jdbcType=BIGINT}");
        }
        WHERE("user_status !='deleted'");
        ORDER_BY("user_name");
        return SQL();
    }

}