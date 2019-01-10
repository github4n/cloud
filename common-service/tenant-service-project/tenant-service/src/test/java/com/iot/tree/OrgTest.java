package com.iot.tree;

import com.iot.BaseTest;
import com.iot.common.helper.Page;
import com.iot.tenant.TenantServiceApplication;
import com.iot.tenant.api.VirtualOrgApi;
import com.iot.tenant.controller.VirtualOrgController;
import com.iot.tenant.vo.req.org.GetOrgByPageReq;
import com.iot.tenant.vo.req.org.SaveOrgReq;
import com.iot.tenant.vo.resp.org.OrgResp;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 描述：组织树测试
 * 创建人：LaiGuiMing
 * 创建时间：2019/01/07 11:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TenantServiceApplication.class)
@Slf4j
public class OrgTest{

    @Autowired
    private VirtualOrgApi virtualOrgApi;

    @Autowired
    private VirtualOrgController virtualOrgController;

    @Test
    public void add(){
        /*SaveOrgReq req = new SaveOrgReq();
        req.setName("二级组织");
        req.setDescription("二级组织 描述");
        req.setOrderNum(1);
        req.setParentId(991495690677154940l);
        req.setTenantId(1l);
        req.setType(0);
        virtualOrgApi.add(req);*/

        /*GetOrgByPageReq req = new GetOrgByPageReq();
        req.setPageSize(1);
        req.setPageNum(1);
        req.setName("组织");
        Page<OrgResp> page= virtualOrgController.selectByPage(req);
        log.info(page.toString());*/

        /*List<OrgResp> list = virtualOrgController.getChildrenTree(991495690677154940l);
        log.info(list.toString());*/

        List<Long> ids = Lists.newArrayList();
        ids.add(991495690677154940l);
        ids.add(991495690677154941l);
        virtualOrgApi.del(ids);
    }
}
