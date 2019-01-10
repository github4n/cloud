package com.iot.building.allocation.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.allocation.entity.ExecuteLog;
import com.iot.common.helper.Page;
import com.iot.building.allocation.vo.*;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
public interface IAllocationService {

    /**
     * 获取配置名称列表
     */
    Page<AllocationNameResp> getPageList(AllocationNameReq req);

    /**
     * 获取配置信息列表
     */
    Page<AllocationResp> getInfoList(AllocationReq req);

    /**
     * 保存配置信息
     */
    Long saveAllocation(AllocationReq req);

    void editAllocation(AllocationReq req);

    void deleteAllocation(Long tenantId,Long orgId, Long id);

    AllocationResp selectById(Long tenantId, Long orgId,Long id);

    void saveExecuteLog(ExecuteLogReq req);

    List<ExecuteLog> queryLogList(Long tenantId,Long locationId, Long functionId);

    ExecuteLogReq queryErrorLog(Long tenantId,Long orgId,Long locationId, Long id);

    void executeIssue(AllocationReq req) throws Exception;
    
    void iftttTemplateToIftttBySpace(Long tenantId,Long orgId, Long deployId,Long spaceId);
}
