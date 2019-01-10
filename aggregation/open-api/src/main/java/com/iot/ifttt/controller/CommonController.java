package com.iot.ifttt.controller;

import com.iot.ifttt.interceptor.IftttTokenInterceptor;
import com.iot.ifttt.service.CommonService;
import com.iot.ifttt.util.TestDataUtil;
import com.iot.ifttt.vo.CommonReq;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：公共接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/22 20:29
 */
@Slf4j
@Api(value = "公共接口")
@RestController
@RequestMapping("/ifttt/v1")
public class CommonController extends BaseController {

    @Autowired
    private UserApi userApi;

    @Autowired
    private CommonService commonService;

    @ApiOperation("状态监测")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public void status() {
        log.info("收到状态监测请求");
        //检测服务秘钥
        checkKey(commonService.getKey());
    }

    @ApiOperation("测试初始化")
    @RequestMapping(value = "/test/setup", method = RequestMethod.POST)
    public String test() {
        //验证key值是否一致
        log.info("测试初始化");

        //检测服务秘钥
        checkKey(commonService.getKey());
        return TestDataUtil.setup();
    }

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public String userInfo() {
        //验证key值是否一致
        log.info("收到获取用户信息请求");

        //获取用户信息
        FetchUserResp userResp = userApi.getUserByUuid(IftttTokenInterceptor.getUserUUID());

        String res = "{\n" +
                "  \"data\":  {\n" +
                "    \"name\": \"" + userResp.getUserName() + "\",\n" +
                "    \"id\": \"" + userResp.getUuid() + "\"\n" +
                "  }\n" +
                "}";
        return res;
    }

    @ApiOperation("trigger获取下拉列表")
    @RequestMapping(value = "/{type}/{name}/fields/objectId/options", method = RequestMethod.POST)
    public String commonOptions(@PathVariable("type") String type, @PathVariable("name") String name) {
        log.debug(type + "收到获取下拉列表请求:" + name);
        if ("scene_sensor".equals(name)) {
            return resList(commonService.getSceneMap());
        } else {
            return resList(commonService.getDeviceMap(name));
        }
    }

    @ApiOperation("触发接口")
    @RequestMapping(value = "/triggers/{name}", method = RequestMethod.POST)
    public String triggers(@RequestBody CommonReq req, @PathVariable("name") String name) {
        req.setName(name);
        log.debug("接收到公共触发请求" + req.toString());
        return resItem(commonService.checkApplet(req), req.getLimit());
    }

    @ApiOperation("控制设备")
    @RequestMapping(value = "/actions/{name}", method = RequestMethod.POST)
    public String actions(@RequestBody CommonReq req, @PathVariable("name") String name) {
        req.setName(name);
        log.info("接收到公共控制设备请求" + req.toString());
        commonService.action(req);
        return resAction();
    }
}
