package com.iot.control.allocation.api;

import com.iot.common.helper.Page;
import com.iot.control.allocation.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@Api("批量下发配置接口")
@FeignClient(value = "control-service")
//@FeignClient(value = "control-service-xby", fallbackFactory = AllocationApiFallbackFactory.class)
@RequestMapping("/allocation")
public interface AllocationApi {

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/queryAllocationNameList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<AllocationNameResp> queryAllocationNameList(@RequestBody AllocationNameReq allocationNameReq);

    @ApiOperation("查询配置信息列表")
    @RequestMapping(value = "/queryAllocationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<AllocationResp> queryAllocationList(@RequestBody AllocationReq req);

    @ApiOperation("保存配置信息")
    @RequestMapping(value = "/saveAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveAllocation(@RequestBody AllocationReq req);

    @ApiOperation("编辑配置信息")
    @RequestMapping(value = "/editAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editAllocation(@RequestBody AllocationReq req);

    @ApiOperation("删除配置信息")
    @RequestMapping(value = "/delete")
    void deleteAllocation(@RequestParam("id") Long id);

    @ApiOperation("查询配置信息")
    @RequestMapping(value = "/selectById", method = RequestMethod.POST)
    AllocationResp selectById(@RequestParam("id") Long id);

    @ApiOperation("保存执行日志")
    @RequestMapping(value = "/saveExeLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveExeLog(@RequestBody ExecuteLogReq req);

    @ApiOperation("查询错误日志")
    @RequestMapping(value = "/queryErrorLog", method = RequestMethod.POST)
    ExecuteLogReq queryErrorLog(@RequestParam("id") Long id);

    @ApiOperation("执行下发配置")
    @RequestMapping(value = "/executeIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void executeIssue(@RequestBody AllocationReq data);
}
