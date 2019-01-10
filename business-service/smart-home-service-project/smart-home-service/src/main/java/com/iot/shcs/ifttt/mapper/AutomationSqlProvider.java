package com.iot.shcs.ifttt.mapper;

import com.iot.shcs.ifttt.entity.Automation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class AutomationSqlProvider {

    /**
     *  获取简单列表(只返回 id这一列)
     * @param record
     * @return
     */
    public String findSimpleList(Automation record) {
        BEGIN();
        SELECT("id");
        FROM("automation");

        if (record.getUserId() != null) {
            WHERE("user_id = #{userId,jdbcType=BIGINT}");
        }

        if (record.getSpaceId() != null) {
            WHERE("space_id = #{spaceId,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            WHERE("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        WHERE("visiable = 1");


        ORDER_BY("create_time desc");

        return SQL();
    }

    /**
     * 根据 ids 获取列表
     * @param ids
     * @return
     */
    public String listByIds(@Param("ids") List<Long> ids) {
        BEGIN();
        SELECT("id, name, icon, trigger_type, status, is_multi, space_id, tenant_id, user_id, applet_id, " +
                "direct_id, create_time, update_time, time_json, delay, rule_id, dev_scene_id, dev_timer_id");
        FROM("automation");

        if (CollectionUtils.isNotEmpty(ids)) {
            WHERE(" id IN ("+buildInStr(ids)+")");
        } else {
            WHERE(" id = -1");
        }
        return SQL();
    }

    // 构建in语句
    private String buildInStr(List<Long> dataList) {
        StringBuilder inStr = new StringBuilder();
        for(int i=0;i<dataList.size();i++) {
            if(i==(dataList.size()-1)) {
                inStr.append("'"+dataList.get(i)+"'");
            }else {
                inStr.append("'"+dataList.get(i)+"'"+",");
            }
        }
        return inStr.toString();
    }
}