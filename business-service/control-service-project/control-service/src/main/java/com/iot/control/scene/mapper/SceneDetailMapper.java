package com.iot.control.scene.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.scene.domain.SceneDetail;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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
public interface SceneDetailMapper extends BaseMapper<SceneDetail> {

	/**
	 * 	插入 情景详情
	 *
	 * @param sceneDetail
	 */
	@Insert({
			"insert into scene_detail (",
			"scene_id,",
			"device_id,",
			"space_id,",
			"device_type_id,",
			"target_value,",
			"create_time,",
			"update_time,",
			"create_by,",
			"update_by,",
			"tenant_id,",
			"sort,",
			"location_id,",
			"method)",
			"values (#{sceneId}, ",
			"#{deviceId},",
			"#{spaceId},",
			"#{deviceTypeId},",
			"#{targetValue},",
			"#{createTime},",
			"#{updateTime},",
			"#{createBy},",
			"#{updateBy},",
			"#{tenantId},",
			"#{sort},",
			"#{locationId},",
			"#{method})"
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void insertSceneDetail(SceneDetail sceneDetail);

	/**
	 * 	统计 sceneId 数量
	 *
	 * @param sceneId
	 * @return
	 */
	@Select({
			"select",
			"count(id)",
			"from scene_detail",
			"WHERE scene_id = #{sceneId}"
	})
	public int countChildBySceneId(@Param("sceneId") Long sceneId);

	/**
	 * 	统计 每个sceneId 下的sceneDetail 数量
	 *
	 * @param sceneIdList
	 * @return
	 */
	@Select({
			"<script>",
			"select",
			"p1.scene_id as sceneId, count(p1.id) as childCount",
			"from scene_detail p1",
			"WHERE scene_id in (",
			" <foreach collection=\"sceneIdList\" item=\"sceneId\" index=\"index\" separator=\",\">" ,
			"#{sceneId,jdbcType=BIGINT}",
			"</foreach>",
			") group by p1.scene_id",
			"</script>"
	})
	public List<Map> countChildBySceneIds(@Param("sceneIdList") List<Long> sceneIdList);

	/**
	 *  根据情景ID和直连设备ID 获取 SceneDetail列表
	 *
	 * @param sceneId	情景id
	 * @param deviceIdList	设备id列表
	 * @return
	 */
	@SelectProvider(type = SceneDetailSqlProvider.class, method = "findSceneDetailBySceneIdAndDeviceParentId")
	public List<SceneDetail> findSceneDetailBySceneIdAndDeviceParentId(@Param("sceneId") Long sceneId, @Param("deviceIdList") List<String> deviceIdList);

	/**
	 *
	 * 描述：根据SceneID and sort获取情景详情
	 *
	 * @author wanglei
	 * @created 2018年3月5日 上午10:43:04
	 * @since
	 * @throws Exception
	 */
	@Select({
			"select",
			"id, device_id as deviceId, target_value as targetValue,space_id as spaceId,",
			"scene_id as sceneId ,device_type_id as deviceTypeId,create_by as createBy,",
			"update_by as updateBy,tenant_id as tennatId,sort,method",
			"from scene_detail",
			"where scene_id = #{sceneId} and sort=#{sort}"
	})
	public SceneDetailResp getSceneDetailBySceneIdAndSort(@Param("sceneId") Long sceneId, @Param("sort") Integer sort);

	/**
	 * 	根据sceneId、deviceId 获取情景详情
	 *
	 * @param sceneId
	 * @param deviceId
	 * @return
	 */
	@Select({
			"select",
			"id, device_id as deviceId, target_value as targetValue,space_id as spaceId,",
			"scene_id as sceneId ,device_type_id as deviceTypeId,create_by as createBy,",
			"update_by as updateBy,tenant_id as tennatId,sort,method",
			"from scene_detail",
			"where scene_id = #{sceneId} and device_id=#{deviceId}"
	})
	public SceneDetailResp sceneDetailBySceneIdAndDeviceId(@Param("sceneId") Long sceneId, @Param("deviceId") String deviceId);

	/**
	 * 	根据 spaceId、templateId 获取 SceneDetailVO列表
	 *
	 * @return
	 */
	@Select({
			"select",
			"s1.scene_name AS sceneName,",
			"s1.space_id AS spaceId,",
			"s2.device_id AS deviceId,",
			"s2.target_value AS targetValue,",
			"s3.device_id AS deviceId ",
			"from scene s1",
			"JOIN scene_detail s2 ON s1.id = s2.scene_id",
			"where s1.space_id = #{spaceId}",
			"AND s1.id = #{sceneId} ",
			"AND s1.tenant_id = #{tenantId} ",
			"AND s1.location_id = #{locationId} ",
			"AND s1.org_id = #{orgId} ",
			"AND s2.tenant_id = #{tenantId} ",
			"AND s2.org_id = #{orgId} ",
			"AND s2.location_id = #{locationId} "
	})
	public List<SceneDetailResp> sceneDetailList(SceneDetailReq sceneDetailReq);

	/**
	 * 	根据 sceneId、deviceTypeId 更新情景详情信息
	 *
	 * @param sceneDetailReq
	 * @return
	 */
	@UpdateProvider(type = SceneDetailSqlProvider.class, method = "updateSceneDetailBySceneIdAndDeviceTypeId")
	public int updateSceneDetailBySceneIdAndDeviceTypeId(@Param("sceneDetailReq") SceneDetailReq sceneDetailReq);

	/**
	 * 	根据 sceneId、deviceId 更新情景详情信息
	 *
	 * @param sceneDetailReq
	 * @return
	 */
	@UpdateProvider(type = SceneDetailSqlProvider.class, method = "updateSceneDetailBySceneIdAndDeviceId")
	public int updateSceneDetailBySceneIdAndDeviceId(@Param("sceneDetailReq") SceneDetailReq sceneDetailReq);

	/**
	 * 	删除情景详情
	 *
	 * @param sceneDetailId	情景详情id
	 */
	@Delete({
			"delete from scene_detail",
			"where id = #{sceneDetailId}"
	})
	public int delSceneDetailById(@Param("sceneDetailId") Long sceneDetailId);

	/**
	 * 	删除情景详情
	 *
	 * @param sceneId	情景id
	 */
	@Delete({
			"delete from scene_detail",
			"where scene_id = #{sceneId}"
	})
	public int delSceneDetailBySceneId(@Param("sceneId") Long sceneId);

	/**
	 * 	根据情景ID和排序值 删除情景详情
	 *
	 * @param sceneId
	 * @param sort
	 */
	@Delete({
			"delete from scene_detail",
			"where scene_id = #{sceneId} and sort = #{sort}"
	})
	public int delSceneDetailBySceneIdAndSort(@Param("sceneId") Long sceneId, @Param("sort") Integer sort);

	/**
	 * 	根据sceneId、deviceId 删除情景详情
	 *
	 * @param sceneId
	 * @param deviceId
	 */
	@Delete({
			"delete from scene_detail",
			"where scene_id = #{sceneId} and device_id = #{deviceId}"
	})
	public int delSceneDetailBySceneIdAndDeviceId(@Param("sceneId") Long sceneId, @Param("deviceId") String deviceId);

	/**
	 * 	根据设备ID 删除情景详情
	 *
	 * @param deviceId
	 * @return
	 */
	@Delete({
			"delete from scene_detail",
			"where device_id = #{deviceId}"
	})
	public int delSceneDetailByDeviceId(@Param("deviceId") String deviceId);

	/**
	 * 根据设备id 获取场景详情
	 * @param deviceId
	 * @return
	 */
	@Select({
			"select",
			"sd.id,",
			"sd.space_id as spaceId,",
			"sd.scene_id as sceneId,",
			"sd.device_id as deviceId,",
			"sd.create_by as createBy,",
			"sd.update_by as updateBy",
			"from scene_detail sd",
			"where sd.device_id =  #{deviceId}"
	})
	List<SceneDetailResp> sceneDetailsByDeviceId(@Param("deviceId") String deviceId);

	/**
	 * 根据场景id 获取场景详情
	 * @param sceneId
	 * @return
	 */
	@Select({
			"select",
			"sd.id ,",
			"sd.scene_id as sceneId ,",
			"sd.device_id as deviceId ,",
			"sd.space_id as spaceId ,",
			"sd.device_type_id as deviceTypeId ,",
			"sd.sort ,",
			"sd.target_value as targetValue ,",
			"sd.create_by as createBy ,",
			"sd.update_by as updateBy,",
			"sd.create_time as createTime,",
			"sd.update_time as updateTime ,",
			"sd.tenant_id as tenantId ,",
			"sd.location_id as locationId, ",
			"sd.method as method ",
			"from scene_detail sd",
			"where sd.scene_id =  #{sceneId}"
	})
	public List<SceneDetailResp> sceneDetailsBySceneId(@Param("sceneId") Long sceneId);
}
