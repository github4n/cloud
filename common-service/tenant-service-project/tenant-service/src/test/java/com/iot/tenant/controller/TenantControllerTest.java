package com.iot.tenant.controller;

import com.iot.BaseTest;
import com.iot.common.util.ToolUtil;
import com.iot.tenant.vo.req.SaveTenantReq;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:53 2018/4/27
 * @Modify by:
 */
public class TenantControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/tenant/";
    }

    @Test
    public void addTenant() {
        SaveTenantReq req = new SaveTenantReq();
        req.setCellphone("4535453");
        req.setName("43w5345");
        req.setEmail("2353456464564243");
        req.setCode(ToolUtil.getUUID());
        mockPost("addTenant", req);
    }

    @Test
    public void getTenantById() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "989764105741332482");
        mockGet("getTenantById", params);
    }

    @Test
    public void getTenantByCode() {
        Map<String, String> params = new HashMap<>();
        params.put("code", "c0adb1b0fbbf4fc786072406251f1772");
        mockGet("getTenantByCode", params);
    }
}