package com.iot.shcs.ifttt;

import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.ifttt.entity.Automation;
import com.iot.shcs.ifttt.mapper.AutomationMapper;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.template.service.impl.PackageServiceImpl;
import com.iot.shcs.template.vo.InitPackReq;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/18 9:00
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTest {
    private final Logger logger = LoggerFactory.getLogger(DataTest.class);

    @Autowired
    private AutomationMapper automationMapper;

    @Autowired
    private IAutoService autoService;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private PackageServiceImpl packageService;

    @Test
    public void getList(){
        Automation entity = new Automation();
        entity.setUserId(1073l);
        entity.setSpaceId(890l);
        entity.setTenantId(0l);
        List<Automation> tempList = automationMapper.findSimpleList(entity);
        logger.debug("list(), 从数据库查询出的 tempList.size()={}", tempList.size());
    }

    @Test
    public void test1(){
        SaaSContextHolder.setCurrentTenantId(2l);
        autoService.getPayloadById(2021l);
    }

    @Test
    public void test2(){
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("a6cb2641297211b18c3e0c32cc1ddbd6");

        List<ListDeviceInfoRespVo> deviceList = deviceCoreService.listDevicesByDeviceIds(deviceIds);
        log.info(deviceList.toString());
    }

    @Ignore
    @Test
    public void testBundle() {
        InitPackReq req = InitPackReq.builder().tenantId(1L).directDeviceId("d:siren:id").userUuId("u:12345").spaceId(78L).build();
        String json = "{\n" +
                "\t\"securityId\": \"\",\n" +
                "\t\"securityType\": \"stay\",\n" +
                "\t\"enabled\": 0,\n" +
                "\t\"defer\": 30,\n" +
                "\t\"delay\": 30,\n" +
                "\t\"if\": {\n" +
                "\t\t\"trigger\": [{\n" +
                "\t\t\t\"idx\": 0,\n" +
                "\t\t\t\"trigType\": \"dev\",\n" +
                "\t\t\t\"devId\": \"1090210042\",\n" +
                "\t\t\t\"attr\": \"Door\",\n" +
                "\t\t\t\"compOp\": \"==\",\n" +
                "\t\t\t\"value\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"idx\": 1,\n" +
                "\t\t\t\"trigType\": \"dev\",\n" +
                "\t\t\t\"devId\": \"1090210042\",\n" +
                "\t\t\t\"attr\": \"Door\",\n" +
                "\t\t\t\"compOp\": \"==\",\n" +
                "\t\t\t\"value\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"idx\": 2,\n" +
                "\t\t\t\"trigType\": \"dev\",\n" +
                "\t\t\t\"devId\": \"1090210056\",\n" +
                "\t\t\t\"attr\": \"Door\",\n" +
                "\t\t\t\"compOp\": \"==\",\n" +
                "\t\t\t\"value\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"idx\": 3,\n" +
                "\t\t\t\"trigType\": \"dev\",\n" +
                "\t\t\t\"devId\": \"1090210056\",\n" +
                "\t\t\t\"attr\": \"Door\",\n" +
                "\t\t\t\"compOp\": \"==\",\n" +
                "\t\t\t\"value\": \"1\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"then\": [{\n" +
                "\t\t\"idx\": 0,\n" +
                "\t\t\"thenType\": \"dev\",\n" +
                "\t\t\"id\": \"1090210050\",\n" +
                "\t\t\"attr\": {\n" +
                "\t\t\t\"warning_duration\": 60,\n" +
                "\t\t\t\"warning_mode\": \"fire\",\n" +
                "\t\t\t\"siren_level\": \"high\",\n" +
                "\t\t\t\"strobe\": \"on\",\n" +
                "\t\t\t\"strobe_level\": \"high\"\n" +
                "\t\t}\n" +
                "\t}]\n" +
                "}";
        Map<String, Queue<String>> map = new HashMap<>();
        map.put("1090210050", new LinkedList<>());
        map.get("1090210050").add("d:siren:id");
        map.put("1090210042", new LinkedList<>());
        map.get("1090210042").add("d:door:id1");
        map.get("1090210042").add("d:door:id2");
        map.put("1090210056", new LinkedList<>());
        map.get("1090210056").add("d:motion:id");
        SaaSContextHolder.setCurrentTenantId(1L);
        packageService.generateRule(req, 0, json, map);

    }
}
