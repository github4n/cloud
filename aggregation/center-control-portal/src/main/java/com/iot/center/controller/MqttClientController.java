package com.iot.center.controller;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.common.beans.CommonResponse;
import com.iot.redis.RedisCacheUtil;

import io.swagger.annotations.Api;

@Api(tags = "mqttClientId接口")
@Controller
@RequestMapping("/client")
public class MqttClientController {
	
	/**
	 * 获取mqtt clientId
	 * @return
	 */
	@RequestMapping("/getClientId")
	@ResponseBody
	public CommonResponse<String> getRandomClientPre(String uuid,String mqttPwd) {
		String hashKey=null;
		if(StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(mqttPwd)) {
			String key=generatePwdKey(uuid);
			hashKey=String.valueOf(new Date().getTime());
			RedisCacheUtil.hashPut(key, hashKey, DigestUtils.sha256Hex(mqttPwd),false);
			RedisCacheUtil.expireKey(key, 60*60*8);//失效时间8小时
		}
		return CommonResponse.success(hashKey);
	}
	
	private String generatePwdKey(String uuid){
        return new StringBuilder().append(uuid).append(":").append("pwd").toString();
    }

}
