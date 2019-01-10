package com.iot.user.mapper;

import com.iot.user.entity.User;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.UserResp;
import com.iot.user.vo.UserSearchReq;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * 描述：用户数据操作类
 * 创建人： laiguiming
 * 创建时间：2018年4月09日 上午10:40:33
 */
public interface UserMapper {

    @Delete({
            "delete from user",
            "where id = #{id,jdbcType=BIGINT}"
        })
    int deleteByPrimaryKey(Long id);

    @Insert({
            "insert into user (id, tenant_id, ",
            "user_name, nickname, ",
            "state, password, ",
            "uuid, ",
            "mqtt_password, tel, ",
            "head_img, background, email, location_id, ",
            "address, user_level, admin_status, ",
            "company, create_time, ",
            "update_time, user_status)",
            "values (#{id,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
            "#{userName,jdbcType=VARCHAR}, #{nickname,jdbcType=VARCHAR}, ",
            "#{state,jdbcType=TINYINT}, #{password,jdbcType=VARCHAR}, #{uuid,jdbcType=VARCHAR}, ",
            "#{mqttPassword,jdbcType=VARCHAR}, #{tel,jdbcType=VARCHAR}, ",
            "#{headImg,jdbcType=VARCHAR}, #{background,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, #{locationId,jdbcType=BIGINT},",
            "#{address,jdbcType=VARCHAR}, #{userLevel,jdbcType=INTEGER},  #{adminStatus,jdbcType=INTEGER}, ",
            "#{company,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{userStatus,jdbcType=VARCHAR})"
        })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(User record);

    @InsertProvider(type = UserSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(User record);

    @Select({
            "select",
            "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            "from user",
            " where user_status != 'deleted' and id = #{id,jdbcType=BIGINT}"
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
        })
    User selectByPrimaryKey(Long id);

    @UpdateProvider(type = UserSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
            "update user",
            "set tenant_id = #{tenantId,jdbcType=BIGINT},",
            "user_name = #{userName,jdbcType=VARCHAR},",
            "nickname = #{nickname,jdbcType=VARCHAR},",
            "state = #{state,jdbcType=TINYINT},",
            "password = #{password,jdbcType=VARCHAR},",
            "uuid = #{uuid,jdbcType=VARCHAR},",
            "mqtt_password = #{mqttPassword,jdbcType=VARCHAR},",
            "tel = #{tel,jdbcType=VARCHAR},",
            "head_img = #{headImg,jdbcType=VARCHAR},",
            "background = #{background,jdbcType=VARCHAR},",
            "email = #{email,jdbcType=VARCHAR},",
            "location_id = #{locationId,jdbcType=BIGINT},",
            "address = #{address,jdbcType=VARCHAR},",
            "user_level = #{userLevel,jdbcType=INTEGER},",
            "admin_status = #{adminStatus,jdbcType=INTEGER},",
            "company = #{company,jdbcType=VARCHAR},",
            "update_time = now(), ",
            "user_status =  #{userStatus,jdbcType=VARCHAR} ",
            "where id = #{id,jdbcType=BIGINT}"
        })
    int updateByPrimaryKey(User record);


    /*****************************************以下为手动生成部分***************************************/
    @Select({
            "select",
            "count(1)",
            "from user",
            "where user_status !='deleted' and user_name = #{userName,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT} and user_level = #{userLevel,jdbcType=INTEGER} "
        })
    int countByNameAndTenant(@Param("userName") String userName, @Param("tenantId") Long tenantId, @Param("userLevel") Integer userLevel);

    @Select({
            "select",
            "count(1)",
            "from user",
            "where user_status !='deleted' and user_name = #{userName,jdbcType=VARCHAR} and user_level = #{userLevel,jdbcType=INTEGER} "
    })
    int countByNameAndUserLevel(@Param("userName") String userName, @Param("userLevel") Integer userLevel);

    @Select({
            "select",
            "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            "from user",
            "where user_status !='deleted' and user_name = #{userName,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT} and user_level = #{userLevel,jdbcType=INTEGER} "
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
        })
    User getByUserNameAndTenantId(@Param("userName") String userName, @Param("tenantId") Long tenantId, @Param("userLevel") Integer userLevel);

    /**
     * 
     * 描述：获取租户信息根据租户ID
     * @author 李帅
     * @created 2018年11月13日 下午7:03:07
     * @since 
     * @param tenantIds
     * @param userLevel
     * @return
     */
    @Select({"<script>",
	        "select",
	        "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
	        "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
	        "from user",
	        "where user_status !='deleted' and admin_status = 1 and tenant_id in ",
            "<foreach item='tenantId' index='index' collection='tenantIds' open='(' separator=',' close=')'>",
            "#{tenantId}",
            "</foreach>",
	        " and user_level = #{userLevel,jdbcType=INTEGER}",
	        "</script>"
	    })
	@Results({
	        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
	        @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
	        @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
	        @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
	        @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
	        @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
	        @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
	        @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
	        @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
	    })
    List<User> getAdminUserByTenantId(@Param("tenantIds") List<Long> tenantIds, @Param("userLevel") Integer userLevel);
    
    /**
     * 
     * 描述：获取租户主账号信息根据用户名称
     * @author 李帅
     * @created 2018年11月13日 下午7:02:42
     * @since 
     * @param userName
     * @param userLevel
     * @return
     */
    @Select({"<script>",
        "select",
        "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
        "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
        "from user",
        "where user_status !='deleted' and admin_status = 1 and user_name  = #{userName,jdbcType=VARCHAR} ",
        " and user_level = #{userLevel,jdbcType=INTEGER}",
        "</script>"
    })
	@Results({
	        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
	        @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
	        @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
	        @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
	        @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
	        @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
	        @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
	        @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
	        @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
	        @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
	    })
	User getAdminUserByUserName(@Param("userName") String userName, @Param("userLevel") Integer userLevel);


    /**
     *@description 根据用户名、用户类型获取用户信息
     *@author wucheng
     *@params [userName, userLevel]
     *@create 2018/11/29 11:16
     *@return com.iot.user.entity.User
     */
    @Select({"<script>",
            "select",
            "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            "from user",
            "where user_status !='deleted' and user_name  = #{userName,jdbcType=VARCHAR} ",
            " and user_level = #{userLevel,jdbcType=INTEGER}",
            "</script>"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
    })
    User getBusinessUserByUserName(@Param("userName") String userName, @Param("userLevel") Integer userLevel);

    /**
     * @return com.iot.user.entity.User
     * @description 根据用户名、用户类型获取用户信息
     * @author wucheng
     * @params [tenantId, userName]
     * @create 2018/11/29 11:16
     */
    @Select({"<script>",
            "select",
            " id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            " email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            " from user ",
            " where 1=1 " +
                    "and user_status !='deleted' and user_name  = #{userName,jdbcType=VARCHAR} ",
            " and tenant_id = #{tenantId,jdbcType=BIGINT} ",
            "</script>"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
    })
    User getUserByUserName(@Param("tenantId") Long tenantId, @Param("userName") String userName);

    @Select({
            "select",
            "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            "from user",
            "where user_status !='deleted' and uuid = #{uuid,jdbcType=VARCHAR}"
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
        })
    User selectByUuid(String uuid);

    @Select({
            "<script> ",
            "select",
            "id, tenant_id, user_name, nickname, state, password, uuid, mqtt_password, tel, head_img, background, ",
            "email, location_id, address, user_level, admin_status, company, create_time, update_time, user_status ",
            "from user where user_status !='deleted' and tenant_id = #{loginUser.tenantId,jdbcType=BIGINT} ",
            "<if test=\"loginUser.userName != null\">		   ",
            "and user_name=#{loginUser.userName}		   ",
            "</if>	                               ",
            "and admin_status=#{loginUser.adminStatus} and user_level = #{userLevel} </script> "
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
            @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
        })
    FetchUserResp getUserByCondition(@Param("loginUser") LoginReq user, @Param("userLevel") Integer userLevel);

    @Select({
            "select",
            "id AS id, tenant_id AS tenantId, user_name AS userName, nickname AS nickname, state AS state, " ,
            "password AS password, uuid AS uuid, mqtt_password AS mqttPassword, tel AS tel, head_img AS headImg, background AS background, ",
            "email AS email, location_id AS locationId, address AS address, user_level AS userLevel, admin_status AS adminStatus, " ,
            "company AS company, create_time AS createTime, update_time AS updateTime, user_status  AS userStatus ",
            "from user",
            "where user_status !='deleted' and user_name = #{userName,jdbcType=VARCHAR} and user_level = #{userLevel,jdbcType=INTEGER}"
        })
    User getByUserNameAndLevel(@Param("userName") String userName, @Param("userLevel") Integer userLevel);

    @Select({
            "<script>",
            "select",
            "id AS id, tenant_id AS tenantId, user_name AS userName, nickname AS nickname, state AS state, " ,
            "password AS password, uuid AS uuid, mqtt_password AS mqttPassword, tel AS tel, head_img AS headImg, background AS background, ",
            "email AS email, location_id AS locationId, address AS address, user_level AS userLevel, admin_status AS adminStatus, " ,
            "company AS company, create_time AS createTime, update_time AS updateTime, user_status  AS userStatus ",
            "from user",
            "where user_status !='deleted' and user_level = #{userLevel,jdbcType=INTEGER} and id in",
            "<foreach item='userId' index='index' collection='userIds' open='(' separator=',' close=')'>",
            "#{userId}",
            "</foreach>",
            "</script>"
    })
    List<User> getByUserIdAndLevel(@Param("userIds")List<Long> userIds, @Param("userLevel") Integer userLevel);

    @Select({
            "select",
            "id AS id, tenant_id AS tenantId, user_name AS userName, nickname AS nickname, state AS state, " ,
            "password AS password, uuid AS uuid, mqtt_password AS mqttPassword, tel AS tel, head_img AS headImg, background AS background, ",
            "email AS email, location_id AS locationId, address AS address, user_level AS userLevel, admin_status AS adminStatus, " ,
            "company AS company, create_time AS createTime, update_time AS updateTime, user_status  AS userStatus ",
            "from user",
            "where user_status !='deleted' and tenant_id = #{tenantId,jdbcType=VARCHAR} and user_level = #{userLevel,jdbcType=INTEGER}"
    })
    List<User> getByTenantIdAndLevel(@Param("tenantId") Long tenantId, @Param("userLevel") Integer userLevel);

    @Update({
            "update user set user_status = #{userStatus,jdbcType=VARCHAR} where id = #{userId,jdbcType=BIGINT}"
    })
    int updateUserStatusByUserId(@Param("userId") Long userId, @Param("userStatus") String userStatus);

    @Select({
            "<script>",
            "select",
            "id AS id, uuid AS uuid " ,
            "from user",
            "where user_status !='deleted'and id in",
            "<foreach item='userId' index='index' collection='userIds' open='(' separator=',' close=')'>",
            "#{userId}",
            "</foreach>",
            "</script>"
    })
    List<User> getBathUuid(@Param("userIds")List<Long> userIds);


    @SelectProvider(type = UserSqlProvider.class, method = "queryList")
    @Results({
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
        @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
        @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
        @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
        @Result(column = "state", property = "state", jdbcType = JdbcType.TINYINT),
        @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
        @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
        @Result(column = "mqtt_password", property = "mqttPassword", jdbcType = JdbcType.VARCHAR),
        @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
        @Result(column = "head_img", property = "headImg", jdbcType = JdbcType.VARCHAR),
        @Result(column = "background", property = "background", jdbcType = JdbcType.VARCHAR),
        @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
        @Result(column = "location_id", property = "locationId", jdbcType = JdbcType.BIGINT),
        @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
        @Result(column = "user_level", property = "userLevel", jdbcType = JdbcType.INTEGER),
        @Result(column = "admin_status", property = "adminStatus", jdbcType = JdbcType.INTEGER),
        @Result(column = "company", property = "company", jdbcType = JdbcType.VARCHAR),
        @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
        @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
        @Result(column = "user_status", property = "userStatus", jdbcType = JdbcType.VARCHAR)
    })
    List<UserResp> queryList(UserSearchReq req);

    @Select({
            "<script>",
            "select",
            "id AS id, uuid AS uuid, tenant_id AS tenantId, user_name AS userName, nickname AS nickname, tel AS tel, email AS email, " ,
            "address AS address, company AS company " ,
            "from user",
            "where user_status !='deleted'and tenant_id = #{tenantId,jdbcType=VARCHAR} ",
            " and user_level = #{userLevel,jdbcType=INTEGER} and admin_status = #{adminStatus,jdbcType=INTEGER}",
            "</script>"
    })
    List<User> querySubUserList(UserSearchReq req);

    @Select({
            "<script>",
            "select",
            "id AS id, tenant_id AS tenantId, user_name AS userName, nickname AS nickname, state AS state, " ,
            "password AS password, uuid AS uuid, mqtt_password AS mqttPassword, tel AS tel, head_img AS headImg, background AS background, ",
            "email AS email, location_id AS locationId, address AS address, user_level AS userLevel, admin_status AS adminStatus, " ,
            "company AS company, create_time AS createTime, update_time AS updateTime, user_status  AS userStatus ",
            "from user",
            "where user_status !='deleted' and id in",
            "<foreach item='userId' index='index' collection='userIds' open='(' separator=',' close=')'>",
            "#{userId}",
            "</foreach>",
            "</script>"
    })
    List<User> getByUserIds(@Param("userIds")List<Long> userIds);

    @Update({
            "update user set password=#{password,jdbcType=VARCHAR} where id=#{userId,jdbcType=BIGINT}"
    })
    int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);

    /**
     *@description 获取App注册总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 13:52
     *@return java.lang.Long
     */
    @Select("select count(id) from user where user_status !='deleted' and tenant_id = #{tenantId}  and user_level = 3")
    Long getAppUserCount(@Param("tenantId") Long tenantId);
}