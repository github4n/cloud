package com.iot.center.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import com.iot.building.space.vo.LocationResp;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.service.SpaceService;
import com.iot.center.service.UserService;
import com.iot.center.service.WxManagerService;
import com.iot.center.utils.Base64Utils;
import com.iot.center.utils.ConstantUtil;
import com.iot.center.vo.UserInfoVo;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.SpaceResp;
import com.iot.file.api.FileApi;
import com.iot.permission.enums.DataTypeEnum;
import com.iot.permission.vo.PermissionDto;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
public class WebUserController {

	@Autowired
	private UserApi userApi;
	@Autowired
	private Environment environment;
	@Autowired
	private SpaceService spaceService;
	@Autowired
	private ConstantUtil constantUtil;
	@Autowired
	private FileApi fileApi;
	@Autowired
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(WebUserController.class);
	
	@ResponseBody
	@RequestMapping(value = "/login", method = { RequestMethod.POST})
	public CommonResponse<UserInfoVo> login(HttpServletRequest request, String username, String password,Long locationId,Long tenantId) {
		try {
			// 当前Subject
			Subject currentUser = SecurityUtils.getSubject();
			if (!verifyParam(username, password)) {
				throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
			}
			// 传递token给shiro的realm
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			currentUser.login(token);
			SecurityUtils.getSubject().getSession().setTimeout(1000*60*30);// 设置登录超时时间0.5h
			LoginResp user = setUserInfo(locationId, tenantId);
			UserInfoVo vo = getBackVo(locationId, user);
			return CommonResponse.success(vo);
		}catch (AuthenticationException e) {// 登录失败
			if(e.getMessage().contains("Your account has 3 consecutive password errors")) {
				throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR_TIMES_EXCEPTION);
			}else {
				throw new BusinessException(BusinessExceptionEnum.USER_NAME_PASSWORD_EXCEPTION);
			}
		}
	}

	private LoginResp setUserInfo(Long locationId, Long tenantId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		user.setLocationId(locationId);
		SaaSContextHolder.setCurrentUserUuid(user.getUserUuid());
		SaaSContextHolder.setCurrentUserId(user.getUserId());
		SaaSContextHolder.setCurrentTenantId(tenantId);
		SaaSContextHolder.setCurrentOrgId(locationId);
		return user;
	}

	private UserInfoVo getBackVo(Long locationId, LoginResp user) {
		List<PermissionDto> funPermission = (List<PermissionDto>) Constants.userPermission.get(user.getUserId());
		List<UserDataPermissionRelateDto> dataPermission = (List<UserDataPermissionRelateDto>) Constants.dataPermission.get(user.getUserId());
		log.info("result = " + JSON.toJSONString(funPermission));
		UserInfoVo vo = new UserInfoVo();
		vo.setFunctionPermissions(funPermission);
		vo.setMqttHost(constantUtil.getMqttHost());
		vo.setMqttUuid(constantUtil.getAdminUuid());//配置的uuid
		vo.setMqttPassword(constantUtil.getAdminPwd());//配置的pwd
		vo.setLocationId(locationId);
		vo.setTenantId(user.getTenantId());
		vo.setDataPermissions(userService.getDataPermission(user.getTenantId(),dataPermission));
		return vo;
	}
	
	
	
	
	
	@RequestMapping(value = "/getTenantId", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<Map<String,Object>> getTenantId() {
//		Long tenantId=constantUtil.getTenantId();
		Long tenantId = 11L;
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
            FetchUserResp userInfo = userApi.getUserByUserNameTenantIdTOB(username, tenantId);
            if(userInfo.getId()==null) {
            	throw new BusinessException(BusinessExceptionEnum.USER_IS_EXIT);
            }
            userInfo.setPassword(null);
            userInfo.setMqttPassword(null);
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
		Long tenantId=constantUtil.getTenantId();
		SaaSContextHolder.setCurrentTenantId(tenantId);
		RegisterReq user = createTempUser(username, new Md5Hash(password).toString());
		userApi.register(user);
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
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = { RequestMethod.POST })
	public CommonResponse<FetchUserResp> userInfo(HttpServletRequest request, String userId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		FetchUserResp userInfo = userApi.getUser(user.getUserId());
		if(userInfo !=null) {
			 userInfo.setPassword(null);
	         userInfo.setMqttPassword(null);
		}
		return CommonResponse.success(userInfo);
	}

	/**
	 * 微信登录获取用户信息
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "微信登录获取用户信息")
	@ResponseBody
	@RequestMapping(value = "/getWxUserInfo", method = RequestMethod.GET)
	public CommonResponse<Map<String,Object>> getWxUserInfo(@RequestParam("code") String code) {
		log.info("code:============"+code);
		String appId=environment.getProperty(ConstantUtil.WX_APPID);
		String appSecret=environment.getProperty(ConstantUtil.WX_APPSECRET);;
		JSONObject jsonObject = WxManagerService.getOpenIdForUrl(code,appId,appSecret);
		String openId = "";
		Map<String,Object> map=new HashMap<>();
		if (null != jsonObject && !jsonObject.containsKey("errcode")) {
			openId = jsonObject.getString("openid");
		}
		map.put("openId", openId);
		log.info("openId:============"+openId);
		return CommonResponse.success(map);
	}

	@ResponseBody
	@RequestMapping("uploadFile")
	public CommonResponse uploadFile(MultipartFile file) {
		String fileName = fileApi.upLoadFile(file, 11L);
		return CommonResponse.success(fileName);
	}

	public static void main(String[] args) {
		String aaa="123456";
		System.out.println(aaa);
		String a1=Base64Utils.encode(aaa.getBytes());
		System.out.println(a1);
		String a2=Base64Utils.decode(a1);
		System.out.println(a2);

		System.out.println(Math.cos(90));
	}
}
