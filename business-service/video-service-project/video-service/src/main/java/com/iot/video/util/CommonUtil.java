package com.iot.video.util;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.SecurityUtil;
import com.iot.video.contants.ModuleConstants;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：MQTT中间服务
 * 功能描述：MQTT中间服务
 * 创建人： 490485964@qq.com
 * 创建时间：2018年03月28日 10:52
 * 修改人： 490485964@qq.com
 * 修改时间：2018年03月28日 10:52
 */
public class CommonUtil {

	/**
	 * 描述：从主题里面获取设备信息ID
	 * @author 490485964@qq.com
	 * @date 2018/3/28 9:41
	 * @param
	 * @return
	 */
	public static String getDeviceId(Map<String, Object> bodyMap) throws BusinessException{
		//从主题获取deviceid
		String topic=bodyMap.get("topic").toString();
		String[] topicArray=topic.split("/");
		if (null==topicArray || topicArray.length<2){
			return "";
		}
		String deviceId=topicArray[topicArray.length-2];
		return deviceId;
	}

	public static String encryptId(long id)
	{
		String str = new StringBuilder().append(id).toString();
		return SecurityUtil.EncryptByAES(str, ModuleConstants.AES_KEY);
	}

	public static long decryptId(String id)
	{
		return Long.parseLong(SecurityUtil.DecryptAES(id, ModuleConstants.AES_KEY));
	}

	public static void main(String agrs[]){
		System.out.println("ssss:"+encryptId(2));
	}

}
