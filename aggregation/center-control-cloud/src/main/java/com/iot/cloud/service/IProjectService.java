package com.iot.cloud.service;



import com.iot.common.helper.Page;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.req.ProjectReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import org.springframework.web.multipart.MultipartFile;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
public interface IProjectService {

    void addProject(ProjectReq projectVO) throws Exception;

    void updateProject(ProjectReq projectVOReq)throws Exception;

    Page<ProjectPageResp> pageProjects(ProjectPageReq projectPageReq)throws Exception;

    ProjectPageResp getProjectById(Long projectId)throws Exception;

    String uploadProjectPic(MultipartFile file)throws Exception;
}
