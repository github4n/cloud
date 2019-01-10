package com.iot.tenant.controller;

import com.iot.BaseTest;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.AddUserReq;
import com.iot.tenant.vo.req.AddVirtualOrgReq;
import org.junit.Test;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:53 2018/4/27
 * @Modify by:
 */
public class VirtualOrgControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/virtualOrg/";
    }

    @Test
    public void addVirtualOrg() {
        AddVirtualOrgReq req = new AddVirtualOrgReq();
        req.setName("test org");
        req.setTenantId(989764105741332482L);
        req.setDescription("test");
        mockPost("addVirtualOrg", req);
    }

    @Test
    public void addUserOrg() {

        AddUserOrgReq req = new AddUserOrgReq();
        AddVirtualOrgReq orgReq = new AddVirtualOrgReq();
        orgReq.setDescription("test org");
        orgReq.setName("test org");
        orgReq.setTenantId(1001L);
        orgReq.setDescription("test");
        req.setOrgReq(orgReq);

        AddUserReq userReq = new AddUserReq();
        userReq.setUserId(100001L);
        req.setUserReq(userReq);

        mockPost("addUserOrg", req);
    }
}