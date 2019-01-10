package com.iot.control.packagemanager.mapper;

import com.iot.control.packagemanager.entity.SceneInfo;
import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Author: nongchongwei
 * @Descrpiton: 场景Mapper
 * @Date: 9:21 2018/10/23
 * @Modify by:
 */
@Mapper
public interface SceneInfoMapper {
    @Delete({
        "delete from scene_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into scene_info (id, package_id, ",
        "tenant_id, scene_name, ",
        "json, create_time, ",
        "create_by, update_time, ",
        "update_by, demo_scene_id)",
        "values (#{id,jdbcType=BIGINT}, #{packageId,jdbcType=BIGINT}, ",
        "#{tenantId,jdbcType=BIGINT}, #{sceneName,jdbcType=VARCHAR}, ",
        "#{json,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{createBy,jdbcType=BIGINT}, #{updateTime,jdbcType=TIMESTAMP}, ",
        "#{updateBy,jdbcType=BIGINT}, #{demoSceneId,jdbcType=BIGINT}",
        ")"
    })
    int insert(SceneInfoSaveReq record);

    @Select({
        "select",
        "id, package_id, tenant_id, scene_name, json, create_time, create_by, update_time, ",
        "update_by",
        "from scene_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="package_id", property="packageId", jdbcType=JdbcType.BIGINT),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="scene_name", property="sceneName", jdbcType=JdbcType.VARCHAR),
        @Result(column="json", property="json", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT)
    })
    SceneInfo selectByPrimaryKey(Long id);

    @Update(
        "<script> update scene_info" +
        "<set> " +
            "<if test=\"sceneName != null and sceneName != ''\">scene_name = #{sceneName,jdbcType=VARCHAR},</if>" +
            "<if test=\"json != null and json != ''\">json = #{json,jdbcType=VARCHAR},</if>" +
            "<if test=\"updateBy != null \">update_by = #{updateBy,jdbcType=VARCHAR},</if>" +
            "<if test=\"updateTime != null \">update_time = #{updateTime,jdbcType=VARCHAR},</if>" +
        "</set>" +
        "  where id = #{id,jdbcType=BIGINT}" +
        "</script>"
    )
    int updateByPrimaryKey(SceneInfoSaveReq record);


    @Select({
            "select",
            "id, package_id, tenant_id, demo_scene_id, scene_name, json, create_time, create_by, update_time, ",
            "update_by",
            "from scene_info",
            "where package_id = #{packageById,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} order by id desc"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="package_id", property="packageId", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="scene_name", property="sceneName", jdbcType=JdbcType.VARCHAR),
            @Result(column="json", property="json", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="demo_scene_id", property="demoSceneId", jdbcType=JdbcType.BIGINT)
    })
    List<SceneInfoResp> selectByPackageId(@Param("packageById") Long packageById,@Param("tenantId") Long tenantId);

    @Delete("delete from scene_info where package_id = #{packageId,jdbcType=BIGINT} and tenant_id = #{tenantId}")
    int deleteByPackageIdAndTenantId(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);

    /**
     *@description 根据套包id和租户id获取当前套包绑定的套包数量
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/7 14:38
     *@return int
     */
    @Select("SELECT count(id) from scene_info where package_id = #{packageId,jdbcType=BIGINT} and tenant_id = #{tenantId}")
    int countSceneNumber(@Param("packageId")  Long packageId, @Param("tenantId") Long tenantId);

    /**
     * @despriction：删除场景
     * @author  yeshiyuan
     * @created 2018/12/10 18:51
     */
    @Delete("delete from scene_info where id = #{sceneId,jdbcType=BIGINT} and tenant_id = #{tenantId}")
    int deleteByIdAndTenantId(@Param("sceneId") Long sceneId, @Param("tenantId") Long tenantId);

    /**
     * @despriction：校验场景是否存在
     * @author  yeshiyuan
     * @created 2018/12/10 20:35
     */
    @Select("<script>" +
            "select count(1) from scene_info where tenant_id = #{tenantId} " +
            " and id in " +
            "<foreach collection='senceIds' item='item' open='(' close=')' separator=','>" +
            " #{item,jdbcType=BIGINT}" +
            "</foreach>" +
            "</script>")
    int countByIdAndTenantId(@Param("senceIds") List<Long> senceIds, @Param("tenantId") Long tenantId);

    /**
     *@description 根据场景id和租户id查询场景id和名称
     *@author wucheng
     *@params [sceneIds, tenantId]
     *@create 2018/12/13 11:03
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp>
     */
    @Select("<script>" +
            "select id,  scene_name from scene_info where tenant_id = #{tenantId} " +
            " and id in " +
            "<foreach collection='sceneIds' item='item' open='(' close=')' separator=','>" +
            " #{item,jdbcType=BIGINT}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="scene_name", property="sceneName", jdbcType=JdbcType.VARCHAR)
    })
    List<SceneInfoIdAndNameResp> getSceneInfoByIds(@Param("sceneIds") List<Long> sceneIds, @Param("tenantId") Long tenantId);
}