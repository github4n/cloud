package com.iot.center.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;

/**
 * @Author: Xieby
 * @Date: 2018/8/31
 * @Description: *
 */
@RestController
@RequestMapping("/ipc")
public class IPCController {


	@RequestMapping(value="/stream",produces="application/json;charset=UTF-8")
	@ResponseBody
    public CommonResponse<String> getNameList(@RequestBody JSONObject jsonParam) {
		// 直接将json信息打印出来
	    System.out.println(jsonParam.toJSONString());
        return CommonResponse.success();
    }
}
