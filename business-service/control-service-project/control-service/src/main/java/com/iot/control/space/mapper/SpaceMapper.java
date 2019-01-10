package com.iot.control.space.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.control.space.domain.Space;
import com.iot.control.space.vo.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SpaceMapper extends BaseMapper<Space> {

//    @Insert({
//            "insert into space (",
//            "id, 			    ",
//            "create_time, 			    ",
//            "update_time,		    ",
//            "icon, 				    ",
//            "position, 			    ",
//            "name,				    ",
//            "parent_id, 			    ",
//            "user_id,			    ",
//            "location_id,			    ",
//            "create_by, 			    ",
//            "update_by, 			    ",
//            "type,				    ",
//            "style,				    ",
//            "sort,				    ",
//            "tenant_id,			    ",
//            "default_space, ",
//            "org_id, ",
//            "model, ",
//            "deploy_id ",
//            ")				    ",
//            "values				    ",
//            "(",
//            "#{id},		    ",
//            "#{createTime},		    ",
//            "#{updateTime},		    ",
//            "#{icon}, 	    ",
//            "#{position},	    ",
//            "#{name},	    ",
//            "#{parentId},	    ",
//            "#{userId},	    ",
//            "#{locationId},    ",
//            "#{createBy}, 	    ",
//            "#{updateBy},	    ",
//            "#{type},		    ",
//            "#{style},		    ",
//            "#{sort},			    ",
//            "#{tenantId},	    ",
//            "#{defaultSpace},	    ",
//            "#{orgId},    ",
//            "#{model},    ",
//            "#{deployId}    ",
//            ")                                  "
//    })
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    public void save(Space space);
//
//    @Update(
//            "<script>							  " +
//                    "update space							  " +
//                    "<set>								  " +
//                    "	<if test=\"updateTime != null\">			  " +
//                    "		update_time = #{updateTime},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"icon != null and icon != '' \">				  " +
//                    "		icon = #{icon},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"position != null\">				  " +
//                    "		position = #{position},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"name != null\">				  " +
//                    "		name = #{name},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"parentId != null\">				  " +
//                    "		parent_id = #{parentId},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"userId != null\">				  " +
//                    "		user_id = #{userId},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"locationId != null\">			  " +
//                    "		location_id = #{locationId},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"updateBy != null\">				  " +
//                    "		update_by = #{updateBy},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"type != null\">				  " +
//                    "		type = #{type},			  " +
//                    "	</if>							  " +
//                    "	<if test=\"style != null\">				  " +
//                    "		style = #{style},			  " +
//                    "	</if>							  " +
//                    "	<if test=\"sort != null\">				  " +
//                    "		sort = #{sort},					  " +
//                    "	</if>							  " +
//                    "	<if test=\"tenantId != null\">				  " +
//                    "		tenant_id = #{tenantId},					  " +
//                    "	</if>							  " +
//                    "	<if test=\"defaultSpace != null\">				  " +
//                    "		default_space = #{defaultSpace},					  " +
//                    "	</if>							  " +
//
//                    "	<if test=\"orgId != null\">				  " +
//                    "		org_id = #{orgId},					  " +
//                    "	</if>							  " +
//                    "	<if test=\"model != null\">				  " +
//                    "		model = #{model},					  " +
//                    "	</if>							  " +
//                    "	<if test=\"seq != null\">				  " +
//                    "		seq = #{seq},					  " +
//                    "	</if>							  " +
//                    "	<if test=\"deployId != null\">				  " +
//                    "		deploy_id = #{deployId},					  " +
//                    "	</if>							  " +
//                    "</set>								  " +
//                    "where id = #{id}				  " +
//                    "</script>                                                        "
//    )
//    public void update(Space space);
//
//    @Delete({
//            "delete from space		     ",
//            "where id=#{id}     "
//    })
//    public void delete(Long id);
//
//    /**
//     * 获取已挂载的设备列表
//     *
//     * @param spaceDeviceReq
//     * @return
//     */
//    @Select(
//            "SELECT  " +
//                    "device_id " +
//                    "from space_device where tenant_id = #{tenantId} " +
//    		"and location_id = #{locationId}"
//    		)
//    public List<String> getMountDeviceBySpaceId(SpaceDeviceReq spaceDeviceReq);
//
//    /**
//     * 根据房间ID获取下边分组
//     *
//     * @param roomId
//     * @return
//     */
//    @Select({
//            "select id from space where parent_id=#{roomId} and type='GROUP'"
//    })
//    public List<Map<String, Object>> findGroupIdByRoom(Long roomId);
//
//    /**
//     * 根据用户获取空间列表
//     *
//     * @param  locationId
//     * @return
//     */
//    @Select(
//            "<script>                                          " +
//                    "select                                            " +
//                    "id, 						   " +
//                    "create_time as createTime, 			   " +
//                    "update_time as updateTime, 		   " +
//                    "icon, 						   " +
//                    "position, 					   " +
//                    "name,						   " +
//                    "parent_id as parentId,				   " +
//                    "user_id as userId, 				   " +
//                    "location_id as locationId,			   " +
//                    "create_by as createBy, 			   " +
//                    "update_by as updateBy, 			   " +
//                    "type,						   " +
//                    "sort,						   " +
//                    "tenant_id as tenantId,		   " +
//                    "model,       				   " +
//                    "deploy_id as deployId		   " +
//                    "from space					   " +
//                    "where location_id = #{locationId} " +
//                    "and tenant_id = #{tenantId} " +
//                    "<if test=\"name != null and name !=''\">	   " +
//                    "and name like CONCAT(CONCAT('%', #{name}), '%')   " +
//                    "</if>						   " +
//                    "order by type DESC,sort desc			   " +
//                    "</script>                                         "
//    )
//    public List<SpaceResp> findSpaceByLocationId(@Param("locationId") Long locationId, @Param("tenantId") Long tenantId, @Param("name") String name);
//
//
//    /**
//     * 查询没有挂载过的空间
//     *
//     * @param locationId
//     * @return
//     */
//    @Select(
//            "<script>                                          " +
//                    "select                                                " +
//                    "id,  						       " +
//                    "create_time as createTime, 			       " +
//                    "update_time as updateTime, 		       " +
//                    "icon, 						       " +
//                    "position, 					       " +
//                    "name,						       " +
//                    "parent_id as parentId,				       " +
//                    "user_id as userId, 				       " +
//                    "location_id as locationId,			       " +
//                    "create_by as createBy, 			       " +
//                    "update_by as updateBy, 			       " +
//                    "type,						       " +
//                    "sort,						       " +
//                    "tenant_id as tenantId				       " +
//                    "from space					       " +
//                    "where location_id = #{locationId}    " +
//                    "and parent_id is null				       " +
//                    "<if test=\"name != null and name !=''\">	       " +
//                    "and name like CONCAT(CONCAT('%', #{name}), '%')       " +
//                    "</if>						       " +
//                    "order by type DESC,sort desc			       " +
//                    "</script>                                         "
//    )
//    public List<Space> findSpaceUnMount(@Param("locationId") Long locationId, @Param("name") String name);
//
    /**
     * 根据用户获取空间列表
     *
     * @param  space
     * @return
     */
    @Select(
            "<script>                                                 " +
                    "select                                                   " +
                    "id,							  " +
                    "create_time as createTime ,				  " +
                    "update_time as updateTime ,			  " +
                    "icon ,							  " +
                    "position ,						  " +
                    "name,							  " +
                    "parent_id as parentId,					  " +
                    "user_id as userId ,					  " +
                    "location_id as locationId,				  " +
                    "create_by as createBy ,				  " +
                    "update_by as updateBy ,				  " +
                    "type,						         	  " +
                    "style,							          " +
                    "sort,							          " +
                    "tenant_id as tenantId,					  " +
                    "model,					  				  " +
                    "seq ,			  " +
                    "default_space as defaultSpace,			  " +
                    "deploy_id as deployId				      " +
                    "from space 						  " +
                    "where 1=1 						  " +
                    "<if test=\"id != null\">				  " +
                    "and id=#{id}				  " +
                    "</if>							  " +
                    "<if test=\"createTime != null\">			  " +
                    "and create_time=#{createTime}		  " +
                    "</if>							  " +
                    "<if test=\"updateTime != null\">			  " +
                    "and update_time=#{updateTime}	  " +
                    "</if>							  " +
                    "<if test=\"icon != null\">				  " +
                    "and icon=#{icon}			  " +
                    "</if>							  " +
                    "<if test=\"position != null\">				  " +
                    "and position=#{position}		  " +
                    "</if>							  " +
                    "<if test=\"name != null and name !=''\">				  " +
                    " and name like CONCAT(CONCAT('%', #{name}), '%')   " +
                    "</if>							  " +
                    "<if test=\"parentId != null\">				  " +
                    "and parent_id=#{parentId}		  " +
                    "</if>							  " +
                    "<if test=\"userId != null\">				  " +
                    "and user_id=#{userId}			  " +
                    "</if>							  " +
                    "<if test=\"locationId != null\">			  " +
                    "and location_id=#{locationId}		  " +
                    "</if>							  " +
                    "<if test=\"createBy != null\">				  " +
                    "and create_by=#{createBy}		  " +
                    "</if>							  " +
                    "<if test=\"updateBy != null\">				  " +
                    "and update_by=#{updateBy}		  " +
                    "</if>							  " +
                    "<if test=\"type != null\">				  " +
                    "and type=#{type}				  " +
                    "</if>							  " +
                    "<if test=\"sort != null\">				  " +
                    "and sort=#{sort}					  " +
                    "</if>							  " +
                    "<if test=\"tenantId != null\">				  " +
                    "and tenant_id=#{tenantId}		  " +
                    "</if>							  " +
                    "<if test=\"defaultSpace != null\">			  " +
                    "and default_space=#{defaultSpace}			  " +
                    "</if>							  " +
                    "<if test=\"orderBy != null\">			          " +
                    "order by ${orderBy}			                  " +
                    "</if>							  " +
                    "<if test=\"seq != null\">				  " +
                    "and seq=#{seq}					  " +
                    "</if>							  " +
                    "<if test=\"model != null\">				  " +
                    "and model=#{model}					  " +
                    "</if>							  " +
                    "<if test=\"deployId != null\">				  " +
                    "and deploy_id=#{deployId}					  " +
                    "</if>							  " +
                    "</script>                                        "
    )
    public List<Space> findSpaceByCondition(Space space);
//
//    /**
//     * 根据用户获取空间列表
//     *
//     * @param space
//     * @return
//     */
//    @Select(
//            "<script>                                                 " +
//                    "select                                                   " +
//                    "count(id)							  " +
//                    "from space 						  " +
//                    "where 1=1 						  " +
//                    "<if test=\"id != null\">				  " +
//                    "and id != #{id}				  " +
//                    "</if>							  " +
//                    "<if test=\"name != null and name !=''\">				  " +
//                    " and name = #{name}   " +
//                    "</if>							  " +
//                    "<if test=\"parentId != null\">				  " +
//                    "and parent_id=#{parentId}		  " +
//                    "</if>							  " +
//                    "<if test=\"userId != null\">				  " +
//                    "and user_id=#{userId}			  " +
//                    "</if>							  " +
//                    "<if test=\"locationId != null\">			  " +
//                    "and location_id=#{locationId}		  " +
//                    "</if>							  " +
//                    "<if test=\"createBy != null\">				  " +
//                    "and create_by=#{createBy}		  " +
//                    "</if>							  " +
//                    "<if test=\"type != null\">				  " +
//                    "and type=#{type}				  " +
//                    "</if>							  " +
//                    "<if test=\"tenantId != null\">				  " +
//                    "and tenant_id=#{tenantId}		  " +
//                    "</if>							  " +
//                    "<if test=\"defaultSpace != null\">			  " +
//                    "and default_space=#{defaultSpace}			  " +
//                    "</if>							  " +
//                    "</script>                                        "
//    )
//    public int checkSpaceName(Space space);
//
//
//    /**
//     * 根据build和类型获取空间
//     *
//     * @param map
//     * @return
//     */
//    /*public List<Space> getSpaceByBuildAndType(Map<String, Object> map);*/
//
//    /**
//     * 根据locationId获取未挂载空间列表
//     *
//     * @param space
//     * @return
//     */
//    @Select(
//            "<script>                                                 " +
//                    "select                                                   " +
//                    "id,							  " +
//                    "create_time as createTime ,				  " +
//                    "update_time as updateTime ,			  " +
//                    "icon ,							  " +
//                    "position ,						  " +
//                    "name,							  " +
//                    "parent_id as parentId,					  " +
//                    "user_id as userId ,					  " +
//                    "location_id as locationId,				  " +
//                    "create_by as createBy ,				  " +
//                    "update_by as updateBy ,				  " +
//                    "type,							  " +
//                    "style,							  " +
//                    "sort,							  " +
//                    "tenant_id as tenantId,					  " +
//                    "default_space as defaultSpace				  " +
//                    "from space 						  " +
//                    "where parent_id is null 						  " +
//                    "<if test=\"id != null\">				  " +
//                    "and id=#{id}				  " +
//                    "</if>							  " +
//                    "<if test=\"createTime != null\">			  " +
//                    "and create_time=#{createTime}		  " +
//                    "</if>							  " +
//                    "<if test=\"updateTime != null\">			  " +
//                    "and update_time=#{updateTime}	  " +
//                    "</if>							  " +
//                    "<if test=\"icon != null\">				  " +
//                    "and icon=#{icon}			  " +
//                    "</if>							  " +
//                    "<if test=\"position != null\">				  " +
//                    "and position=#{position}		  " +
//                    "</if>							  " +
//                    "<if test=\"name != null\">				  " +
//                    "and name like CONCAT(CONCAT('%', #{name}), '%')			  " +
//                    "</if>							  " +
//                    "<if test=\"parentId != null\">				  " +
//                    "and parent_id=#{parentId}		  " +
//                    "</if>							  " +
//                    "<if test=\"userId != null\">				  " +
//                    "and user_id=#{userId}			  " +
//                    "</if>							  " +
//                    "<if test=\"locationId != null\">			  " +
//                    "and location_id=#{locationId}		  " +
//                    "</if>							  " +
//                    "<if test=\"createBy != null\">				  " +
//                    "and create_by=#{createBy}		  " +
//                    "</if>							  " +
//                    "<if test=\"updateBy != null\">				  " +
//                    "and update_by=#{updateBy}		  " +
//                    "</if>							  " +
//                    "<if test=\"type != null\">				  " +
//                    "and type=#{type}				  " +
//                    "</if>							  " +
//                    "<if test=\"sort != null\">				  " +
//                    "and sort=#{sort}					  " +
//                    "</if>							  " +
//                    "<if test=\"tenantId != null\">				  " +
//                    "and tenant_id=#{tenantId}		  " +
//                    "</if>							  " +
//                    "<if test=\"defaultSpace != null\">			  " +
//                    "and default_space=#{defaultSpace}			  " +
//                    "</if>							  " +
//                    "<if test=\"orderBy != null\">			          " +
//                    "order by ${orderBy}			                  " +
//                    "</if>							  " +
//                    "</script>                                        "
//    )
//    public List<Space> findSpaceUnMountInfo(Space space);
//
//    /**
//     * 设置空间关系
//     *
//     * @param map
//     * @return
//     */
//    @Update(
//            "<script>							  " +
//                    "update space set parent_id=#{parentId}                                                                " +
//                    "where id in											       " +
//                    "<foreach collection=\"ids\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">     " +
//                    "#{item}											       " +
//                    "</foreach>											       " +
//                    "</script>                            "
//    )
//    public void setSpaceRelation(Map<String, Object> map);
//
//    /**
//     * 更新设备父ID
//     *
//     * @param map
//     * @return
//     */
////    @Select(
////            "<script>							  " +
////                    "select d.uuid ,                                                                                          " +
////                    "d.reality_id as realItyId,												" +
////                    "d.name,												" +
////                    "d.parent_id as parentId,										" +
////                    "d.device_type_id as deviceCategoryId,									" +
////                    "d.extra_name as extraName												" +
////                    "from													" +
////                    "space_device sd , iot_db_device.device d,								" +
////                    "iot_db_device.device_type dc 									" +
////                    "where sd.device_id=d.uuid 										" +
////                    "and d.device_type_id=dc.id 										" +
////                    "and													" +
////                    "<if test=\"types != null\">										" +
////                    "dc.type in												" +
////                    "<foreach collection=\"types\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">	" +
////                    "#{item}												" +
////                    "</foreach>												" +
////                    "and													" +
////                    "</if>													" +
////                    "sd.space_id in												" +
////                    "<foreach collection=\"groupIds\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">	" +
////                    "#{item}												" +
////                    "</foreach>												" +
////                    "</script>                            "
////    )
////    public List<Map<String, Object>> findDeviceByGroupAndType(Map<String, Object> map);
//
//    /**
//     * 更新设备父ID
//     *
//     * @param spaceId
//     * @return
//     */
//    @Update({
//            "update space set parent_id = null 	 ",
//            "where parent_id = #{sapceId}		 "
//    })
//    public void updateParentIdNull(Long spaceId);
//
//    /**
//     * 根据ID查询归属空间
//     *
//     * @param id
//     * @return
//     */
//	/*@Select({
//		"SELECT                          ",
//		"name,				 ",
//		"parent_id as parentId,		 ",
//		"type,				 ",
//		"location_id AS locationId 	 ",
//		"from  space			 ",
//		"where id=#{id,jdbcType=VARCHAR} "
//	})
//    public Space findSpaceNameById(Long id);*/
//
//    /**
//     * 根据设备查询归属直接空间
//     *
//     * @param deviceId
//     * @return
//     */
//    @Select({
//            "SELECT                                                        ",
//            "s.name as name , 					       ",
//            "s.parent_id,						       ",
//            "sd.space_id as spaceId					       ",
//            "FROM space_device sd LEFT JOIN space s on sd.space_id=s.id    ",
//            "WHERE 							       ",
//            "sd.device_id = #{deviceId,jdbcType=VARCHAR}		       "
//    })
//    public List<Map<String, Object>> findSpaceNameByDevice(String deviceId);
//
//    /*public void setStyle(@Param("spaceId") String spaceId, @Param("style") String style);*/
//
//    /*public String getStyle(String spaceId);*/
//
//    /*public List<Space> getSpaceByTypeAndLocationId(@Param("type") String type,@Param("locationId") String loctionId);*/
//
//    /**
//     * 根据空间ID统计设备归属数量
//     *
//     * @param spaceId
//     * @return
//     */
//    @Select({
//            "SELECT                                 ",
//            "count(*) 				",
//            "from space_device 			",
//            "where					",
//            "space_id=#{spaceId,jdbcType=VARCHAR}	"
//    })
//    public Integer countSpaceDeviceMount(Long spaceId);
//
//
//    /**
//     * 根据上级ID查询对应类型（HOME/ROOM）数量
//     *
//     * @param parentId
//     * @return
//     */
//    @Select({
//            "SELECT COUNT(*)                                            ",
//            "FROM (  						    ",
//            "select count(s.id) from 				    ",
//            "space s left join space_device sd on s.id=sd.space_id 	    ",
//            "where 1=1						    ",
//            "and s.parent_id = #{parentId} 		    ",
//            "GROUP BY s.id						    ",
//            ") t							    "
//    })
//    public int findSpaceCountByParentId(@Param("parentId") Long parentId);
//
//    /**
//     * 根据用户ID查询对应类型（HOME/ROOM）数量
//     *
//     * @param type
//     * @param userId
//     * @return
//     */
//    @Select({
//            "select                                     ",
//            "count(1)				    ",
//            "from space				    ",
//            "where 1=1 				    ",
//            "and user_id = #{userId,jdbcType=VARCHAR}   ",
//            "and type = #{type,jdbcType=CHAR}	    "
//    })
//    public int findSpaceCountByType(@Param("type") String type, @Param("userId") Long userId);
//
////    /**
////     * 根据租户查询空间列表
////     *
////     * @param tenantId
////     * @return
////     * @author fenglijian
////     */
//    /*public List<Space> findSpaceByTenantId(String tenantId);*/
//
//
////    /**
////     * 查询空间（家或房间）名称是否存在
////     *
////     * @param params spaceName、userId、spaceType
////     * @return
////     */
//    /*public List<Map<String, String>> checkSpaceName(Map<String, Object> params);*/
//
//    /*public Space findUserDefaultSpace(String userId);*/
//
//    /*public int updateSpaceByUserId(Space space);*/
//
    /**
     * 根据上级ID查询对应类型（HOME/ROOM）列表
     *
     * @param  parentId
     * @return
     */
    @Select({
            "select ",
            "s.id, ",
            "s.create_time as createTime, ",
            "s.update_time as updateTime, ",
            "icon, ",
            "s.position, ",
            "name, ",
            "parent_id as parentId, ",
            "user_id as userId, ",
            "s.location_id as locationId, ",
            "create_by as createBy, ",
            "update_by as updateBy, ",
            "type, ",
            "sort, ",
            "style, ",
            "default_space, ",
            "org_id, ",
            "model, ",
            "seq, ",
            "deploy_id, ",
            "mesh_name, ",
            "mesh_password, ",
            "s.tenant_id as tenantId, ",
            "count(sd.id) as devNum ",
            "from space s left join space_device sd on s.id=sd.space_id ",
            "where 1=1 ",
            " and s.parent_id = #{parentId} " +
            " and s.tenant_id = #{tenantId} ",
            "GROUP BY s.id ",
            "ORDER BY s.id "
    })
    public List<SpaceResp> findSpaceByParentId(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);
//
//    @Select({
//            "select ",
//            "s.id " +
//                    "from space s "
//    })
//    List<Map<String, Object>> findSpaceList();
//
//    //pageHelper 不支持 跟 @SelectProvider 一起使用 4.1.0 版本不兼容  目前已经解决
//    @SelectProvider(type = SpaceSqlProvider.class, method = "findSpaceListTest")
//    List<Map<String, Object>> findSpaceListTest();
//
//
//    /**
//     * 根据 spaceId 获取 Space
//     *
//     * @param id
//     * @return
//     */
//    @Select({
//            "select ",
//            "* ",
//            "from space ",
//            "where id = #{id} "
//    })
//    public Space selectById(@Param("id") Long id);
//
//    @Select({
//            "select ",
//            "b.name as name ",
//            "from space_device a ",
//            "left join space b on b.id = a.space_id ",
//            "where a.device_id = #{deviceId} "
//    })
//    public String findRoomNameByDeviceId(@Param("deviceId") String deviceId);
//
//    @Select({
//    	"select                                    ",
//		"id, 					   ",
//		"create_time as createTime, 		   ",
//		"update_time as updateTime, 	   ",
//		"icon, 					   ",
//		"position, 				   ",
//		"name,					   ",
//		"parent_id as parentId,			   ",
//		"user_id as userId, 			   ",
//		"location_id as locationId,		   ",
//		"create_by as createBy, 		   ",
//		"update_by as updateBy, 		   ",
//		"type,					   ",
//		"sort,					   ",
//		"deploy_id as deployId,					   ",
//		"tenant_id as tenantId			   ",
//        "from space s ",
//        "where s.deploy_id = #{deployId} ",
//        "and s.tenant_id = #{tenantId} ",
//        "and s.location_id = #{locationId} "
//    })
//    List<SpaceResp> findSpaceListByDeployId(@Param("deployId") Long deployId, @Param("tenantId") Long tenantId, @Param("locationId") Long locationId);
//
//
    @Select("<script> " +
            "select " +
            "* " +
            "from space " +
            "where tenant_id = #{tenantId} " +
            " and id in " +
            "  <foreach collection=\"spaceIds\" item=\"spaceId\"  open=\"(\" close=\")\" separator=\",\"> " +
            "   #{spaceId} " +
            "  </foreach> " +
            "</script> ")
    public List<Space> findSpaceListBySpaceIds(@Param("tenantId") Long tenantId, @Param("spaceIds") List<Long> spaceIds);
//
//    @Insert({
//            "insert into index_content (",
//            "id, 			    ",
//            "type, 			    ",
//            "image,		    ",
//            "top, 				    ",
//            "`left`, 			    ",
//            "width,				    ",
//            "build_id, 			    ",
//            "location_id,			    ",
//            "tenant_id	,		    ",
//            "title,		    ",
//            "create_time,				    ",
//            "create_by, 			    ",
//            "enable 			    ",
//            ")				    ",
//            "values				    ",
//            "(",
//            "#{id},		    ",
//            "#{type},		    ",
//            "#{image},		    ",
//            "#{top}, 	    ",
//            "#{left},	    ",
//            "#{width},	    ",
//            "#{buildId},	    ",
//            "#{locationId},    ",
//            "#{tenantId}, 	    ",
//            "#{title}, 	    ",
//            "#{createTime},	    ",
//            "#{createBy},	    ",
//            "#{enable}	    ",
//            ")                                  "
//    })
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    int saveIndexContent(IndexReq indexReq);
//
//    @Update(
//            "<script>							  " +
//                    "update index_content							  " +
//                    "<set>								  " +
//                    "	<if test=\"type != null\">			  " +
//                    "		type = #{type},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"image != null\">				  " +
//                    "		image = #{image},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"top != null\">				  " +
//                    "		top = #{top},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"left != null\">				  " +
//                    "		`left` = #{left},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"width != null\">				  " +
//                    "		width = #{width},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"buildId != null\">				  " +
//                    "		build_id = #{buildId},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"locationId != null\">			  " +
//                    "		location_id = #{locationId},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"tenantId != null\">				  " +
//                    "		tenant_id = #{tenantId},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"title != null\">				  " +
//                    "		title = #{title},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"enable != null\">				  " +
//                    "		enable = #{enable},	  " +
//                    "	</if>							  " +
//                    "</set>								  " +
//                    "where id = #{id}				  " +
//                    "</script>                              "
//    )
//    void updateIndexContent(IndexReq indexReq);
//
//    @Insert({
//            "insert into index_detail (",
//            "id, 			    ",
//            "index_content_id, 			    ",
//            "module_sort,		    ",
//            "module_name, 				    ",
//            "parameter, 			    ",
//            "create_time,				    ",
//            "create_by, 			    ",
//            "update_time,			    ",
//            "update_by,			    ",
//            "fresh			    ",
//            ")				    ",
//            "values				    ",
//            "(",
//            "#{id},		    ",
//            "#{indexContentId},		    ",
//            "#{moduleSort},		    ",
//            "#{moduleName}, 	    ",
//            "#{parameter},	    ",
//            "#{createTime},	    ",
//            "#{createBy},	    ",
//            "#{updateTime},    ",
//            "#{updateBy}, 	    ",
//            "#{fresh} 	    ",
//            ")                                  "
//    })
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    int saveIndexDetail(IndexDetailReq indexDetailReq);
//
//    @Update(
//            "<script>							  " +
//                    "update index_detail							  " +
//                    "<set>								  " +
//                    "	<if test=\"moduleSort != null\">			  " +
//                    "		module_sort = #{moduleSort},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"moduleName != null\">				  " +
//                    "		module_name = #{moduleName},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"parameter != null\">				  " +
//                    "		parameter = #{parameter},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"updateTime != null\">				  " +
//                    "		update_time = #{updateTime},		  " +
//                    "	</if>							  " +
//                    "	<if test=\"updateBy != null\">				  " +
//                    "		update_by = #{updateBy},	  " +
//                    "	</if>							  " +
//                    "	<if test=\"fresh != null\">				  " +
//                    "		fresh = #{fresh},		  " +
//                    "	</if>							  " +
//                    "</set>								  " +
//                    "where index_content_id = #{indexContentId} and id = #{id}				  " +
//                    "</script>                              "
//    )
//    void updateIndexDetail(IndexDetailReq indexDetailReq);
//
//    @Select({
//            "select                                    ",
//            "s.id as id, 					   ",
//            "s.type as type, 					   ",
//            "s.location_id as locationId, 		   ",
//            "s.tenant_id as tenantId, 	   ",
//            "s.title as title, 	   ",
//            "s.create_time as createTime, 	   ",
//            "s.create_by as createBy, 	   ",
//            "s.enable as enable   ",
//            "from index_content s ",
//            "where s.tenant_id = #{tenantId} and s.location_id = #{locationId} and s.image is null",
//    })
//    List<IndexResp> findCustomIndex(Pagination pagination, IndexReq indexReq);
//
//    @Select({
//            "select                                    ",
//            "s.id as id, 					   ",
//            "s.index_content_id as indexContentId, 					   ",
//            "s.module_sort as moduleSort, 					   ",
//            "s.module_name as moduleName, 					   ",
//            "s.parameter as parameter, 					   ",
//            "s.create_time as createTime, 					   ",
//            "s.create_by as createBy, 		   ",
//            "s.update_time as updateTime, 	   ",
//            "s.update_by as updateBy, 	   ",
//            "s.fresh as fresh 	   ",
//            "from index_detail s ",
//            "where s.index_content_id = #{indexContentIdStr} ",
//    })
//    List<IndexDetailResp> findIndexDetailByIndexId(@Param("indexContentIdStr") Long indexContentIdStr);
//
//    @Delete({
//            "delete from index_detail		     ",
//            "where index_content_id=#{indexContentId}     "
//    })
//    void deleteIndexDatailByIndexId(@Param("indexContentId") Long indexContentId);
//
//    @Delete({
//            "delete from index_content		     ",
//            "where id=#{id}     "
//    })
//    void deleteIndexContent(IndexReq indexReq);
//
//    @Select({
//            "select                                    ",
//            "s.id as id, 					   ",
//            "s.type as type, 					   ",
//            "s.location_id as locationId, 		   ",
//            "s.tenant_id as tenantId, 	   ",
//            "s.title as title 	   ",
//            "from index_content s ",
//            "where s.id = #{indexContentIdStr} ",
//    })
//    IndexResp findIndexContentById(@Param("indexContentIdStr") Long indexContentIdStr);
//
//
//    @Update(
//            "<script>							  " +
//                    "update index_content					 " +
//                    "set  enable = 0	  " +
//                    "where enable = #{enable}				  " +
//                    "</script>                              "
//    )
//    void setAllEnableOff(@Param("enable") int enable);
//
//
//    @Select({
//            "select                                    ",
//            "s.id as id, 					   ",
//            "s.type as type, 					   ",
//            "s.location_id as locationId, 		   ",
//            "s.tenant_id as tenantId, 	   ",
//            "s.title as title, 	   ",
//            "s.enable as enable 	   ",
//            "from index_content s ",
//            "where s.enable = '1' ",
//    })
//    IndexResp getEnableIndex();
}
