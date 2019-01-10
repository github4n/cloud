package com.iot.design.project.service.impl;

import com.iot.common.helper.Page;
import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.mapper.ProjectMapper;
import com.iot.design.project.service.IProjectService;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Service
public class ProjectServiceImpl implements IProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public void addProject(ProjectDTO projectDTO) throws Exception {
        projectMapper.insertProject(projectDTO);
    }

    @Override
    public void updateProject(ProjectDTO projectDTO) throws Exception {
        // projectMapper.countProjectByNameWithOutId("测试1111", null);

        ProjectDTO projectDTOTemp = projectMapper.findProjectById(projectDTO.getId());
        projectDTOTemp.setName(projectDTO.getName());
        projectDTOTemp.setTenantId(projectDTO.getTenantId());

        projectMapper.updateProject(projectDTOTemp);
    }

    @Override
    public void deleteProject(Long id) throws Exception {
        projectMapper.deleteProjectById(id);
    }

    @Override
    public Page<ProjectPageResp> pageProjects(ProjectPageReq projectPageReq) {
        Page<ProjectPageResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(projectPageReq.getPageNum(), projectPageReq.getPageSize());
        List<ProjectPageResp> respList = projectMapper.pageProjects(pagination, projectPageReq);
        pageResult.setPageNum(projectPageReq.getPageNum());
        pageResult.setPageSize(projectPageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(respList);
        return pageResult;
    }

    @Override
    public ProjectPageResp getProjectById(Long projectId) {
        ProjectDTO projectDTO = projectMapper.findProjectById(projectId);
        ProjectPageResp projectPageResp = new ProjectPageResp();
        BeanUtils.copyProperties(projectDTO, projectPageResp);
        return projectPageResp;
    }

}
