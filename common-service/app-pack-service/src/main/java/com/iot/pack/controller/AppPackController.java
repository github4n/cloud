package com.iot.pack.controller;

import com.iot.pack.service.AppPackService;
import com.iot.pack.vo.PackReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 描述：app打包控制器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/28 15:59
 */
@RestController
@EnableAutoConfiguration
public class AppPackController {

    @Autowired
    private AppPackService appPackService;

    @PostMapping("/pack")
    public Boolean pack(@RequestBody PackReq req) {
        return appPackService.pack(req);
    }


    @PostMapping("/validation")
    public Boolean validation(@RequestBody Map map){
        return appPackService.validation(map);
    }

    @GetMapping("test")
    public Boolean test(){
        appPackService.test();
        return true;
    }
}
