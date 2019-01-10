package com.iot.device.controller;

import com.alibaba.fastjson.JSON;
import com.iot.device.BaseTest;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GroupControllerTest extends BaseTest {

    @Autowired
    private GroupController groupController;

    @Test
    public void save() {
        UpdateGroupReq req = new UpdateGroupReq();
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setName("groupName");
        req.setIcon("living.png");
        req.setDevGroupId(123);
        req.setHomeId(1313l);
        Object result = groupController.save(req);
        System.out.println(result);

    }

    @Test
    public void update() {
        UpdateGroupReq req = new UpdateGroupReq();
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setName("group");
        req.setIcon("bed.png");
        req.setId(6l);
        req.setHomeId(1313l);
        Object result = groupController.update(req);
        System.out.println(result);
    }

    @Test
    public void getGroup() {
        UpdateGroupReq req = new UpdateGroupReq();
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setHomeId(1313l);
        Object result = groupController.getGroup(req);
        System.out.println(result);
    }

    @Test
    public void delGroupById() {
    }

    @Test
    public void saveGroupDetial() {
        UpdateGroupDetailReq req = new UpdateGroupDetailReq();
        List<String> member = new ArrayList<>();
        member.add("b6f7c7f3e7a14a74be155d6a0f7f38e1");
        member.add("31b292b79774b3dedf65c30f439543c5");
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setGroupId(6l);
        req.setMembers(member);
        Object result = groupController.saveGroupDetialBatch(req);
        System.out.println(result);
    }

    @Test
    public void saveGroupDetialBatch() {
    }

    @Test
    public void delGroupDetial() {
        UpdateGroupDetailReq req = new UpdateGroupDetailReq();
        List<String> member = new ArrayList<>();
        member.add("b6f7c7f3e7a14a74be155d6a0f7f38e1");
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setGroupId(6l);
        req.setMembers(member);
        Object result = groupController.delGroupDetial(req);
        System.out.println(result);
    }

    @Test
    public void getGroupDetial() {
        UpdateGroupDetailReq req = new UpdateGroupDetailReq();
        req.setTenantId(2l);
        req.setCreateBy(1138l);
        req.setGroupId(6l);
        Object result = groupController.getGroupDetial(req);
        System.out.println(result);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}