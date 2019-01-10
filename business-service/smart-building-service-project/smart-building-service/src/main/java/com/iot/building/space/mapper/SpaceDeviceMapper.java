package com.iot.building.space.mapper;

import com.iot.building.space.domain.RoomSpaceVo;
import com.iot.building.space.domain.SpaceDevice;
import com.iot.building.space.domain.Warning;
import com.iot.building.space.vo.DeviceVo;
import com.iot.building.space.vo.SpaceDeviceReq;
import com.iot.building.space.vo.SpaceDeviceVo;
import com.iot.building.space.vo.SpaceResp;
import com.iot.building.warning.vo.WarningResp;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpaceDeviceMapper {

    @Select(
            "<script>                                     "
                    + "select                               "
                    + "id,							      "
                    + "create_time as createTime ,		  "
                    + "update_time as lastUpdateDate ,	  "
                    + "device_id as deviceId,				  "
                    + "space_id as spaceId ,				  "
                    + " `status` ,							  "
                    + "position,							  "
                    + "location_id as locationId,			  "
                    + "tenant_id as tenantId ,			  "
                    + "device_type_id as deviceTypeId ,	  "
                    + " `order` , 						      "
                    + " business_type_id as businessTypeId, 						      "
                    + " product_id as productId		          "
                    + " from space_device   				  "
                    + "where 1=1 						      "
                    + "<if test=\"id != null\">			  "
                    + "and id=#{id}				          "
                    + "</if>							      "
                    + "<if test=\"createTime != null\">	  "
                    + "and create_time=#{createTime}		  "
                    + "</if>							      "
                    + "<if test=\"lastUpdateDate != null\"> "
                    + "and update_time=#{lastUpdateDate}	  "
                    + "</if>							      "
                    + "<if test=\"deviceId != null\">		  "
                    + "and device_id=#{deviceId}			  "
                    + "</if>							      "
                    + "<if test=\"spaceId != null\">		  "
                    + "and space_id=#{spaceId}		      "
                    + "</if>							      "
                    + "<if test=\"status != null\">		  "
                    + "and status=#{status}			      "
                    + "</if>							      "
                    + "<if test=\"position != null\">		  "
                    + "and position=#{position}		      "
                    + "</if>							      "
                    + "<if test=\"tenantId != null\">	  "
                    + "and tenant_id=#{tenantId}			  "
                    + "</if>							      "
                    + "<if test=\"locationId != null\">	  "
                    + "and location_id=#{locationId}		  "
                    + "</if>							      "
                    + "<if test=\"deviceTypeId != null\">	  "
                    + "and device_type_id=#{deviceTypeId}	  "
                    + "</if>							      "
                    + "<if test=\"order != null\">		  "
                    + "and order=#{order}		              "
                    + "</if>							      "
                    + "<if test=\"businessTypeId != null\">		  "
                    + "and business_type_id=#{businessTypeId}		              "
                    + "</if>							      "
                    + "<if test=\"productId != null\">		  "
                    + "and product_id=#{productId}		              "
                    + "</if>							      "
                    + "</script>")
    List<SpaceDevice> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq);

    /**
     * 根据分组统计设备总数
     *
     * @param map
     * @return
     */
//    @Select({
//            "<script> ",
//            "SELECT count(DISTINCT(d.id)) ",
//            "from space_device sd  ",
//            "iot_db_device.device d ON ",
//            "sd.device_id=d.id LEFT JOIN ",
//            "iot_db_device.device_type dc on ",
//            "d.device_type_id=dc.id ",
//            "where dc.type in",
//            "<foreach collection=\"type\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "and sd.space_id in",
//            "<foreach collection=\"groupIds\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "</script>                                                        "
//    })
//    public Integer countByGroupIds(Map<String, Object> map);

    /**
     * 根据空间ID和 设备类型查询设备ID
     *
     * @param map
     * @return
     */
//    @Select({
//            "<script> ",
//            "SELECT d.id as deviceId ",
//            "from space_device sd LEFT JOIN ",
//            "iot_db_device.device d ON ",
//            "sd.device_id=d.id LEFT JOIN ",
//            "iot_db_device.device_type dc on ",
//            "d.device_type_id=dc.id ",
//            "where dc.type in",
//            "<foreach collection=\"type\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "and sd.space_id in",
//            "<foreach collection=\"groupIds\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "</script>                                                        "
//    })
//    public List<Map<String, Object>> getDeviceIdByTypeAndSpace(Map<String, Object> map);

    /**
     * 根据分组统计已经开关为打开的设备总数
     *
     * @param
     * @return
     */
//    @Select({
//            "<script> ",
//            "SELECT count(DISTINCT(d.id)) ",
//            "from space_device sd LEFT JOIN ",
//            "iot_db_device.device d ON ",
//            "sd.device_id=d.id LEFT JOIN ",
//            "iot_db_device.device_type dc on ",
//            "d.device_type_id=dc.id ",
//            "where d.switch=1 ",
//            "	<if test=\"type != null\">			  ",
//            "and dc.type in	  ",
//            "<foreach collection=\"type\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "	</if>							  ",
//            "and sd.space_id in",
//            "<foreach collection=\"groupIds\" item=\"item\" index=\"index\"	   ",
//            "open=\"(\" separator=\",\" close=\")\">				   ",
//            "#{item}								   ",
//            "</foreach>								   ",
//            "</script>                                                        "
//    })
//    public Integer countOnSwitchByGroupIds(Map<String, Object> map);
    @Select({
            "<script> ",
            "SELECT device_id as deviceId ",
            "from space_device ",
            "where 1=1 ",
            "and space_id in",
            "<foreach collection=\"spaceIds\" item=\"item\" index=\"index\"	   ",
            "open=\"(\" separator=\",\" close=\")\">				   ",
            "#{item}								   ",
            "</foreach>								   ",
            "</script>                                                        "
    })
    List<String> findBySpaceIds(@Param("spaceIds") List<Long> spaceIds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Delete({
            "delete from space_device ",
            "where id=#{id} "
    })
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Insert({
            "insert into space_device (",
            "create_time, 			    ",
            "update_time,		    ",
            "device_id, 				    ",
            "space_id, 			    ",
            "location_id,				    ",
            "device_category_id, 			    ",
            "status,			    ",
            "tenant_id,			    ",
            "device_type_id, 			    ",
            "business_type_id, 			    ",
            "position, 			    ",
            "product_id 			    ",
            ")				    ",
            "values				    ",
            "( 	    ",
            "#{createTime},		    ",
            "#{lastUpdateDate},		    ",
            "#{deviceId}, 	    ",
            "#{spaceId},	    ",
            "#{locationId},	    ",
            "#{deviceCategoryId},	    ",
            "#{status},	    ",
            "#{tenantId},    ",
            "#{deviceTypeId}, 	    ",
            "#{businessTypeId}, 	    ",
            "#{position}, 	    ",
            "#{productId} 	    ",
            ")                                  "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(SpaceDevice spaceDevice);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Insert({
            "<script> " +
                    "insert into space_device " +
                    "		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"> " +
                    "			<if test=\"createTime != null\"> " +
                    "				create_time, " +
                    "			</if> " +
                    "			<if test=\"lastUpdateDate != null\"> " +
                    "				update_time, " +
                    "			</if> " +
                    "			<if test=\"deviceId != null\"> " +
                    "				device_id, " +
                    "			</if> " +
                    "			<if test=\"spaceId != null\"> " +
                    "				space_id, " +
                    "			</if> " +
                    "			<if test=\"locationId != null\"> " +
                    "				location_id, " +
                    "			</if> " +
                    "			<if test=\"deviceCategoryId != null\"> " +
                    "				device_category_id, " +
                    "			</if> " +
                    "			<if test=\"tenantId != null\"> " +
                    "				tenant_id, " +
                    "			</if> " +
                    "			<if test=\"deviceTypeId != null\"> " +
                    "				device_type_id, " +
                    "			</if> " +
                    "			<if test=\"position != null\"> " +
                    "				position, " +
                    "			</if> " +
                    "		</trim> " +
                    "		<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\"> " +
                    "			<if test=\"createTime != null\"> " +
                    "				#{createTime}, " +
                    "			</if> " +
                    "			<if test=\"lastUpdateDate != null\"> " +
                    "				#{lastUpdateDate}, " +
                    "			</if> " +
                    "			<if test=\"deviceId != null\"> " +
                    "				#{deviceId}, " +
                    "			</if> " +
                    "			<if test=\"spaceId != null\"> " +
                    "				#{spaceId}, " +
                    "			</if> " +
                    "			<if test=\"locationId != null\"> " +
                    "				#{locationId}, " +
                    "			</if> " +
                    "			<if test=\"deviceCategoryId != null\"> " +
                    "				#{deviceCategoryId}, " +
                    "			</if> " +
                    "			<if test=\"tenantId != null\"> " +
                    "				#{tenantId}, " +
                    "			</if> " +
                    "			<if test=\"deviceTypeId != null\"> " +
                    "				#{deviceTypeId}, " +
                    "			</if> " +
                    "			<if test=\"position != null\"> " +
                    "				#{position}, " +
                    "			</if> " +
                    "		</trim>" +
                    "</script>                                                        "
    })
    int insertSelective(SpaceDevice spaceDevice);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Select({
            "select " +
                    "		id, create_time, last_update_date, device_id, space_id, " +
                    "		location_id, " +
                    "		device_category_id, device_type_id, tenant_id " +
                    "		from space_device " +
                    "		where id = #{id}"
    })
    SpaceDevice selectByPrimaryKey(@Param("id") Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Update({
            "<script> " +
                    "update space_device " +
                    "		<set> " +
                    "			<if test=\"createTime != null\"> " +
                    "				create_time = #{createTime}, " +
                    "			</if> " +
                    "			<if test=\"lastUpdateDate != null\"> " +
                    "				update_time = #{lastUpdateDate}, " +
                    "			</if> " +
                    "			<if test=\"deviceId != null\"> " +
                    "				device_id = #{deviceId}, " +
                    "			</if> " +
                    "			<if test=\"spaceId != null\"> " +
                    "				space_id = #{spaceId}, " +
                    "			</if> " +
                    "			<if test=\"locationId != null\"> " +
                    "				location_id = #{locationId}, " +
                    "			</if> " +
                    "			<if test=\"deviceCategoryId != null\"> " +
                    "				device_category_id = " +
                    "				#{deviceCategoryId}, " +
                    "			</if> " +
                    "			<if test=\"position != null\"> " +
                    "				position = #{position}, " +
                    "			</if> " +
                    "			<if test=\"tenantId != null\"> " +
                    "				tenant_id = #{tenantId}, " +
                    "			</if> " +
                    "			<if test=\"deviceTypeId != null\"> " +
                    "				device_type_id = #{deviceTypeId}, " +
                    "			</if> " +
                    "			<if test=\"order != null\"> " +
                    "				`order` = #{order}, " +
                    "			</if> " +
                    "		</set> " +
                    "		where id = #{id}" +
                    "</script>                                                        "
    })
    int updateByPrimaryKeySelective(SpaceDevice spaceDevice);

    /**
     * 根据空间删除挂载的设备关系
     *
     * @mbggenerated
     */
    @Delete({
            "delete from " +
                    "		space_device " +
                    "		where space_id = #{spaceId} and " +
                    "		device_id " +
                    "		= #{deviceId}"
    })
    int deleteBySpace(SpaceDeviceVo spaceDeviceVo);

    @Delete({
            "delete from " +
                    "		space_device " +
                    "		where space_id = #{spaceId}"
    })
    int deleteBySpaceId(@Param("spaceId") Long spaceId);

    /**
     * 根据设备ID删除挂载关系
     *
     * @param deviceId
     * @return
     */
    @Delete({
            "delete from space_device " +
                    "        where device_id = #{deviceId}"
    })
    int deleteByDeviceId(@Param("deviceId") String deviceId);

    @Delete({
            "<script> " +
            "delete from space_device  where device_id in " +
                    " <foreach collection=\"deviceId\" item=\"arr\"  open=\"(\"" +
                    "    separator=\",\" close=\")\">" +
                    "   #{arr}" +
                    " </foreach>"+
            " </script>"
    })
    int deleteByDeviceIds(@Param("deviceId") String[] deviceId);
    /**
     * 查询类型是灯的打开统计数量
     *
     * @param map
     * @return
     */
//    @Select({
//            "<script> " +
//                    "SELECT count(d.id) " +
//                    "		from space_device sd LEFT JOIN " +
//                    "		iot_db_device.device d ON " +
//                    "		sd.device_id=d.id LEFT JOIN " +
//                    "		iot_db_device.device_type dc on " +
//                    "		d.device_type_id=dc.id " +
//                    "		WHERE " +
//                    "		d.switch=1 " +
//                    "		AND dc.type in " +
//                    "		<foreach item=\"item\" index=\"index\" collection=\"type\" open=\"(\" " +
//                    "			separator=\",\" close=\")\"> " +
//                    "			#{item} " +
//                    "		</foreach> " +
//                    "		AND sd.space_id in " +
//                    "		<foreach item=\"item\" index=\"index\" collection=\"groupIds\" open=\"(\" " +
//                    "			separator=\",\" close=\")\"> " +
//                    "			#{item} " +
//                    "		</foreach>" +
//                    "</script>                                                        "
//    })
//    int countLightOnSwitchByGroupIds(Map<String, Object> map);

    /**
     * 根据设备查询设备和房间关联数量
     *
     * @param deviceId
     * @return
     */
    @Select({
            "SELECT count(*) " +
                    "		from " +
                    "		space_device " +
                    "		where device_id=#{deviceId} and space_id " +
                    "		is not null"
    })
    int countByDevice(String deviceId);

    /**
     * 根据空间ID查询挂载设备ID
     *
     * @param spaceId
     * @return
     */
    @Select({
            "SELECT " +
                    "		device_id AS " +
                    "		deviceId " +
                    "		FROM " +
                    "		space_device " +
                    "		where space_id = #{spaceId}"
    })
    List<String> getDeviceIdBySpaceId(Long spaceId);

    /**
     * 根据空间ID查询 spaceDevice列表
     *
     * @param spaceId
     * @return
     */
    @Select({"SELECT id, create_time AS createTime, device_id AS deviceId, space_id AS spaceId, location_id AS locationId, " +
            "device_category_id AS deviceCategoryId, device_type_id AS deviceTypeId, tenant_id AS tenantId, " +
            "business_type_id AS businessTypeId, position AS position, product_id AS productId" +
            " from space_device " +
            "where space_id = #{spaceId}"})
    List<SpaceDevice> findListBySpaceId(Long spaceId);

    @Select({
            "SELECT " +
                    "		sd.device_id AS deviceId " +
                    "		FROM " +
                    "		space_device sd left join space s on sd.space_id=s.space_id " +
                    "		where sd.space_id =  #{spaceId} and s.type=#{type}"
    })
    List<String> getDeviceIdBySpaceIdAndType(@Param("spaceId") Long spaceId, @Param("type") String type);


    /**
     * 更新设备挂载状态
     *
     * @param spaceDeviceVo
     * @return
     */
    @Update({
            "update " +
                    "		space_device " +
                    "		set status = " +
                    "		#{spaceDeviceVo.status} " +
                    "		where device_id = " +
                    "		#{spaceDeviceVo.deviceId} and " +
                    "		space_id = " +
                    "		#{spaceDeviceVo.spaceId}"
    })
    int updateSpaceDeviceStatus(SpaceDeviceVo spaceDeviceVo);

    /**
     * 查询未挂载到网关的房间信息
     *
     * @param status
     * @return
     */
    //TODO 待修改的跨库查询
    @Select({
            "select s1.device_id AS deviceId,s1.space_id AS " +
                    "		spaceId,s2.`name`,s2.type,d1.parent_id AS parentId from space_device " +
                    "		s1 " +
                    "		JOIN space s2 on s1.space_id = s2.id " +
                    "		JOIN iot_db_device.device " +
                    "		d1 on s1.device_id = d1.id " +
                    "		where s1.status = #{status} " +
                    "		order by d1.parent_id"
    })
    List<SpaceDeviceVo> findSpaceInfo(String status);

    /**
     * 查询空间挂载的设备信息
     *
     * @param spaceDeviceVo spaceId,deviceId
     * @return
     */
    @Select({
            "SELECT " +
                    "		count(*) " +
                    "		FROM " +
                    "		space_device " +
                    "		where space_id = " +
                    "		#{spaceId} and " +
                    "		device_id = #{deviceId}"
    })
    int findSpaceMount(SpaceDeviceVo spaceDeviceVo);

    /**
     * 根据设备id和空间ID获取空间设备关系
     *
     * @param map
     * @return
     */
    @Select({
            "select id ,device_id as deviceId,space_id AS " +
                    "		spaceId,device_type_id as deviceCategoryId ,position " +
                    "		from space_device " +
                    "		where device_id=#{deviceId} and " +
                    "		space_id=#{spaceId}"
    })
    SpaceDevice findSpaceDeviceBySpaceAndDevice(Map<String, Object> map);

    @Select({
            "select id ,device_id as deviceId,space_id AS " +
                    "		spaceId,device_type_id as deviceCategoryId ,position " +
                    "		from space_device " +
                    "		where " +
                    "		device_id = #{deviceId} "
    })
    SpaceDevice findSpaceDeviceByDeviceId(Map<String, Object> map);

    /**
     * 根据设备和空间统计数量 wanglei
     *
     * @param paramMap
     * @return
     */
    @Select({
            "<script> " +
                    "select count(*) from space_device where " +
                    "		device_id = #{deviceId} and " +
                    "		space_id in " +
                    "		<foreach collection=\"spaceIds\" item=\"item\" index=\"index\" open=\"(\" " +
                    "			separator=\",\" close=\")\"> " +
                    "			#{item} " +
                    "		</foreach>" +
                    "</script>                                                        "
    })
    Integer countBySpaceAndDevice(Map<String, Object> paramMap);

    /**
     * 根据设备id获取房间id
     *
     * @param paramMap
     * @return
     */
    /*@Select({
            "select id ,device_id as deviceId,space_id AS " +
                    "		spaceId,device_type_id as deviceCategoryId ,position " +
                    "		from space_device " +
                    "		where device_id=#{deviceId}"
    })*/
    @SelectProvider(type = SpaceDeviceSqlProvider.class, method = "findSpaceIdByDeviceId")
    List<SpaceDevice> findSpaceIdByDeviceId(Map<String, Object> paramMap);

    /**
     * 描述：插入告警数据
     *
     * @param warning
     * @throws Exception
     * @author zhouzongwei
     * @created 2017年11月30日 下午2:51:45
     * @since
     */
    @Insert({"insert into warning(id, device_id, content, create_time,type,status,tenant_id,location_id) " +
            "		values " +
            "		(#{id}, #{deviceId},#{content},#{createTime},#{type},#{status},#{tenantId},#{locationId}) "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertWarning(Warning warning) throws Exception;

    /**
     * 描述：获取历史告警数据
     *
     * @return
     * @throws Exception
     * @author zhouzongwei
     * @created 2017年11月30日 下午3:14:00
     * @since
     */
    @Select({"select " +
            "		id,device_id AS " +
            "		deviceId,content,from_unixtime(create_time,'%Y-%m-%d  %H:%i:%S') " +
            "		as createTime,type,status from warning ORDER BY create_time " +
            "		desc"})
    List<WarningResp> findHistoryWarningList() throws Exception;

    /**
     * 描述：获取未读告警数据
     *
     * @return
     * @throws Exception
     * @since
     */
    @Select({"select id,content " +
            "		from warning where status = '0' ORDER BY create_time desc"})
    List<WarningResp> findUnreadWarningList() throws Exception;

    /**
     * 描述：更新告警消息状态
     *
     * @return
     * @throws Exception
     * @since
     */
    @Update({"update warning " +
            "		set status = '1' " +
            "		where id = #{id}"})
    int updateWarningStatus(Long id) throws Exception;

    /**
     * 描述：检查id是否已存在
     *
     * @return
     * @throws Exception
     * @since
     */
    @Select({"select count(*) from warning " +
            "		where id=#{id}"})
    int countWarningById(Long id) throws Exception;

    /**
     * 描述：查询房间挂载信息
     *
     * @return
     * @throws Exception
     * @since
     */
    @Select({"SELECT "
            + "id, create_time, last_update_date, device_id, "
            + "space_id, location_id, " +
            "		device_category_id, device_type_id, tenant_id" +
            "		from " +
            "		space_device"})
    List<SpaceDevice> spaceDeviceInfo();

    /**
     * 统计网关设备在楼层的数量
     *
     * @param spaceId
     * @return
     */
    @Select({
            "select ",
            "device_id AS deviceId ",
            "from space_device ",
            "where space_id = #{spaceId}"
    })
    Set<String> countGatewayDeviceOnRoom(@Param("spaceId") Long spaceId);

    /**
     * 获取楼层设备挂载信息
     *
     * @param spaceId
     * @return
     */
    @Select({"SELECT " +
            "		sd.device_id AS deviceId, " +
            "		sd.device_type_id AS deviceTypeId, " +
            "		sd.`status`AS status, " +
            "		s.id AS spaceId " +
            "		from " +
            "		space_device sd " +
            "		JOIN space s on " +
            "		sd.space_id = s.id " +
            "		where s.id = #{spaceId}"})
    List<SpaceDevice> getDeviceMonutOnFloor(@Param("spaceId") Long spaceId);


    /**
     * 查找用户房间下所有设备
     *
     * @param params
     * @return
     * @throws Exception
     */
    //TODO 跨库查询待修改
//    @Select({"select " + 
//    		"		DISTINCT " + 
//    		"		d.id, " + 
//    		"		d.NAME, " + 
//    		"		d.mac, " + 
//    		"		d.vcode, " + 
//    		"		d.STATUS, " + 
//    		"		d.create_time, " + 
//    		"		d.update_time, " + 
//    		"		d.is_direct_device, " + 
//    		"		d.parent_id, " + 
//    		"		dc.type, " + 
//    		"		d.device_type_id, " + 
//    		"		d.switch, " + 
//    		"		d.ip, " + 
//    		"		d.realityId, " + 
//    		"		d.extraName, " + 
//    		"		d.conditional, " + 
//    		"		d.product_id, " + 
//    		"		d.online_status, " + 
//    		"		d.ssid, " + 
//    		"		s.id as spaceId, " + 
//    		"		s.parent_id as parentSpaceId, " + 
//    		"		d.icon, " + 
//    		"		dc.name as devType " + 
//    		"		from iot_db_device.device d  " + 
//    		"		LEFT JOIN iot_db_device.lds_user_device ud on d.id=ud.deviceid " + 
//    		"		LEFT JOIN iot_db_device.lds_product p on d.product_id = p.id  " + 
//    		"		LEFT JOIN iot_db_device.lds_device_type dc ON dc.id = p.device_type_id  " + 
//    		"		LEFT JOIN space_device sd ON d.id = sd.device_id " + 
//    		"		LEFT JOIN space s ON sd.space_id = s.id " + 
//    		"		WHERE ud.userid is not null " + 
//    		"		and s.user_id=#{userId} " + 
//    		"		<if test=\"spaceId != null and spaceId !=''\"> " + 
//    		"		and s.id=#{spaceId} " + 
//    		"		</if> " + 
//    		"		order by sd.create_time desc"})
//    List<DeviceVo> getDeviceByUserRoom(Map<String, Object> params) throws Exception;

    /**
     * 查找用户未挂载的设备
     *
     * @param params
     * @return
     * @throws Exception
     */
    //TODO 跨库查询待修改
//    @Select({"select DISTINCT " +
//            "		d.id, " +
//            "		d.NAME, " +
//            "		d.mac, " +
//            "		d.vcode, " +
//            "		d.STATUS, " +
//            "		d.create_time, " +
//            "		d.last_update_date, " +
//            "		d.is_direct_device, " +
//            "		d.parent_id, " +
//            "		dc.type, " +
//            "		d.device_type_id, " +
//            "		d.switch, " +
//            "		d.ip, " +
//            "		d.realityId, " +
//            "		d.extraName, " +
//            "		d.conditional, " +
//            "		d.product_id, " +
//            "		d.online_status, " +
//            "		d.ssid, " +
//            "		d.icon, " +
//            "		dc.name as devType  " +
//            "		from iot_db_device.device d  " +
//            "		LEFT JOIN iot_db_device.lds_user_device ud on d.id=ud.deviceid " +
//            "		LEFT JOIN iot_db_device.lds_product p on d.product_id = p.id  " +
//            "		LEFT JOIN iot_db_device.lds_device_type dc ON dc.id = p.device_type_id  " +
//            "		INNER JOIN space_device sd ON d.ID=sd.device_id " +
//            "		left join space s on sd.space_id=s.id  " +
//            "		where ud.userid is not null " +
//            "		and s.id=#{spaceId} " +
//            "		and s.type='HOME' " +
//            "		order by sd.create_time desc"})
//    List<DeviceVo> getUserUnmountDevice(Map<String, Object> params) throws Exception;

    //TODO 跨库查询待修改
//    @Select({"select count(1) " +
//            "		from iot_db_device.device d  " +
//            "		LEFT JOIN iot_db_device.lds_user_device ud on d.id=ud.deviceid " +
//            "		LEFT JOIN iot_db_device.lds_product p on d.product_id = p.id  " +
//            "		LEFT JOIN iot_db_device.lds_device_type dc ON dc.id = p.device_type_id  " +
//            "		INNER JOIN space_device sd ON d.ID=sd.device_id " +
//            "		left join space s on sd.space_id=s.id  " +
//            "		where ud.userid is not null " +
//            "		and s.id=#{spaceId} " +
//            "		and s.type='HOME' "})
//    int getUserUnmountDeviceCount(Map<String, Object> params) throws Exception;

    /**
     * 查找用户房间下所有设备数量
     *
     * @param params
     * @return
     * @throws Exception
     */
    //TODO 跨库查询待修改
//    @Select({"select" +
//            "		count(1) " +
//            "		from iot_db_device.device d " +
//            "		LEFT JOIN iot_db_device.lds_user_device ud on d.id=ud.deviceid " +
//            "		LEFT JOIN iot_db_device.lds_product p on d.product_id = p.id " +
//            "		LEFT JOIN iot_db_device.lds_device_type dc ON dc.id = p.device_type_id " +
//            "		LEFT JOIN space_device sd ON d.id = sd.device_id" +
//            "		LEFT JOIN space s ON sd.space_id = s.id" +
//            "		WHERE ud.userid is not null " +
//            "		and s.user_id=#{userId} " +
//            "		<if test=\"spaceId != null and spaceId !=''\">" +
//            "		and s.id=#{spaceId} " +
//            "		</if>"})
//    int getDeviceCountByUserRoom(Map<String, Object> params) throws Exception;

    /**
     * 根据设备查询设备属性
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Select({"select " +
            "		sd.device_id as deviceId, " +
            "		sd.space_id as spaceId, " +
            "		s.parent_id as parentId," +
            "		s.name as spaceName," +
            "		s.icon as spaceIcon " +
            "		from space_device sd left join space s on sd.space_id=s.id " +
            "		where sd.device_id=#{deviceId}"})
    List<Map<String, Object>> querySpaceByDeviceId(Map<String, Object> params);


    @UpdateProvider(type = SpaceDeviceSqlProvider.class, method = "updateByDeviceId")
    int updateByDeviceId(SpaceDevice spaceDevice);

    /**
     * 描述：查找所有/空间下设备集合
     *
     * @return
     * @throws Exception
     * @since
     */
//	List<Device> listAllDevice(@Param("name") String name, @Param("spaceId") String spaceId) throws Exception;
    @Update({" update space_device " +
            "		set space_id=#{parentSpaceId} " +
            "		where space_id = #{spaceId}"})
    int updateByRoomId(@Param("spaceId") Long spaceId, @Param("parentSpaceId") Long parentSpaceId);

    /**
     * 根据空间ID查询挂载设备数量
     *
     * @param spaceId
     * @return
     */
    @Select({"SELECT count(*) " +
            "		from " +
            "		space_device sd LEFT JOIN iot_db_device.device d ON " +
            "		sd.device_id=d.id " +
            "		where space_id=#{spaceId} and space_id is not null"})
    int countDeviceBySpaceId(Long spaceId);

    //    @Select({
  //            "select ",
  //            " p1.id, p1.device_id AS deviceId, p1.space_id AS spaceId, p2.parent_id AS parentId,
  // p1.order ",
  //            " from space_device p1 ",
  //            " LEFT JOIN space p2 ON p2.id = p1.space_id ",
  //            " where p1.space_id IN ",
  //            " (select p3.id from space p3 where p3.parent_id = #{homeId} or p3.id=#{homeId} ",
  //            " AND p3.user_id=#{userId}) ",
  //            " ORDER BY p1.order asc,p1.create_time desc "
  //    })
  @Select({
    "SELECT \n"
        + "\tp5.id,\n"
        + "\tp5.deviceId AS deviceId,\n"
        + "\tp5.spaceId AS spaceId,\n"
        + "\tp5.parentId AS parentId,\n"
        + "\tp5.order,\n"
        + "\tp5.create_time,\n"
        + "\tp5.defaultSpace as defaultSpace,\n"
        + "\tp5.create_time as newCreateTime\n"
        + "\n"
        + "from (\n"
        + "\tSELECT\n"
        + "\tp1.id,\n"
        + "\tp1.device_id AS deviceId,\n"
        + "\tp1.space_id AS spaceId,\n"
        + "\tp2.parent_id AS parentId,\n"
        + "\tp1.order,\n"
        + "\tp2.default_space as defaultSpace,\n"
        + "\tp1.create_time\n"
        + "FROM\n"
        + "\tspace_device p1\n"
        + "INNER JOIN  space AS p2  on (p1.space_id = p2.id)\n"
        + "\n"
        + "WHERE 1=1 \n"
        + "AND p2.id = #{homeId}\n"
        + "AND p2.user_id = #{userId}\n"
        + "\n"
        + "UNION ALL \n"
        + "\n"
        + "\tSELECT\n"
        + "\tp1.id,\n"
        + "\tp1.device_id AS deviceId,\n"
        + "\tp1.space_id AS spaceId,\n"
        + "\tp2.parent_id AS parentId,\n"
        + "\tp1.order,\n"
        + "\tp2.default_space as defaultSpace,\n"
        + "\tp1.create_time\n"
        + "FROM\n"
        + "\tspace_device p1\n"
        + "INNER JOIN  space AS p2  on (p1.space_id = p2.id)\n"
        + "\n"
        + "WHERE 1=1 \n"
        + "AND p2.parent_id = #{homeId}\n"
        + "AND p2.user_id = #{userId}\n"
        + "\n"
        + ") as p5 where 1=1 \n"
        + "ORDER BY\n"
        + "\tp5.order ASC,\n"
        + "\tp5.id DESC\n"
  })
  List<SpaceDeviceVo> findSpaceDeviceVOByHomeIdAndUserId(RoomSpaceVo spaceVo);

  @Select({
            "SELECT \n"
                    + "\tp5.id,\n"
                    + "\tp5.deviceId AS deviceId,\n"
                    + "\tp5.spaceId AS spaceId,\n"
                    + "\tp5.parentId AS parentId,\n"
                    + "\tp5.order,\n"
                    + "\tp5.create_time,\n"
                    + "\tp5.defaultSpace as defaultSpace,\n"
                    + "\tp5.create_time as newCreateTime\n"
                    + "\n"
                    + "from (\n"
                    + "\tSELECT\n"
                    + "\tp1.id,\n"
                    + "\tp1.device_id AS deviceId,\n"
                    + "\tp1.space_id AS spaceId,\n"
                    + "\tp2.parent_id AS parentId,\n"
                    + "\tp1.order,\n"
                    + "\tp2.default_space as defaultSpace,\n"
                    + "\tp1.create_time\n"
                    + "FROM\n"
                    + "\tspace_device p1\n"
                    + "INNER JOIN  space AS p2  on (p1.space_id = p2.id)\n"
                    + "\n"
                    + "WHERE 1=1 \n"
                    + "AND p2.id = #{spaceId}\n"
                    + "\n"
                    + "UNION ALL \n"
                    + "\n"
                    + "\tSELECT\n"
                    + "\tp1.id,\n"
                    + "\tp1.device_id AS deviceId,\n"
                    + "\tp1.space_id AS spaceId,\n"
                    + "\tp2.parent_id AS parentId,\n"
                    + "\tp1.order,\n"
                    + "\tp2.default_space as defaultSpace,\n"
                    + "\tp1.create_time\n"
                    + "FROM\n"
                    + "\tspace_device p1\n"
                    + "INNER JOIN  space AS p2  on (p1.space_id = p2.id)\n"
                    + "\n"
                    + "WHERE 1=1 \n"
                    + "AND p2.parent_id = #{spaceId}\n"
                    + "\n"
                    + ") as p5 where 1=1 \n"
                    + "ORDER BY\n"
                    + "\tp5.order ASC,\n"
                    + "\tp5.id DESC\n"
    })
    List<SpaceDeviceVo> findSpaceDeviceVOByHomeSpaceId(@Param("spaceId") Long spaceId);

    /*********************************************跨库查询业务逻辑梳理 by lucky****************************************************************/


    @Select({"select DISTINCT " +
            " sd.device_id as DeviceId " +
            "	from  space_device AS sd  " +
            "	LEFT JOIN space AS s on (sd.space_id=s.id)  " +
            "   where 1=1 " +
            "	and s.id=#{spaceId} " +
            "	and s.type='HOME' " +
            "	order by sd.create_time desc"})
    List<DeviceVo> selectUnMountUserDeviceList(@Param("spaceId") Long spaceId);

    @Select({"select count(1) " +
            "		from " +
            "		INNER JOIN space_device sd ON d.ID=sd.device_id " +
            "		left join space s on sd.space_id=s.id  " +
            "		where 1=1 " +
            "		and s.id=#{spaceId} " +
            "		and s.type='HOME' "})
    int countUnMountUserDevice(@Param("spaceId") Long spaceId);


    @Select({"select DISTINCT " +
            " sd.device_id as DeviceId " +
            "	from  space_device AS sd  " +
            "	LEFT JOIN space AS s on (sd.space_id=s.id)  " +
            "	and s.id=#{spaceId} " +
            " and sd.device_id in (#{deviceIds}) " +
            " and s.type='HOME' " +
            " order by sd.create_time desc "})
    List<DeviceVo> selectGroupListBySpaceId(@Param("spaceId") Long spaceId, String deviceIds);

    /**
     * @param deviceIds 设备uuid集合
     * @return
     * @despriction：通过设备id找到空间信息
     * @author yeshiyuan
     * @created 2018/5/18 15:25
     */
    @Select("<script>" +
            "select s.*,d.device_id from space_device d left join space s on d.space_id=s.id where d.device_id in " +
            "  <foreach collection=\"deviceIds\" item=\"deviceId\"  open=\"(\" close=\")\" separator=\",\">" +
            "   #{deviceId}" +
            "  </foreach>" +
            "</script>")
    List<SpaceResp> selectByDeviceUuids(@Param("deviceIds") List<String> deviceIds);

    /*********************************************跨库查询业务逻辑梳理 by lucky****************************************************************/

    @Select("<script>                                            " +
            "select 					     " +
            "sd.device_id as deviceId, 			     " +
            "s.id as spaceId, 				     " +
            "s.name as spaceName 				     " +
            "from space_device sd 				     " +
            "left join space s on sd.space_id=s.id		     " +
            "where 1=1 and s.tenant_id=#{tenantId}		     " +
            "<if test=\"spaceId != null and spaceId !=0\">	     " +
            "and s.id=#{spaceId}				     " +
            "</if>						     " +
            "<if test=\"name != null and name !=''\">	     " +
            "and s.`name` like CONCAT(CONCAT('%', #{name}), '%')     " +
            "</if>						     " +
            "order by sd.create_time						     " +
            "</script>					     "
    )
    List<Map<String, Object>> getDeviceIdsBySpaceIdName(@Param("spaceId") Long spaceId, @Param("name") String name, @Param("tenantId") Long tenantId);

    @Select("<script>                                        " +
            "select 					                     " +
            "DISTINCT(sd.product_id) as productId, 			 " +
            "from space_device sd 				             " +
            "where 1=1 and s.tenant_id=#{tenantId}		     " +
            "and s.id=#{spaceId}				             " +
            "<if test=\"deviceTypeId != null \">	         " +
            "and sd.device_type_id=#{deviceTypeId}           " +
            "</if>						                     " +
            "</script>					                     "
    )
    List<Long> getDistinctProductId(@Param("spaceId") Long spaceId, @Param("deviceTypeId") Long deviceTypeId);


    @Select({"select " +
            "		s.name as spaceName" +
            "		from space_device sd left join space s on sd.space_id=s.id " +
            "		where sd.device_id=#{deviceId}"})
    String getSpaceByDeviceId(String params);

    @Select({
            "select " +
                    "id" +
                    ",device_id as deviceId " +
                    ",space_id as spaceId " +
                    ",location_id as locationId " +
                    "from space_device " +
                    "where tenant_id = #{tenantId} " +
                    "and device_id " +
                    "=#{deviceId,jdbcType=VARCHAR}"
    })
    SpaceDevice findSpaceDeviceByDeviceIds(@Param("tenantId") Long tenantId, @Param("deviceId") String deviceId);

    @SelectProvider(type = SpaceDeviceSqlProvider.class, method = "findSpaceDevBySpaceDevIds")
//    @Select("<script>" +
//            "select " +
//                    "* " +
//                    "from space_device " +
//                    "where tenant_id = #{tenantId} " +
//            "and id in " +
//            "  <foreach collection=\"spaceDevIds\" item=\"spaceDevId\"  open=\"(\" close=\")\" separator=\",\">" +
//            "   #{spaceDevId}" +
//            "  </foreach>" +
//            "</script>")
    List<SpaceDevice> findSpaceDevBySpaceDevIds(@Param("tenantId") Long tenantId, List<Long> spaceDevIds);

    @Select({
            "select " +
                    "id " +
                    "from space_device " +
                    "where tenant_id = #{tenantId} " +
                    "and space_id = #{spaceId} "
    })
    List<Long> findSpaceDevIdBySpaceId(@Param("tenantId") Long tenantId, @Param("spaceId") Long spaceId);

}