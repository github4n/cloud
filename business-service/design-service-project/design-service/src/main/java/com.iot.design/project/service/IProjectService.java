package com.iot.design.project.service;

import com.iot.common.helper.Page;
import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.resp.ProjectPageResp;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
public interface IProjectService {

    void addProject(ProjectDTO projectDTO) throws Exception;

    void updateProject(ProjectDTO projectDTO) throws Exception;

    void deleteProject(Long id)throws Exception;

    Page<ProjectPageResp> pageProjects(ProjectPageReq projectPageReq);

    ProjectPageResp getProjectById(Long projectId);
}
