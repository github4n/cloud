package com.iot.control.scene.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.scene.domain.Scene;
import com.iot.control.scene.vo.rsp.SceneBasicResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 15:39
 * 修改人:
 * 修改时间：
 */
@Mapper
public interface SceneMapper extends BaseMapper<Scene> {


	/**
	 * 查询排序最大的情景
	 * @param spaceId
	 * @return
	 */
	@Select({
			"SELECT ",
			"b.sort as sort, ",
			"b.id as id, ",
			"b.create_by as createBy, ",
			"b.update_by as updateBy, ",
			"b.create_time as createTime, ",
			"b.update_time as updateTime, ",
			"b.tenant_id as tenantId ",
			"FROM ",
			"scene b  ",
			"WHERE b.sort=(select MAX(a.sort) from scene a WHERE a.space_id = #{spaceId}) and ",
			"b.space_id = #{spaceId} "
	})
	public SceneResp maxSortSceneBySpaceId(@Param("spaceId") Long spaceId);

	/**
	 * 统计 情景名称 数量
	 *
	 * @param sceneName
	 * @param userId
	 * @return
	 */
	@SelectProvider(type = SceneSqlProvider.class, method = "countBySceneName")
	public int countBySceneName(@Param("sceneName") String sceneName, @Param("userId") Long userId, @Param("sceneId") Long sceneId);

	/**
	 * 	根据 spaceId 获取 SceneVO列表
	 * @param spaceId
	 * @return
	 */
	@Select({
			"select",
			"id,",
			"space_id AS spaceId,",
			"scene_name AS sceneName,",
			"set_type,",
			"sort",
			"from scene",
			"where space_id = #{spaceId}"
	})
	public List<SceneResp> findSceneVOListBySpaceId(@Param("spaceId") Long spaceId);

	/**
	 * 	根据 userId 获取 SceneResp列表
	 *
	 * @param userId
	 * @return
	 */
	@Select({
			"select",
			"id as sceneId,",
			"scene_name as name,",
			"icon,",
			"set_type,",
			"sort,",
			"create_by as createBy",
			"from scene",
			"where create_by = #{userId} order by id desc"
	})
	public List<SceneBasicResp> findSceneRespListByUserId(@Param("userId") Long userId);

	@Select({
			"select",
			"id as sceneId,",
			"scene_name as name,",
			"icon,",
			"set_type,",
			"sort,",
			"create_by as createBy",
			"from scene",
			"where create_by = #{userId} and id in (#{sceneId}) order by id desc"
	})
	public List<SceneBasicResp> findSceneRespListByUserIdAndSceneId(@Param("userId") Long userId, @Param("sceneId") String sceneId);

	/**
	 * 	根据 userId、spaceId 获取 List<SceneBasicResp>
	 *
	 * @param userId
	 * @return
	 */
	@Select({
			"select",
			"id as sceneId,",
			"scene_name as name,",
			"icon,",
			"set_type,",
			"sort",
			"from scene",
			"where create_by = #{userId} and space_id = #{spaceId} order by id desc"
	})
	public List<SceneBasicResp> findSceneRespListByUserIdAndSpaceId(@Param("userId") Long userId, @Param("spaceId") Long spaceId);

	/**
	 * 	根据ID获取情景
	 *
	 * @param sceneId
	 * @return
	 */
	@Select({
			"select",
			"s.id ,",
			"s.scene_name as sceneName ,",
			"s.space_id as spaceId ,",
			"s.create_by as createBy ,",
			"s.update_by as updateBy,",
			"s.create_time as createTime,",
			"s.update_time as updateTime ,",
			"s.tenant_id as tenantId ,",
			"s.org_id as orgId ,",
			"s.icon ,",
			"s.set_type as setType ,",
			"s.sort ,",
			"s.upload_status as uploadStatus ,",
			"s.location_id as locationId ",
			"from scene s",
			"where s.id = #{sceneId}"
	})
	public Scene getById(@Param("sceneId") Long sceneId);

	/**
	 * 	根据ID获取情景
	 *
	 * @param sceneId
	 * @return
	 */
	@Select({
		"select",
		"s.id ,",
		"s.scene_name as sceneName ,",
		"s.space_id as spaceId ,",
		"s.create_by as createBy ,",
		"s.update_by as updateBy,",
		"s.create_time as createTime,",
		"s.update_time as updateTime ,",
		"s.tenant_id as tenantId ,",
		"s.org_id as orgId ,",
		"s.icon ,",
		"s.set_type as setType ,",
		"s.sort ,",
		"s.upload_status as uploadStatus ,",
		"s.location_id as locationId ",
		"from scene s",
		"where s.id = #{sceneId}"
	})
	public SceneResp getSceneById(@Param("sceneId") Long sceneId);

	/**
	 * 	插入 情景信息
	 *
	 * @param scene
	 */
	@Insert({
			"insert into scene (",
			"scene_name,",
			"space_id,",
			"create_time,",
			"update_time,",
			"create_by,",
			"update_by,",
			"tenant_id,",
			"set_type,",
			"sort,",
			"upload_status,",
			"icon,location_id,template_id)",
			"values (#{sceneName}, ",
			"#{spaceId},",
			"#{createTime},",
			"#{updateTime},",
			"#{createBy},",
			"#{updateBy},",
			"#{tenantId},",
			"#{setType},",
			"#{sort},",
			"#{uploadStatus},",
			"#{icon},#{locationId},#{templateId})"
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void insertScene(Scene scene);

	/**
	 * 	更新情景信息
	 *
	 * @param scene
	 * @return
	 */
	@UpdateProvider(type = SceneSqlProvider.class, method = "updateSceneInfo")
	public int updateSceneInfo(@Param("scene") Scene scene);

	/**
	 * 	删除情景
	 *
	 * @param sceneId	情景id
	 */
	@Delete({
			"delete from scene",
			"where id = #{sceneId}"
	})
	public void delScene(@Param("sceneId") Long sceneId);

	/**
	 * 根据用户id删除所有情景
	 * @param userId
	 * @return
	 */
	@Delete({
			"delete from scene",
			"where create_by = #{userId}"
	})
	int delScenesByUserId(@Param("userId") Long userId);



	/**
	 * 查询排序最小的情景
	 * @param spaceId
	 * @return
	 */
	@Select({
		"SELECT " +
		"			b.sort, " +
		"			b.id, " +
		"			b.create_by, " +
		"			b.update_by, " +
		"			b.create_time, " +
		"			b.update_time, " +
		"			b.tenant_id " +
		"		FROM " +
		"			scene b  " +
		"		WHERE b.sort=(select MIN(a.sort) from scene a WHERE a.space_id = #{spaceId}) and " +
		"			b.space_id = #{spaceId}"
	})
	public Scene getMinSortSceneBySpaceId(Long spaceId);

	/**
	 * 查询房间情景列表
	 * @param spaceId
	 * @return
	 */
	@Select({
		"SELECT " +
		"		id, " +
		"		space_id AS " +
		"		spaceId, " +
		"		scene_name AS sceneName, " +
		"		template_id AS templateId, " +
		"		set_type AS setType " +
		"		FROM " +
		"		scene " +
		"		WHERE " +
		"		space_id = " +
		"		#{spaceId}" +
		"       order by sort asc "
	})
	public List<SceneResp> findSceneDetailList(Long spaceId);

	/**
	 * 查询房间情景列表
	 * @param spaceId
	 * @return
	 */
	@Select({
		"SELECT " +
		"		sort, " +
		"		id, " +
		"		create_by, " +
		"		update_by, " +
		"		create_time, " +
		"		update_time, " +
		"		tenant_id" +
		"		FROM " +
		"			scene " +
		"		where space_id = #{spaceId} and sort =#{sort}"
	})
	public Scene getSceneBySpaceIdAndSort(@Param("spaceId") Long spaceId, @Param("sort") int sort);
	
	/**
	 * 	根据ID获取情景
	 *
	 * @return
	 */
	@Select({
			"select",
			"s.id ,",
			"s.scene_name as sceneName ,",
			"s.space_id as spaceId ,",
			"s.create_by as createBy ,",
			"s.update_by as updateBy,",
			"s.create_time as createTime,",
			"s.update_time as updateTime ,",
			"s.tenant_id as tenantId ,",
			"s.org_id as orgId ,",
			"s.icon ,",
			"s.set_type as setType ,",
			"s.sort ,",
			"s.upload_status as uploadStatus ,",
			"s.template_id as templateId ,",
			"s.location_id as locationId ",
			"from scene s",
			"where s.template_id = #{templateId} ",
			"and s.tenant_id = #{tenantId} ",
			"and s.location_id = #{locationId} "
	})
	public List<Scene> findSceneBytemplateId(@Param("templateId") Long templateId, @Param("tenantId") Long tenantId, @Param("locationId") Long locationId);


	@Select({
			"select",
			"s.id as id,",
			"s.scene_name as sceneName ",
			"from scene s",
			"where s.space_id = #{spaceId} "
	})
    List<Scene> findSceneByCondition(Scene scene);
}
