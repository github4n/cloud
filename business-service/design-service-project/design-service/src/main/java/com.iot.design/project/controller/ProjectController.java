package com.iot.design.project.controller;

import com.iot.common.helper.Page;
import com.iot.design.project.controller.base.BaseController;
import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.service.IProjectService;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.req.ProjectReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/27
 */
@RestController
public class ProjectController extends BaseController implements ProjectApi {
    @Autowired
    private IProjectService projectService;

    @Override
    public Page<ProjectPageResp> pageProjects(@RequestBody ProjectPageReq projectPageReq) {
        return projectService.pageProjects(projectPageReq);
    }

    @Override
    public ProjectPageResp getProjectById(Long projectId) {
        return projectService.getProjectById(projectId);
    }

    public void addProject(@RequestBody ProjectReq projectVOReq
    ) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(projectVOReq, projectDTO);
        projectDTO.setCreateTime(new Date());
        projectService.addProject(projectDTO);
    }


    public void updateProject(@RequestBody ProjectReq projectVOReq
    ) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(projectVOReq, projectDTO);
        projectDTO.setUpdateTime(new Date());
        projectService.updateProject(projectDTO);
    }

    public void deleteProject(
            @ApiParam(name = "id", value = "项目id", required = true) @RequestParam(value = "id", required = true) Long id
    ) throws Exception {
        projectService.deleteProject(id);

    }


}
