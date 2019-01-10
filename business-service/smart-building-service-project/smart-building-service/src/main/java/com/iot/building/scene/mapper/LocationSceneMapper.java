package com.iot.building.scene.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.scene.domain.LocationScene;
import com.iot.building.scene.domain.LocationSceneDetail;
import com.iot.building.scene.domain.LocationSceneRelation;
import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;

public interface LocationSceneMapper {

	/**
	 * 保存整校的locationScene
	 * @param locationScene
	 */
	@Insert({
			"insert into location_scene (",
			"id, 			    ",
			"code, 			    ",
			"name,		    ",
			"del_flag, 				    ",
			"tenant_id, 			    ",
			"location_id,				    ",
			"build_id,				    ",
			"floor_id,				    ",
			"create_by, 			    ",
			"create_time,			    ",
			"update_by,			    ",
			"update_time,			    ",
			"org_id,			    ",
			"shortcut 			    ",
			")				    ",
			"values				    ",
			"(",
			"#{id},		    ",
			"#{code},		    ",
			"#{name},		    ",
			"#{delFlag}, 	    ",
			"#{tenantId},	    ",
			"#{locationId},	    ",
			"#{buildId},	    ",
			"#{floorId},	    ",
			"#{createBy},	    ",
			"#{createTime},	    ",
			"#{updateBy},    ",
			"#{updateTime}, 	    ",
			"#{orgId}, 	    ",
			"#{shortcut} 	    ",
			")                                  "
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int save(LocationScene locationScene);


	/**
	 * 修改整校的locationScene
	 * @param locationScene
	 */
	@Update(
			"<script>							  " +
					"update location_scene							  " +
					"<set>								  " +
					"	<if test=\"code != null\">			  " +
					"		code = #{code},	  " +
					"	</if>							  " +
					"	<if test=\"name != null\">				  " +
					"		name = #{name},		  " +
					"	</if>							  " +
					"	<if test=\"delFlag != null\">				  " +
					"		del_flag = #{delFlag},	  " +
					"	</if>							  " +
					"	<if test=\"tenantId != null\">				  " +
					"		tenant_id = #{tenantId},		  " +
					"	</if>							  " +
					"	<if test=\"orgId != null\">				  " +
					"		org_id = #{orgId},		  " +
					"	</if>							  " +
					"	<if test=\"locationId != null\">				  " +
					"		location_id = #{locationId},	  " +
					"	</if>							  " +
					"	<if test=\"buildId != null\">				  " +
					"		build_id = #{buildId},	  " +
					"	</if>							  " +
					"	<if test=\"buildId != null\">				  " +
					"		floor_id = #{floorId},	  " +
					"	</if>							  " +
					"	<if test=\"updateBy != null\">				  " +
					"		update_by = #{updateBy},	  " +
					"	</if>							  " +
					"   <if test=\"updateTime != null\">				  " +
			        "		update_time = #{updateTime},	  " +
			        "	</if>							  " +
			        "   <if test=\"shortcut != null\">				  " +
			        "		shortcut = #{shortcut}	  " +
			        "	</if>							  " +
					"</set>								  " +
					"where id = #{id}				  " +
					"</script>                                                        "
	)
	void update(LocationScene locationScene);

	/**
	 *查询整校locationScene的列表，通过tenantId查询
	 * @param locationSceneReq
	 * @return
	 */
	@Select({
			"<script>							  " +
			"SELECT " +
					"		id, " +
					"		code, " +
					"		name, " +
					"		tenant_id as tenantId, " +
					"		org_id as orgId, " +
					"		build_id as buildId, " +
					"		floor_id as floorId, " +
					"		location_id as locationId, " +
					"		create_by as createBy, " +
					"		update_by as updateBy, " +
					"		create_time as createTime, " +
					"		update_time as updateTime," +
					"		shortcut " +
					"		FROM " +
					"			location_scene " +
					"		WHERE tenant_id = #{tenantId} " +
					"		AND org_id = #{orgId} " +
					"		AND location_id = #{locationId} " +
					"	<if test=\"buildId != null\">				  " +
					"		AND build_id = #{buildId}		  " +
					"	</if>							  " +
					"	<if test=\"name != null\">				  " +
					"		AND `name` like CONCAT(CONCAT('%', #{name}), '%')		  " +
					"	</if>							  " +
					"	<if test=\"floorId != null\">				  " +
					"		AND floor_id = #{floorId}	  " +
					"	</if>	" +
					"	<if test=\"shortcut != null\">				  " +
					"		AND shortcut = #{shortcut}	  " +
					"	</if>	" +
					"	<if test=\"id != null\">				  " +
					"		AND id = #{id}	  " +
					"	</if>	" +
					"   ORDER BY CONVERT (name USING gbk) COLLATE gbk_chinese_ci ASC  " +
					"</script>                                                        "
	})
	List<LocationSceneResp> findLocationSceneList(LocationSceneReq locationSceneReq);


    /**
     * 保存整校的locationSceneDetail
     * @param locationSceneDetail
     */
    @Insert({
            "insert into location_scene_detail (",
            "id, 			    ",
            "location_scene_id, 			    ",
            "scene_id,		    ",
            "tenant_id, 			    ",
            "del_flag, 				    ",
            "create_by, 			    ",
            "create_time,			    ",
            "update_by,			    ",
            "update_time 			    ",
            ")				    ",
            "values				    ",
            "(",
            "#{id},		    ",
            "#{locationSceneId},		    ",
            "#{sceneId},		    ",
            "#{tenantId}, 	    ",
            "#{delFlag},	    ",
            "#{createBy},	    ",
            "#{createTime},	    ",
            "#{updateBy},    ",
            "#{updateTime} 	    ",
            ")                                  "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveLocationSceneDetail(LocationSceneDetail locationSceneDetail);


    /**
     * 修改整校的locationSceneDetail
     * @param locationSceneDetail
     */
    @Update(
            "<script>							  " +
                    "update location_scene_detail							  " +
                    "<set>								  " +
                    "	<if test=\"locationSceneId != null\">			  " +
                    "		location_scene_id = #{locationSceneId},	  " +
                    "	</if>							  " +
                    "	<if test=\"sceneId != null\">				  " +
                    "		scene_id = #{sceneId},		  " +
                    "	</if>							  " +
                    "	<if test=\"delFlag != null\">				  " +
                    "		del_flag = #{delFlag},	  " +
                    "	</if>							  " +
                    "	<if test=\"tenantId != null\">				  " +
                    "		tenant_id = #{tenantId},		  " +
                    "	</if>							  " +
                    "	<if test=\"updateBy != null\">				  " +
                    "		update_by = #{updateBy},	  " +
                    "	</if>							  " +
                    "   <if test=\"updateTime != null\">				  " +
                    "		update_time = #{updateTime}	  " +
                    "	</if>							  " +
                    "</set>								  " +
                    "where id = #{id}				  " +
                    "</script>                                                        "
    )
    void updateLocationSceneDetail(LocationSceneDetail locationSceneDetail);

    /**
     * 查询整校locationSceneDetail的列表，通过tenantId和locationSceneId查询
     * @param locationSceneDetailReq
     * @return
     */
    @Select({
			"<script>							  " +
					"SELECT " +
                    "		a.id as id, " +
                    "		a.location_scene_id as locationId, " +
                    "		a.scene_id as sceneId, " +
                    "		a.tenant_id as tenantId, " +
                    "		a.del_flag as delFlag, " +
                    "		a.create_by as createBy, " +
                    "		a.update_by as updateBy, " +
                    "		a.create_time as createTime, " +
                    "		a.update_time as updateTime " +
//                    "		b.scene_name as sceneName " +
                    "		FROM " +
                    "		location_scene_detail a  " +
//					"      LEFT JOIN scene b on b.id = a.scene_id  " +
                    "		where a.location_scene_id = #{locationSceneId}"+
				    "	   <if test=\"tenantId != null\">				  " +
		        	"	     and 	a.tenant_id = #{tenantId}		  " +
			        "	   </if>	" +
					"</script>							  "
    })
    List<LocationSceneDetailResp> findLocationSceneDetailList(LocationSceneDetailReq locationSceneDetailReq);

    /**
     * 删除location_scene表中的数据,通过id
     * @param id
     */
    @Delete({
            "delete from location_scene",
            "where id = #{id}"
    })
    void deleteLocationScene(@RequestParam("id") Long id);

	/**
	 *删除location_scene_detail表中的数据,通过tenantId和locationSceneId
	 * @param tenantId
	 * @param locationSceneId
	 */
	@Delete({
			"delete from location_scene_detail",
			"where tenant_id =#{arg0} ",
			"and location_scene_id = #{arg1}"
	})
	void deleteLocationSceneDetail(@RequestParam("tenantId") Long tenantId, @RequestParam("locationSceneId") Long locationSceneId);

	@Delete({
			"delete from location_scene_detail",
			"where location_scene_id =#{locationSceneId} "
	})
	void deleteLocationSceneDetailStr(@RequestParam("locationSceneId") Long locationSceneId);

	@Select({
			"SELECT " +
					"		a.scene_id as sceneId " +
					"		FROM " +
					"		location_scene_detail a  " +
					"		where a.location_scene_id = #{locationSceneId}"
	})
    List<Long> findSceneIds(@RequestParam("locationSceneId") Long locationSceneId);


	/**
	 * 保存整校的locationSceneRelation
	 * @param locationSceneRelation
	 */
	@Insert({
			"insert into location_scene_relation (",
			"id, 			    ",
			"location_scene_id, 			    ",
			"location_id,		    ",
			"start_cron,			    ",
			"end_cron 			    ",
			")				    ",
			"values				    ",
			"(",
			"#{id},		    ",
			"#{locationSceneId},		    ",
			"#{locationId},		    ",
			"#{startCron},    ",
			"#{endCron} 	    ",
			")                                  "
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveLocationSceneRelation(LocationSceneRelation locationSceneRelation);


	/**
	 * 获取location_scene_list 的列表
	 * @return
	 */
	@Select({
			"SELECT " +
					"		b.id as id, " +
					"		b.name as sceneName, " +
					"		a.location_id as locationId, " +
					"		a.start_cron as startCron, " +
					"		a.end_cron as endCron " +
					"		FROM " +
					"		location_scene_relation a  " +
					"		LEFT JOIN location_scene b on b.id = a.location_scene_id  "
	})
	List<LocationSceneRelationResp> findLocationSceneRelationList();

	@Select({
			"<script>							  " +
					"SELECT " +
					"		id, " +
					"		code, " +
					"		name, " +
					"		tenant_id as tenantId, " +
					"		build_id as buildId, " +
					"		floor_id as floorId, " +
					"		location_id as locationId, " +
					"		create_by as createBy, " +
					"		update_by as updateBy, " +
					"		create_time as createTime, " +
					"		update_time as updateTime," +
					"		shortcut " +
					"		FROM " +
					"			location_scene " +
					"		WHERE tenant_id = #{tenantId} " +
					"		AND org_id = #{orgId} " +
					"		AND location_id = #{locationId} " +
					"	<if test=\"name != null\">				  " +
					"		AND `name` = #{name}		  " +
					"	</if>							  " +
					"</script>                                                        "
	})
	List<LocationSceneResp> findLocationSceneListByName(LocationSceneReq req);
}
