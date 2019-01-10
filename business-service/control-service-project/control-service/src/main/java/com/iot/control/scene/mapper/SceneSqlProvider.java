package com.iot.control.scene.mapper;

import com.iot.control.scene.domain.Scene;
import org.apache.ibatis.annotations.Param;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/17 17:14
 * 修改人:
 * 修改时间：
 */
public class SceneSqlProvider {


    public String countBySceneName(@Param("sceneName") String sceneName, @Param("userId") Long userId, @Param("sceneId") Long sceneId) {
        BEGIN();

        SELECT("count(id)");

        FROM("scene");

        WHERE("scene_name = #{sceneName}");

        WHERE("create_by= #{userId}");

        if (sceneId != null && sceneId != 0) {
            WHERE("id != #{sceneId}");
        }

        return SQL();
    }

    public String updateSceneInfo(@Param("scene") Scene scene) {
        BEGIN();
        UPDATE("scene");

        if (scene.getSceneName() != null) {
            SET("scene_name = #{scene.sceneName}");
        }

        if (scene.getIcon() != null) {
            SET("icon = #{scene.icon}");
        }

        if (scene.getUpdateBy() != null) {
            SET("update_by = #{scene.updateBy}");
        }

        if (scene.getSpaceId() != null) {
            SET("space_id = #{scene.spaceId}");
        }

        if (scene.getSetType() != null) {
            SET("set_type = #{scene.setType}");
        }

        if (scene.getSort() != null) {
            SET("sort = #{scene.sort}");
        }

        if (scene.getUploadStatus() != null) {
            SET("upload_status = #{scene.uploadStatus}");
        }

        SET("update_time = now()");

        WHERE("id = #{scene.id}");

        return SQL();
    }
}
