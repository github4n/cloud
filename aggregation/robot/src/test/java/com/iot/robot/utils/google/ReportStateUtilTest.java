package com.iot.robot.utils.google;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/21 16:06
 * @Modify by:
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReportStateUtilTest{

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void callReportState() {
        String jsonContent = "{\"agent_user_id\":\"feadbabe75df45a29d7f775e4e1c671c\",\"payload\":{\"devices\":{\"states\":{\"f845355ec6014fe4ac9e881a8c8579fd\":{\"on\":true}}}},\"requestId\":\"dd6b4c00-d9df-47ba-be71-31308c39b379\"}";
        ReportStateUtil.callReportState(jsonContent, null);
        System.out.println("*********");
    }
}