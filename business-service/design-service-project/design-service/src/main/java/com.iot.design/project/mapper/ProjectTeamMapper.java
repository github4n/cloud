package com.iot.design.project.mapper;

import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.dto.ProjectTeamDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Mapper
public interface ProjectTeamMapper {
    @Select({"select * " +
            "from project_team " +
            "where id = #{id,jdbcType=BIGINT} "})
    ProjectTeamDTO findProjectTeamById(@Param("id") Long id);

    @Select({"select * " +
            "from project_team " +
            "where project_id = #{projectId,jdbcType=BIGINT}  "})
    List<ProjectTeamDTO> findProjectTeamByProjectId(@Param("projectId") Long projectId);


    @Insert("INSERT INTO project (   " +
            "    tenant_id,   " +
            "    project_id,   "+
            "    team_id,   " +
            "    create_by,   " +
            "    create_time   " +
            ")   " +
            "VALUES (   " +
            "        #{projectDTO.tenantId,jdbcType=BIGINT},   " +
            "        #{projectDTO.projectId,jdbcType=BIGINT},   " +
            "        #{projectDTO.teamId,jdbcType=BIGINT},   " +
            "        #{projectDTO.createBy,jdbcType=VARCHAR},   " +
            "        now()   " +
            "    )")
    int insertProject(@Param("projectDTO") ProjectDTO projectDTO);



    @Delete("delete from project_team  WHERE project_id =#{projectId}")
    int deleteProjectTeamByProjectId(Long projectId);


    @Delete("delete from project_team  WHERE team_id =#{teamId}")
    int deleteProjectTeamByTeamId(Long teamId);

}
