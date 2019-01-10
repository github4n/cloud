package com.iot.control;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.redis.RedisCacheUtil;
import com.iot.service.WxManagerService;

import io.swagger.annotations.ApiOperation;

@RestController
public class MeetingControl {

	private static final Logger log = LoggerFactory.getLogger(MeetingControl.class);
	
	/**
	 * 微信登录获取用户信息
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "微信登录获取用户信息")
	@RequestMapping(value = "/getWxUserInfo", method = RequestMethod.GET)
	public CommonResponse<Map<String,Object>> getWxUserInfo(@RequestParam("code") String code) {
		log.info("code:============"+code);
		JSONObject jsonObject = WxManagerService.getOpenIdForUrl(code);
		String openId = "";
		Map<String,Object> map=new HashMap<>();
		if (null != jsonObject && !jsonObject.containsKey("errcode")) {
			openId = jsonObject.getString("openid");
		}
		map.put("openId", openId);
		return CommonResponse.success(map);
	}
//	String accesstoken = WxManagerService.getToken("wxd2a8841d92ccdc5d", "640c971d3b68a4cc02ff115400ac8a72").getString("access_token");
//  JSONObject object = WxManagerService.httpsRequest(WxManagerService.getUserinfoStrUrl(accesstoken, openId), "GET", null);
 
	
	/**
	 * 获取mqtt clientId
	 * @return
	 */
	@RequestMapping("/getClientId")
	@ResponseBody
	public CommonResponse<String> findBusinessTypeList(String uuid,String mqttPwd) {
		String hashKey=null;
		if(StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(mqttPwd)) {
			String key=generatePwdKey(uuid);
			hashKey=String.valueOf(new Date().getTime());
			RedisCacheUtil.hashPut(key, hashKey, DigestUtils.sha256Hex(mqttPwd),false);
		}
		return CommonResponse.success(hashKey);
	}
	
	/**
	 * 获取mqtt clientId
	 * @return
	 */
	@RequestMapping(value="/addPermission", method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse<String> addPermission(String uuid) {
		if(StringUtils.isNotBlank(uuid)) {
			String pubKey=generatePubKey(uuid);
			String subKey=generateSubKey(uuid);
			RedisCacheUtil.setPush(pubKey, "iot/v1/s/"+uuid+"/#", false);
			RedisCacheUtil.setPush(pubKey, "iot/v1/cb/"+uuid+"/#", false);
			RedisCacheUtil.setPush(subKey, "iot/v1/c/"+uuid+"/#", false);
			RedisCacheUtil.setPush(subKey, "iot/v1/cb/"+uuid+"/#", false);
		}
		return CommonResponse.success();
	}
	
	private String generatePwdKey(String uuid){
        return new StringBuilder().append(uuid).append(":").append("pwd").toString();
    }
	
	private String generatePubKey(String uuid){
		return new StringBuilder().append(uuid).append(":").append("pub").toString();
	}
	
	private String generateSubKey(String uuid){
		return new StringBuilder().append(uuid).append(":").append("sub").toString();
	}
}
