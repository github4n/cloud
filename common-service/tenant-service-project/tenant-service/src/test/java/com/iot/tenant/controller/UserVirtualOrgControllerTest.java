package com.iot.tenant.controller;

import com.iot.BaseTest;
import com.iot.tenant.vo.req.AddUserVirtualOrgReq;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:53 2018/4/27
 * @Modify by:
 */
public class UserVirtualOrgControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/userVirtualOrgApi/";
    }

    @Test
    public void addUserVirtualOrg() {
        AddUserVirtualOrgReq userVirtualOrgReq = new AddUserVirtualOrgReq();
        userVirtualOrgReq.setUserId(1000001L);
        userVirtualOrgReq.setOrgId(989765133488394241L);

        mockPost("addUserVirtualOrg", userVirtualOrgReq);
    }

    @Test
    public void getOrgInfoByUserId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1000001");
        mockGet("getOrgInfoByUserId", params);
    }

    @Test
    public void getDefaultUsedOrgInfoByUserId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1000001");
        mockGet("getDefaultUsedOrgInfoByUserId", params);
    }

    @Test
    public void deleteOrgByUserIdAndOrgId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1000001");
        params.put("orgId", "989765133488394241");
        mockGet("deleteOrgByUserIdAndOrgId", params);
    }
}