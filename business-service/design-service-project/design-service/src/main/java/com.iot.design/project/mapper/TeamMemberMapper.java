package com.iot.design.project.mapper;

import com.iot.design.project.dto.TeamMemberDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Mapper
public interface TeamMemberMapper {
    @Select({"select * " +
            "from team_member " +
            "where id = #{id,jdbcType=BIGINT} and data_status = 1 "})
    TeamMemberDTO findTeamMemberById(@Param("id") Long id);


    @Select({"<script>" +
            " select * " +
            " from team_member " +
            " where data_status = 1 and " +
            " user_name like \"%${userName}%\" " +
            " limit ${(page-1)*pageSize},${page*pageSize}" +
            "</script>"})
    List<TeamMemberDTO> pageTeamMember(
            @Param("userName") String userName,
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize);

    @Select({" select * " +
            " from team_member " +
            " where  data_status = 1 "})
    List<TeamMemberDTO> listTeamMember();

    @Insert("INSERT INTO team_member (   " +
            "    tenant_id,   " +
            "    team_id,   " +
            "    user_id,   " +
            "    user_name,   " +
            "    user_role   " +
            "    data_status,   " +
            "    create_by,   " +
            "    create_time,   " +
            ")   " +
            "VALUES (   " +
            "        #{teamMemberDTO.tenantId,jdbcType=BIGINT},   " +
            "        #{teamMemberDTO.teamId,jdbcType=BIGINT},   " +
            "        #{teamMemberDTO.userId,jdbcType=BIGINT},   " +
            "        #{teamMemberDTO.userName,jdbcType=VARCHAR},   " +
            "        #{teamMemberDTO.userRole,jdbcType=INTEGER},   " +
            "        #{teamMemberDTO.dataStatus,jdbcType=INTEGER},   " +
            "        #{teamMemberDTO.createBy,jdbcType=VARCHAR},   " +
            "        now()   " +
            "    )")
    int insertTeamMember(@Param("teamMemberDTO") TeamMemberDTO teamMemberDTO);

    @Update({"<script>",
            "update team_member" +
                    "	set " +
                    "	 <if test=\"teamMemberDTO.tenantId != null\"> tenant_id=#{teamMemberDTO.tenantId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"teamMemberDTO.teamId != null\"> team_id=#{teamMemberDTO.teamId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"teamMemberDTO.userId != null\"> user_id=#{teamMemberDTO.userId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"teamMemberDTO.userName != null\"> user_name=#{teamMemberDTO.userName,jdbcType=VARCHAR},</if>" +
                    "	 <if test=\"teamMemberDTO.userRole != null\"> user_role=#{teamMemberDTO.userRole,jdbcType=INTEGER},</if>" +
                    "	 <if test=\"teamMemberDTO.dataStatus != null\"> data_status=#{teamMemberDTO.dataStatus,jdbcType=INTEGER},</if>" +
                    "	 <if test=\"teamMemberDTO.updateBy != null\"> update_by=#{teamMemberDTO.updateBy,jdbcType=VARCHAR},</if>" +
                    "   update_time=now()," +
                    "    id = #{teamMemberDTO.id,jdbcType=BIGINT}" +
                    "   where id = #{teamMemberDTO.id,jdbcType=BIGINT} and data_status = 1 ",
            "</script>"})
    int updateTeamMember(@Param("teamMemberDTO") TeamMemberDTO teamMemberDTO);

    @Delete("update  team_member  set data_status = 0 WHERE id =#{id}")
    int deleteTeamMemberById(Long id);


}
