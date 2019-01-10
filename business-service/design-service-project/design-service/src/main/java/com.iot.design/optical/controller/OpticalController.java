package com.iot.design.optical.controller;

import com.alibaba.fastjson.JSON;
import com.iot.design.optical.OpticalApi;
import com.iot.design.optical.service.OpticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 光学业务
 *
 * @author fenglijian
 */
@RestController
public class OpticalController implements OpticalApi {
    @Autowired
    private OpticalService opticalService;

    @Override
    public void readFile(@RequestParam("fileId") String fileId) {
        opticalService.readFile(fileId);
    }

    @Override
    public void addWZZS2Redis(@RequestBody String wzzsJSON) {
        Map<String,Float> wzzsMap= (Map<String, Float>) JSON.parse(wzzsJSON);
        opticalService.addWZZS2Redis(wzzsMap);
    }

}
