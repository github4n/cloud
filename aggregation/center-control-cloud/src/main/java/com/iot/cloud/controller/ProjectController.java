package com.iot.cloud.controller;

import com.iot.cloud.service.IProjectService;
import com.iot.cloud.util.ConstantUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.design.dict.controller.DictApi;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.req.ProjectReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@RestController
@RequestMapping(ConstantUtil.commonPath + "/project")
@Api(tags = "项目模块")
public class ProjectController {
    @Autowired
    private IProjectService projectService;
    @Autowired
    private DictApi dictApi;


    @ApiOperation("新增项目")
    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public CommonResponse addProject(@RequestBody ProjectReq projectVOReq) {
        try {
            projectVOReq.setTenantId(SaaSContextHolder.currentTenantId());
            projectService.addProject(projectVOReq);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("修改项目")
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    public CommonResponse updateProject(@RequestBody ProjectReq projectVOReq) {
        try {
            projectVOReq.setTenantId(SaaSContextHolder.currentTenantId());
            projectService.updateProject(projectVOReq);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("根据ID获取项目详情")
    @RequestMapping(value = "/getProjectById", method = RequestMethod.GET)
    public CommonResponse<ProjectPageResp> getProjectById(@RequestParam(value = "projectId") Long projectId) {
        try {
            ProjectPageResp resp = projectService.getProjectById(projectId);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg()).data(resp);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("删除项目")
    @RequestMapping(value = "/deleteProjectById", method = RequestMethod.POST)
    public CommonResponse deleteProjectById(@RequestParam(value = "projectId") Long projectId) {
        try {

            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("获取项目列表")
    @RequestMapping(value = "/pageProjects", method = RequestMethod.POST)
    public CommonResponse<ProjectPageResp> pageProjects(@RequestBody ProjectPageReq projectPageReq) {
        try {
            Page<ProjectPageResp> page = projectService.pageProjects(projectPageReq);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg()).data(page);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }


    @ApiOperation("项目封面上传")
    @RequestMapping(value = "/uploadProjectPic", method = RequestMethod.POST)
    public CommonResponse uploadProjectPic(@RequestParam(value = "projectId") Long projectId,
                                           @RequestParam(value = "file") MultipartFile file) {
        try {
            String url = projectService.uploadProjectPic(file);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg()).data(url);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("3D模型上传")
    @RequestMapping(value = "/upload3D", method = RequestMethod.POST)
    public CommonResponse upload3D(@RequestParam(value = "projectId") Long projectId,
                                   @RequestParam(value = "file") MultipartFile file) {
        try {

            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("图片上传")
    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public CommonResponse uploadPic(@RequestParam(value = "file") MultipartFile file) {
        try {

            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("3D模型下载")
    @RequestMapping(value = "/download3D", method = RequestMethod.POST)
    public CommonResponse download3D(@RequestParam(value = "projectId", required = true) Long projectId) {
        try {

            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("获取字典")
    @RequestMapping(value = "/getDict", method = RequestMethod.GET)
    public CommonResponse<List> getDict(
            @RequestParam(required = false, name = "tenantId") Long tenantId,
            @RequestParam(required = true, name = "dict") String dict
    ) {
        try {
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg()).data(dictApi.getDict(tenantId, dict));
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }
}
