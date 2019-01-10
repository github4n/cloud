package com.iot.device.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:23 2018/5/17
 * @Modify by:
 */

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserApi userApi;

    @LoginRequired(value = Action.Skip)
    @RequestMapping("/test")
    public Object test(@RequestParam("id") Long id) {
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("id", 2496964856749657398L);
        returnData.put("rest", "hello");
        returnData.put("inputId", id);
        Long userId = 1L;
        FetchUserResp userResp = userApi.getUser(userId);
        returnData.put("user", userResp);
        return returnData;
    }
}
