package com.iot.center.controller;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.annotation.SystemLogAnnotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.utils.MailUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.MD5SaltUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.ModifyPwdReq;
import com.iot.user.vo.UpdateUserReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户信息操作
 * 
 * @author linjihuang
 */
@Api(tags = "用户中心接口")
@Controller
@RequestMapping("/userCheck")
public class UserInfoController {
	private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

	private static final SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private UserApi userApiService;

	/**
	 * 查询用户信息
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "用户信息", notes = "查询用户信息")
	@RequestMapping(value = "/userInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<FetchUserResp> userInfo(HttpServletRequest request) {
		log.info("userCheck");
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		FetchUserResp userInfo = userApiService.getUser(user.getUserId());
		if(userInfo !=null) {
			userInfo.setPassword(null);
			userInfo.setMqttPassword(null);
		}
		return CommonResponse.success(userInfo);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @param user_nickname
	 * @param company
	 * @param phone
	 * @param address
	 * @return
	 */
	@PermissionAnnotation(value = "USER_MANAGER")
	@SystemLogAnnotation(value = "修改用户信息")
	@ApiOperation(value = "修改信息", notes = "修改用户信息")
	@RequestMapping(value = "/changeInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> changeInfo(HttpServletRequest request,
			@RequestParam("user_nickname") @ApiParam(value = "昵称") String user_nickname,
			@RequestParam("company") @ApiParam(value = "公司") String company,
			@RequestParam("phone") @ApiParam(value = "电话") String phone,
			@RequestParam("address") @ApiParam(value = "地址") String address) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		if (user == null) {
			throw new BusinessException(BusinessExceptionEnum.USER_IS_EXIT);
		}
		UpdateUserReq userReq = new UpdateUserReq();
		userReq.setId(user.getUserId());
		userReq.setNickname(user_nickname);
		userReq.setTel(phone);
		userReq.setCompany(company);
		userReq.setAddress(address);
		userReq.setUpdateTime(new Date());

		try {
			userApiService.updateUser(userReq);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 修改用户图标
	 * 
	 * @param request
	 * @param icon_name
	 * @return
	 */
	@PermissionAnnotation(value = "USER_MANAGER")
	@SystemLogAnnotation(value = "修改用户图标")
	@ApiOperation(value = "修改图标", notes = "修改用户图标")
	@RequestMapping(value = "/changeIcon", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> changeIcon(HttpServletRequest request,
			@RequestParam("icon_name") @ApiParam(value = "图标名") String icon_name) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		if (user == null) {
			throw new BusinessException(BusinessExceptionEnum.USER_IS_EXIT);
		}
		try {
			UpdateUserReq userReq = new UpdateUserReq();
			userReq.setId(user.getUserId());
			userReq.setHeadImg(icon_name);
			userReq.setUpdateTime(new Date());
			userApiService.updateUser(userReq);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@PermissionAnnotation(value = "USER_MANAGER")
	@SystemLogAnnotation(value = "修改用户密码")
	@ApiOperation(value = "修改密码", notes = "修改用户密码")
	@RequestMapping(value = "/changePwd", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> changePwd(HttpServletRequest request,
			@RequestParam("oldPwd") @ApiParam(value = "旧密码") String oldPwd,
			@RequestParam("newPwd") @ApiParam(value = "新密码") String newPwd) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			ModifyPwdReq pwdReq = new ModifyPwdReq();
			pwdReq.setNewPwd(newPwd);
			pwdReq.setOldPwd(oldPwd);
			pwdReq.setUserId(user.getUserId());
			userApiService.modifyPwd(pwdReq);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 发送验证邮件
	 * 
	 * @param request
	 * @param username
	 * @return
	 */
	@SystemLogAnnotation(value = "发送验证邮件")
	@ApiOperation(value = "发送邮件", notes = "发送验证邮件")
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> sendMail(HttpServletRequest request,
			@RequestParam("username") @ApiParam(value = "用户名") String username) {
		String key = "email-interval:" + username;
		String value = RedisCacheUtil.valueGet(key);
		if (StringUtils.isNotBlank(value)) {
			throw new BusinessException(BusinessExceptionEnum.SEND_IS_FAST);
		}
		int randNum = 100000 + (int) (Math.random() * ((999999 - 100000) + 1));
		String ranCode = String.valueOf(randNum);
		MailUtil mailUtil = new MailUtil();
		boolean check = mailUtil.doSendHtmlEmail("Email verification code",
				"Your email verification code is : " + ranCode, username);
		if (check) {
			HttpSession session = request.getSession();
			session.setAttribute(username, ranCode);
			session.setAttribute(username + "codeTime", System.currentTimeMillis());
			session.setAttribute(username + "status", "1");
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		}
		return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
	}

	/**
	 * 检查验证码
	 * 
	 * @param request
	 * @param username
	 * @param newPwd
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "判断验证", notes = "判断邮件验证码")
	@RequestMapping(value = "/checkCode", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> checkCode(HttpServletRequest request,
			@RequestParam("username") @ApiParam(value = "用户名") String username,
			@RequestParam("newPwd") @ApiParam(value = "新密码") String newPwd,
			@RequestParam("code") @ApiParam(value = "验证码") String code) {
		HttpSession session = request.getSession();
		String checkCode = (String) session.getAttribute(username);
		String status = (String) session.getAttribute(username + "status");
		if (null == status || "".equals(status)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
		}
		long codeTime = (long) session.getAttribute(username + "codeTime");
		long nowTime = System.currentTimeMillis();
		if (0 != codeTime && ((nowTime - codeTime) / 60000) > 9) {// 超过10分钟，验证码失效
			session.removeAttribute(username);
			session.removeAttribute(username + "codeTime");
			session.removeAttribute(username + "status");
			throw new BusinessException(BusinessExceptionEnum.CODE_IS_INVALID);
		}
		if (StringUtils.isNotBlank(checkCode) && code.equals(checkCode)) {
			try {
				newPwd = MD5SaltUtil.generate(newPwd);
				FetchUserResp userInfo = userApiService.getUserByUserNameTenantIdTOB(username, Long.valueOf(Constants.AIR_CONDITION_DEVICE_TENANT_ID));
				userApiService.updatePasswordByUserId(userInfo.getId(), newPwd);
				session.removeAttribute(username);// 清除使用完的验证码
				session.removeAttribute(username + "codeTime");
				session.removeAttribute(username + "status");
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} catch (BusinessException e) {
				e.printStackTrace();
				throw new BusinessException(BusinessExceptionEnum.CODE_IS_ERROR);
			}
		}
		throw new BusinessException(BusinessExceptionEnum.CODE_IS_ERROR);
	}

	/**
	 * 查询实时天气
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "查询天气", notes = "查询实时天气")
	@RequestMapping(value = "/checkWeather", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> checkWeather(HttpServletRequest request) {
		// 参数url化
		String result = "";
		try {
			String ip = java.net.URLEncoder.encode("US3290092", "utf-8");
			// 拼地址
			String apiUrl = "https://free-api.heweather.com/s6/weather/now?location=" + ip
					+ "&key=744556359df645d8bc082b071dd9423c&lang=en&unit=m";
			// 开始请求
			URL url = new URL(apiUrl);
			URLConnection open = url.openConnection();
			InputStream input = open.getInputStream();
			// 这里转换为String
			result = org.apache.commons.io.IOUtils.toString(input, "utf-8");
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
		// 输出
		return CommonResponse.success(result);
	}
}
