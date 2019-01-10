package com.iot.control.share.web;

import com.google.common.collect.Maps;
import com.iot.control.BaseTest;
import com.iot.control.share.vo.req.AddShareSpaceReq;
import com.iot.saas.SaaSContextHolder;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ShareSpaceControllerTest extends BaseTest {

    private Long tenantId = 1L;

    @Override
    public String getBaseUrl() {
        return "/shareSpace/";
    }

    @Test
    public void saveOrUpdate() {
        Object content = AddShareSpaceReq.builder()
                .tenantId(SaaSContextHolder.currentTenantId())
                .fromUserId(27L)
                .fromUserUuid("a521b4b80aa94494907eb570683239b6")
                .toUserId(23L)
                .toUserUuid("711f085e61c141dbaa1f5b491f0aeb58")
                .spaceId(4L)
                .toUserEmail("chenhongqiao@leedarson.com")
                .alias("chenhongqiao@leedarson.com")
                .shareUuid(UUID.randomUUID().toString().replace("-", ""))
                .status(0)
                .remark("inviting " + "chenhongqiao@leedarson.com" + "now.")
                .expireTime(60 * 60 * 24L)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        mockPost("saveOrUpdate", content);
    }

    @Test
    public void getByShareUUID() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("shareUUID", "9976bb739b8149dfbf308b3435b433e6");
        mockGet("getByShareUUID", requestMap);
    }

    @Test
    public void getByToUserId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("spaceId", String.valueOf(4));
        requestMap.put("toUserId", String.valueOf(23));
        mockGet("getByToUserId", requestMap);
    }

    @Test
    public void countByFromUserId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("fromUserId", String.valueOf(27));
        mockGet("countByFromUserId", requestMap);
    }

    @Test
    public void listByFromUserId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("fromUserId", String.valueOf(27));
        mockGet("listByFromUserId", requestMap);
    }

    @Test
    public void listBySpaceId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("spaceId", String.valueOf(4));
        mockGet("listBySpaceId", requestMap);
    }

    @Test
    public void listByToUserId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("toUserId", String.valueOf(1206));
        mockGet("listByToUserId", requestMap);
    }

    @Test
    public void getById() { //test 通过
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("shareId", String.valueOf(5));
        mockGet("getById", requestMap);
    }

    @Test
    public void delById() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("shareId", String.valueOf(5));
        mockGet("delById", requestMap);
    }

    @Test
    public void delBySpaceId() {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("tenantId", String.valueOf(1));
        requestMap.put("spaceId", String.valueOf(4));
        mockPost("delBySpaceId?tenantId=" + String.valueOf(1) + "&spaceId=" + String.valueOf(4), null);
    }
}