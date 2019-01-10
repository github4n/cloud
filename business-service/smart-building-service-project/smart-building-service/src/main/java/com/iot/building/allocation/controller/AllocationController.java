package com.iot.building.allocation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.allocation.api.AllocationApi;
import com.iot.building.allocation.service.IAllocationService;
import com.iot.common.helper.Page;
import com.iot.building.allocation.vo.*;

/**
 * @Author: Xieby
 * @Date: 2018/8/31
 * @Description: *
 */
@RestController
public class AllocationController implements AllocationApi {

    @Autowired
    private IAllocationService allocationService;

    @Override
    public Page<AllocationNameResp> queryAllocationNameList(@RequestBody AllocationNameReq allocationNameReq) {
        return allocationService.getPageList(allocationNameReq);
    }

    @Override
    public Page<AllocationResp> queryAllocationList(@RequestBody AllocationReq req) {
        return allocationService.getInfoList(req);
    }

    @Override
    public Long saveAllocation(@RequestBody AllocationReq req) {
        return allocationService.saveAllocation(req);
    }

    @Override
    public void editAllocation(@RequestBody AllocationReq req) {
        allocationService.editAllocation(req);
    }

    @Override
    public void deleteAllocation(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id) {
        allocationService.deleteAllocation(tenantId,orgId,id);
    }

    @Override
    public AllocationResp selectById(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id) {
        return allocationService.selectById(tenantId,orgId,id);
    }

    @Override
    public void saveExeLog(@RequestBody ExecuteLogReq req) {
        allocationService.saveExecuteLog(req);
    }

    @Override
    public ExecuteLogReq queryErrorLog(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, 
    		@RequestParam("locationId") Long locationId,@RequestParam("id") Long id) {
        return allocationService.queryErrorLog(tenantId,orgId,locationId,id);
    }

    @Override
    public void executeIssue(@RequestBody AllocationReq data) throws Exception{
        allocationService.executeIssue(data);
    }
}
