package com.iot.cloud.service.impl;

import com.iot.cloud.service.IProjectService;
import com.iot.common.helper.Page;
import com.iot.common.util.IOUtil;
import com.iot.design.project.controller.ProjectApi;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.req.ProjectReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Service
public class ProjectServiceImpl implements IProjectService {
    private String savePath = ProjectServiceImpl.class.getClass().getResource("/").getPath() + "projectpic/";
    @Autowired
    private ProjectApi projectApi;

    @Override
    public void addProject(ProjectReq projectVO) throws Exception {
        projectApi.addProject(projectVO);
    }

    @Override
    public void updateProject(ProjectReq projectVOReq) throws Exception {
        projectApi.updateProject(projectVOReq);
    }

    @Override
    public Page<ProjectPageResp> pageProjects(ProjectPageReq projectPageReq) throws Exception {
        return projectApi.pageProjects(projectPageReq);
    }

    @Override
    public ProjectPageResp getProjectById(Long projectId) throws Exception {
        return projectApi.getProjectById(projectId);
    }

    @Override
    public String uploadProjectPic(MultipartFile file) throws Exception {
        //文件保存到本地
//        String path=getPath();
        // IOUtil.copyInputToFile(file.getInputStream(),"");
        //返回本地的路径

        return "";
    }

//    public String getPath() {
//       //判断下面有没有今年的文件夹
//        File file=new File(savePath) ;
//        return path;
//    }
}
