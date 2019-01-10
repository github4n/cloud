package com.iot.design.project.controller;

import com.iot.common.helper.Page;
import com.iot.design.project.controller.fallback.ProjectApiFallbackFactory;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.req.ProjectReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/27
 */
@Api("项目相关几口")
@FeignClient(value = "design-service-cwd", fallbackFactory = ProjectApiFallbackFactory.class)
@RequestMapping("project")
public interface ProjectApi {

    @RequestMapping(method = RequestMethod.POST, value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("新增項目")
    void addProject(@RequestBody ProjectReq projectVOReq) throws Exception;

    @RequestMapping(method = RequestMethod.POST, value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("更新項目")
    void updateProject(@RequestBody ProjectReq projectVOReq) throws Exception;

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    @ApiOperation("删除項目")
    void deleteProject(
            @ApiParam(name = "id", value = "项目id", required = true) @RequestParam(value = "id", required = true) Long id
    ) throws Exception;

    @RequestMapping(method = RequestMethod.POST, value = "/pageProjects", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("分页查询项目")
    Page<ProjectPageResp> pageProjects(@RequestBody ProjectPageReq projectPageReq);

    @RequestMapping(method = RequestMethod.GET, value = "/getProjectById")
    @ApiOperation("根据ID获取项目详情")
    ProjectPageResp getProjectById(Long projectId);
}
