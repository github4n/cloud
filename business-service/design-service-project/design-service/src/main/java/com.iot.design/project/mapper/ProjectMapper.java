package com.iot.design.project.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectDTO> {
    @Select({"select * " +
            "from project " +
            "where id = #{id,jdbcType=BIGINT} and data_status = 1 "})
    ProjectDTO findProjectById(@Param("id") Long id);

    /**
     * 获取项目名称是@name 项目id不是@id的数目
     *
     * @param name 项目名称
     * @param id   项目id
     * @return
     */
    @Select({"<script>" +
            " select count(id) " +
            " from project " +
            " where data_status = 1 " +
            " <if test=\"name != null\"> and name=#{name,jdbcType=VARCHAR}</if> " +
            " <if test=\"id != null\"> and id !=#{id,jdbcType=BIGINT} </if> " +
            " </script>"})
    Integer countProjectByNameWithOutId(@Param("name") String name, @Param("id") Long id);

    @Insert("INSERT INTO project (   " +
            "    tenant_id,   " +
            "    data_status,   " +
            "    create_by,   " +
            "    create_time,   " +
            "    name,   " +
            "    type," +
            "    pic,    " +
            "    leader_id,   " +
            "    leader_name,   " +
            "    output    " +
            ")   " +
            "VALUES (   " +
            "        #{projectDTO.tenantId,jdbcType=BIGINT},   " +
            "        #{projectDTO.dataStatus,jdbcType=INTEGER},   " +
            "        #{projectDTO.createBy,jdbcType=VARCHAR},   " +
            "        now(),   " +
            "       #{projectDTO.name,jdbcType=VARCHAR},   " +
            "       #{projectDTO.type,jdbcType=INTEGER},   " +
            "       #{projectDTO.pic,jdbcType=VARCHAR},   " +
            "       #{projectDTO.leaderId,jdbcType=BIGINT},   " +
            "       #{projectDTO.leaderName,jdbcType=VARCHAR},   " +
            "       #{projectDTO.output,jdbcType=INTEGER}   " +
            "    )")
    int insertProject(@Param("projectDTO") ProjectDTO projectDTO);

    @Update({"<script>",
            "update project" +
                    "	set " +
                    "	 <if test=\"projectDTO.tenantId != null\"> tenant_id=#{projectDTO.tenantId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"projectDTO.dataStatus != null\"> data_status=#{projectDTO.dataStatus,jdbcType=INTEGER},</if>" +
                    "	 <if test=\"projectDTO.updateBy != null\"> update_by=#{projectDTO.updateBy,jdbcType=VARCHAR},</if>" +
                    "   update_time=now()," +
                    "	 <if test=\"projectDTO.name != null\"> name=#{projectDTO.name,jdbcType=VARCHAR},</if>" +
                    "	 <if test=\"projectDTO.type != null\"> type=#{projectDTO.type,jdbcType=INTEGER},</if>" +
                    "	 <if test=\"projectDTO.leaderId != null\"> leader_id=#{projectDTO.leaderId,jdbcType=BIGINT},</if>" +
                    "	 <if test=\"projectDTO.leaderName != null\"> leader_name=#{projectDTO.leaderName,jdbcType=VARCHAR},</if>" +
                    "	 <if test=\"projectDTO.output != null\"> output=#{projectDTO.output,jdbcType=INTEGER},</if>" +
                    "    id = #{projectDTO.id,jdbcType=BIGINT}" +
                    "   where id = #{projectDTO.id,jdbcType=BIGINT} and data_status = 1 ",
            "</script>"})
    int updateProject(@Param("projectDTO") ProjectDTO projectDTO);

    @Delete("update  project  set data_status = 0 WHERE id =#{id}")
    int deleteProjectById(Long id);

    @Select({"<script>" +
            " select * " +
            " from project " +
            " where  data_status = 1 and " +
            " name like \"%${name}%\" " +
            "</script>"})
    List<ProjectDTO> listProject(String name);

    @Select({"SELECT   " +
            "    id id,   " +
            "    pic pic,   " +
            "    name name   " +
            " FROM   " +
            "    project    " +
            "WHERE   " +
            "    data_status = 1    "})
    List<ProjectPageResp> pageProjects(Pagination pagination, ProjectPageReq projectPageReq);
}
