package com.iot.user;

import com.alibaba.fastjson.JSON;
import com.iot.user.service.UserService;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {

    }


    @Test
    public void getUserToken() {
        userService.getUserToken("169b1236-c59b-4bda-824a-9be9039a827c");
    }

    @Test
    public void loginTest() {
        LoginReq req = new LoginReq();
        req.setUserName("yuchangxing@leedarson.com");
        req.setPwd("bf82579e376247aa03775da645dcb15a");
        req.setLastIp("192.168.1.111");
        req.setTerminalMark("APP");
        LoginResp res = userService.login(req);
        System.out.print("结果：" + res.toString());
    }

    @Test
    public void registerTest() {
        for (int i = 0; i < 1000; i++) {
            RegisterReq req = new RegisterReq();
            req.setEmail("testuser" + i + "@qq.com");
            req.setPassWord("e10adc3949ba59abbe56e057f20f883e");
            req.setUserName("testuser" + i + "@qq.com");
            Long userId = userService.registerMock(req);
            System.out.println("用户创建结果,userId=" + userId);
        }
    }

    @Test
    public void testJson() {
        String json = JSON.toJSONString("03fbaba9354d4072920f45c4ea1d40b0");
        System.out.println(json);
        String jsonRes = JSON.parseObject(json, String.class);
        System.out.println(jsonRes);

        String jsonInt = JSON.toJSONString(123);
        System.out.println(jsonInt);
        Integer jsonIntRes = JSON.parseObject(jsonInt, Integer.class);
        System.out.println(jsonIntRes);
    }
}
