package com.iot.building.allocation.api.callback;

import org.springframework.stereotype.Component;

import com.iot.building.allocation.api.AllocationApi;
import com.iot.building.allocation.vo.*;
import com.iot.common.helper.Page;

import feign.hystrix.FallbackFactory;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
@Component("buildAllocationApiFallbackFactory")
public class AllocationApiFallbackFactory implements FallbackFactory<AllocationApi> {


    @Override
    public AllocationApi create(Throwable throwable) {
        return new AllocationApi() {
            @Override
            public Page<AllocationNameResp> queryAllocationNameList(AllocationNameReq allocationNameReq) {
                return null;
            }

            @Override
            public Page<AllocationResp> queryAllocationList(AllocationReq req) {
                return null;
            }

            @Override
            public Long saveAllocation(AllocationReq req) {
                return null;
            }

            @Override
            public void editAllocation(AllocationReq req) {
            }

            @Override
            public void saveExeLog(ExecuteLogReq req) {
            }

            @Override
            public void executeIssue(AllocationReq data) {

            }

			@Override
			public void deleteAllocation(Long tenantId, Long orgId, Long id) {
				
			}

			@Override
			public AllocationResp selectById(Long tenantId, Long orgId, Long id) {
				return null;
			}

			@Override
			public ExecuteLogReq queryErrorLog(Long tenantId, Long orgId, Long locationId, Long id) {
				return null;
			}
        };
    }
}
