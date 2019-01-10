package com.iot.center.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.building.allocation.vo.AllocationNameReq;
import com.iot.building.allocation.vo.AllocationNameResp;
import com.iot.building.allocation.vo.AllocationReq;
import com.iot.building.allocation.vo.AllocationResp;
import com.iot.building.allocation.vo.ExecuteLogReq;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.service.AllocationService;
import com.iot.common.beans.CommonResponse;
import com.iot.common.helper.Page;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.ApiOperation;

/**
 * @Author: Xieby
 * @Date: 2018/8/31
 * @Description: *
 */
@Controller
@RequestMapping("allocation")
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    @SystemLogAnnotation(value = "查询配置功能列表")
    @ApiOperation("查询配置功能列表")
    @RequestMapping("getNameList")
    @ResponseBody
    public CommonResponse<Page<AllocationNameResp>> getNameList(AllocationNameReq req) {
        return allocationService.queryList(req);
    }

    @PermissionAnnotation(value = "BATCH_ALLOCATION")
    @SystemLogAnnotation(value = "查询配置列表")
    @ApiOperation("查询配置列表")
    @RequestMapping("getConfigList")
    @ResponseBody
    public CommonResponse<Page<AllocationResp>> getConfigList(AllocationReq req) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		req.setLocationId(user.getLocationId());
		req.setTenantId(user.getTenantId());
        return allocationService.queryConfigList(req);
    }

    @SystemLogAnnotation(value = "查询配置信息通过ID")
    @ApiOperation("查询配置信息通过ID")
    @RequestMapping("getAllocationById")
    @ResponseBody
    public CommonResponse<AllocationResp> getAllocationById(Long id) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	Long tenantId=user.getTenantId();
    	Long orgId=user.getOrgId();
        return allocationService.getAllocationById(tenantId,orgId,id);
    }

    @PermissionAnnotation(value = "BATCH_ALLOCATION")
    @SystemLogAnnotation(value = "保存配置信息")
    @ApiOperation("保存配置信息")
    @RequestMapping("saveAllocation")
    @ResponseBody
    public CommonResponse saveAllocation(AllocationReq req) {
        return allocationService.saveAllocation(req);
    }

    @PermissionAnnotation(value = "BATCH_ALLOCATION")
    @SystemLogAnnotation(value = "编辑配置信息")
    @ApiOperation("编辑配置信息")
    @RequestMapping("editAllocation")
    @ResponseBody
    public CommonResponse editAllocation(AllocationReq req) {
        return allocationService.editAllocation(req);
    }

    @PermissionAnnotation(value = "BATCH_ALLOCATION")
    @SystemLogAnnotation(value = "删除配置信息")
    @ApiOperation("删除配置信息")
    @RequestMapping("deleteAllocation")
    @ResponseBody
    public CommonResponse deleteAllocation(Long id) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	Long tenantId=user.getTenantId();
    	Long orgId=user.getOrgId();
        return allocationService.deleteAllocation(tenantId,orgId,id);
    }

    @PermissionAnnotation(value = "BATCH_ALLOCATION")
    @SystemLogAnnotation(value = "批量删除配置信息")
    @ApiOperation("批量删除配置信息")
    @RequestMapping("deleteAllocationBatch")
    @ResponseBody
    public CommonResponse deleteAllocationBatch(String ids) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	Long tenantId=user.getTenantId();
    	Long orgId=user.getOrgId();
    	if(StringUtils.isNotBlank(ids)) {
    		String[] idArry=ids.split(",");
    		for(String idStr:idArry) {
    			Long id=Long.valueOf(idStr);
    			allocationService.deleteAllocation(tenantId,orgId,id);
    		}
    	}
    	return CommonResponse.success();
    }

    @SystemLogAnnotation(value = "查询配置的错误日志")
    @ApiOperation("查询配置的错误日志")
    @RequestMapping("queryErrorLog")
    @ResponseBody
    public CommonResponse<ExecuteLogReq> queryErrorLog(Long id) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	Long tenantId=user.getTenantId();
    	Long locationId=user.getLocationId();
    	Long orgId=user.getOrgId();
        return allocationService.queryErrorLog(tenantId,orgId,locationId,id);
    }
}
