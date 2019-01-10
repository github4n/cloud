package com.iot.control.allocation.api.callback;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.helper.Page;
import com.iot.control.allocation.api.AllocationApi;
import com.iot.control.allocation.vo.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
@Component
public class AllocationApiFallbackFactory implements FallbackFactory<AllocationApi> {

    private Logger log = LoggerFactory.getLogger(AllocationApiFallbackFactory.class);

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
            public void deleteAllocation(Long id) {
                log.error("delete id = " + id);
            }

            @Override
            public AllocationResp selectById(Long id) {
                return null;
            }

            @Override
            public void saveExeLog(ExecuteLogReq req) {

            }

            @Override
            public ExecuteLogReq queryErrorLog(Long id) {
                return null;
            }

            @Override
            public void executeIssue(AllocationReq data) {

            }
        };
    }
}
