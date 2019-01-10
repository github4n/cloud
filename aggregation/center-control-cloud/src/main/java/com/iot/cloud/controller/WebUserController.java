package com.iot.cloud.controller;

import com.iot.cloud.helper.BusinessExceptionEnum;
import com.iot.cloud.service.impl.SpaceService;
import com.iot.cloud.util.ConstantUtil;
import com.iot.cloud.vo.LoginVo;
import com.iot.cloud.vo.UserInfoVo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.control.space.vo.LocationResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ConstantUtil.commonPath+"/")
@Api(tags = {"登录相关"})
public class WebUserController {

	@Autowired
	private ConstantUtil constantUtil;

	@Autowired
	private UserApi userService;
	@Autowired
	private DeviceControlApi deviceControlApi;
	@Autowired
	private SpaceService spaceService;
	@Autowired
    private Environment environment;

	private static final Logger log = LoggerFactory.getLogger(WebUserController.class);

	@ResponseBody
	@ApiOperation("登录")
	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse<UserInfoVo> login(
			HttpServletRequest request,
			@RequestBody LoginVo loginVo) {
		String username=loginVo.getUsername();
		String password=loginVo.getPassword();
		System.out.println("username:"+username+",password:");
		// 当前Subject
		Subject currentUser = SecurityUtils.getSubject();
		if (!verifyParam(username, password)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
		}
		// 加密（md5+盐），返回一个32位的字符串小写
//		String md5Pwd = new Md5Hash(password).toString();
		String md5Pwd =password;
		// 改成前端Base64加密 后台在解密成明文
		//String md5Pwd = Base64Utils.decode(password);
		// 传递token给shiro的realm
		UsernamePasswordToken token = new UsernamePasswordToken(username, md5Pwd);
		try {
			SaaSContextHolder.setCurrentTenantId(ConstantUtil.tenantId);
			SaaSContextHolder.setCurrentOrgId(ConstantUtil.locationId);

			currentUser.login(token);
			SecurityUtils.getSubject().getSession().setTimeout(86400 * 1000 * 30 * 12);// 设置登录超时时间24h
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			user.setLocationId(ConstantUtil.locationId);
			FetchUserResp userInfo=userService.getUser(user.getUserId());
			userInfo.setLocationId(ConstantUtil.locationId);
			UserInfoVo vo=new UserInfoVo();
			BeanUtil.copyProperties(userInfo, vo);
			vo.setMqttHost(constantUtil.getMqttHost());
			vo.setMqttUuid(constantUtil.getAdminUuid());//配置的uuid
			vo.setMqttPassword(constantUtil.getAdminPwd());//配置的pwd
			SaaSContextHolder.setCurrentUserUuid(userInfo.getUuid());
			SaaSContextHolder.setCurrentUserId(userInfo.getId());
			return  CommonResponse.success(vo);
		} catch (AuthenticationException e) {// 登录失败
			throw e;
		}
	}

	@RequestMapping(value = "/getTenantId", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<Map<String,Object>> getTenantId() {
		Long tenantId=constantUtil.getTenantId();
		Map<String,Object> backMap=new HashMap<String, Object>();
		backMap.put("tenantId", tenantId);
		return CommonResponse.success(backMap);
	}

	@RequestMapping(value = "/getLocation", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<List<LocationResp>> getLocation(Long tenantId) {
		List<LocationResp> locationRespList=null;
		if(tenantId !=null) {
			locationRespList=spaceService.getLocationByTenantId(tenantId);
		}
		return CommonResponse.success(locationRespList);
	}

	/**
	 * 判断用户是否为超级管理员
	 * @param username
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "/checkAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<FetchUserResp> checkAdmin(String username, Long tenantId) {
		boolean flag = false;
		if(StringUtils.isNotBlank(username)&&tenantId!=0){
			FetchUserResp userInfo = userService.getUserByUserNameTenantId(username, tenantId);
			return CommonResponse.success(userInfo);
		}
		return null;
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<ResultMsg> logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	@RequestMapping(value = "/loginError", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<ResultMsg> loginError() {
		return new CommonResponse<ResultMsg>(-1, "please login");
	}

	@RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<ResultMsg> register(HttpServletRequest request, String username, String password) {
		if (!verifyParam(username, password)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
		}
		Long tenantId=constantUtil.getTenantId();;
		SaaSContextHolder.setCurrentTenantId(tenantId);
		RegisterReq user = createTempUser(username, new Md5Hash(password).toString());
		userService.register(user);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	/**
	 * 验证用户信息
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	private Boolean verifyParam(String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return false;
		}
		return true;
	}

	/**
	 * wanglei
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	private RegisterReq createTempUser(String username, String password) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RegisterReq user=new RegisterReq();
		user.setUserName(username);
		user.setPassWord(new Md5Hash(password, username).toString());
		user.setEmail(username);
		Integer status=1;
		user.setAdminStatus(status);
		return user;
	}

	/**
	 * 根据用户名获取用户信息
	 *
	 * @param request
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = { RequestMethod.POST })
	public CommonResponse<FetchUserResp> userInfo(HttpServletRequest request, String userId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		FetchUserResp userInfo=userService.getUser(user.getUserId());
		return CommonResponse.success(userInfo);
	}


}
