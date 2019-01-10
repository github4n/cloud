package com.iot.control.scene.mapper;

import com.iot.control.scene.vo.req.SceneDetailReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/17 17:35
 * 修改人:
 * 修改时间：
 */
public class SceneDetailSqlProvider {

    public String updateSceneDetailBySceneIdAndDeviceTypeId(@Param("sceneDetailReq") SceneDetailReq sceneDetailReq) {
        BEGIN();
        UPDATE("scene_detail");

        SET("target_value = #{sceneDetailReq.targetValue}");

        if (sceneDetailReq.getUpdateBy() != null) {
            SET("update_by = #{sceneDetailReq.updateBy}");
        }

        SET("update_time = now()");

        WHERE("scene_id = #{sceneDetailReq.sceneId}");
        WHERE("device_type_id = #{sceneDetailReq.deviceTypeId}");
        WHERE("device_id = #{sceneDetailReq.deviceId}");
        return SQL();
    }

    public String updateSceneDetailBySceneIdAndDeviceId(@Param("sceneDetailReq") SceneDetailReq sceneDetailReq) {
        BEGIN();
        UPDATE("scene_detail");

        if (sceneDetailReq.getSpaceId() != null) {
            SET("space_id = #{sceneDetailReq.spaceId}");
        }

        if (sceneDetailReq.getTargetValue() != null) {
            SET("target_value = #{sceneDetailReq.targetValue}");
        }

        if (sceneDetailReq.getUpdateBy() != null) {
            SET("update_by = #{sceneDetailReq.updateBy}");
        }

        if (sceneDetailReq.getLocationId() != null) {
            SET("location_id = #{sceneDetailReq.locationId}");
        }

        if (sceneDetailReq.getMethod() != null) {
            SET("method = #{sceneDetailReq.method}");
        }

        SET("update_time = now()");

        WHERE("scene_id = #{sceneDetailReq.sceneId}");
        WHERE("device_id = #{sceneDetailReq.deviceId}");
        return SQL();
    }

    public String findSceneDetailBySceneIdAndDeviceParentId(@Param("sceneId") Long sceneId, @Param("deviceIdList") List<String> deviceIdList) {
        BEGIN();

        SELECT("sd.*");

        FROM(" scene_detail sd");

        WHERE("sd.scene_id = #{sceneId}");

        if (deviceIdList != null && deviceIdList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (String deviceId : deviceIdList) {
                sb.append("'").append(deviceId).append("'");
                count++;

                if (count < deviceIdList.size()) {
                    sb.append(",");
                }
            }

            WHERE("sd.device_id in(" + sb.toString() + ")");
        }

        return SQL();
    }
}
