package com.iot.design.project.mapper;

import com.iot.design.project.dto.TeamDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Mapper
public interface TeamMapper {
    @Select({"select * " +
            "from team " +
            "where id = #{id,jdbcType=BIGINT} and data_status = 1 "})
    TeamDTO findTeamById(@Param("id") Long id);

    /**
     *  获取项目名称是@name 项目id不是@id的数目
     * @param name 项目名称
     * @param id 项目id
     * @return
     */
    @Select({"<script>" +
            " select count(id) " +
            " from team " +
            " where data_status = 1 " +
            " <if test=\"name != null\"> and name=#{name,jdbcType=VARCHAR}</if> "  +
            " <if test=\"id != null\"> and id !=#{id,jdbcType=BIGINT} </if> " +
            " </script>"})
    Integer countTeamByNameWithOutId(@Param("name") String name, @Param("id") Long id);

    @Select({"<script>" +
            " select * " +
            " from team " +
            " where  data_status = 1 and " +
            " name like \"%${name}%\" " +
            " limit ${(page-1)*pageSize},${page*pageSize}" +
            "</script>"})
    List<TeamDTO> pageTeam(
            @Param("name") String name,
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize);

    @Select({" select * " +
            " from team " +
            " where  data_status = 1 "})
    List<TeamDTO> listTeam();

    @Insert("INSERT INTO team (   " +
            "    tenant_id,   " +
            "    data_status,   " +
            "    create_by,   " +
            "    create_time,   " +
            "    name   " +
            ")   " +
            "VALUES (   " +
            "       #{teamDTO.tenantId,jdbcType=BIGINT},   " +
            "       #{teamDTO.dataStatus,jdbcType=INTEGER},   " +
            "       #{teamDTO.createBy,jdbcType=VARCHAR},   " +
            "       now(),   " +
            "       #{teamDTO.name,jdbcType=VARCHAR}   " +
            "    )")
    int insertTeam(@Param("teamDTO") TeamDTO teamDTO);

    @Update({"<script>",
            "update team" +
                    "	set " +
                    "	 <if test=\"teamDTO.tenantId != null\"> tenant_id=#{teamDTO.tenantId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"teamDTO.dataStatus != null\"> data_status=#{teamDTO.dataStatus,jdbcType=INTEGER},</if>" +
                    "	 <if test=\"teamDTO.updateBy != null\"> update_by=#{teamDTO.updateBy,jdbcType=VARCHAR},</if>" +
                    "   update_time=now()," +
                    "	 <if test=\"teamDTO.name != null\"> name=#{teamDTO.name,jdbcType=VARCHAR},</if>" +
                    "    id = #{teamDTO.id,jdbcType=BIGINT}" +
                    "   where id = #{teamDTO.id,jdbcType=BIGINT} and data_status = 1 ",
            "</script>"})
    int updateTeam(@Param("teamDTO") TeamDTO teamDTO);

    @Delete("update  team  set data_status = 0 WHERE id =#{id}")
    int deleteTeamById(Long id);


}
