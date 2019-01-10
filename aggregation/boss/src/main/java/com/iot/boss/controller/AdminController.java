package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.boss.service.user.UserService;
import com.iot.boss.vo.user.SendEmailReq;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "admin接口",value ="admin接口")
@RequestMapping("/api/admin")
public class AdminController {

	private Logger log = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private UserApi userApi;

	@Autowired
    private UserService userService;
	
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse<LoginResp> save(@RequestBody LoginReq req) {
    	log.debug("登陆信息：{}",JSONObject.toJSON(req));
		LoginResp user = userService.userLogin(req);
		return  ResultMsg.SUCCESS.info(user);
	}
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "用户退出", notes = "用户退出")
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public CommonResponse userLogout(HttpServletRequest request) {
        String terminalMark = request.getHeader(UserConstants.HEADER_TERMINALMARK); //header 終端類型 app web等
        Long userId = SaaSContextHolder.getCurrentUserId();
        this.userApi.logout(userId, terminalMark);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "创建子账户", notes = "创建子账户")
    @RequestMapping(value = "/createSubUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createSubUser(@RequestBody RegisterReq user) {
        this.userService.createSubUser(user);
        return ResultMsg.SUCCESS.info();
    }


    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Long> userRegister(@RequestBody RegisterReq req) {
        log.debug("user register message:{}",JSONObject.toJSON(req));
        Long userId = userService.userRegister(req);
        return  ResultMsg.SUCCESS.info(userId);
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "发送验证码", notes = "type 注册验证码:1 密码重置验证码:2 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendVerifyCode", method = {RequestMethod.POST,RequestMethod.GET})
    public CommonResponse sendVerifyCode(@RequestBody SendEmailReq req) {
        this.userService.sendVerifyCode(req.getEmail(),req.getType());
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "校验用户账号是否存在", notes = "校验用户账号是否存在")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/checkUserName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse checkUserName(@RequestBody RegisterReq req) {
        this.userService.checkUserName(req);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation("Boss用户授权")
    @RequestMapping(value = "/auditUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse auditUser(@RequestBody RegisterReq registerReq){
        this.userService.auditUser(registerReq);
        return ResultMsg.SUCCESS.info();
    }
}
