package com.iot.shcs.ipc.util;

import com.iot.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：MQTT中间服务
 * 功能描述：MQTT中间服务
 * 创建人： 490485964@qq.com
 * 创建时间：2018年03月28日 10:52
 * 修改人： 490485964@qq.com
 * 修改时间：2018年03月28日 10:52
 */
public class CommonUtil {
	
	/**返回代码*/
	private static int  CODE = 200;
	
	/**返回信息*/
	private static String DESC = "Success.";

	/**
	 * 描述：初始化响应map
	 * @author 490485964@qq.com
	 * @date 2018/3/28 9:39
	 * @param
	 * @return
	 */
	public static Map<String, Object> initRespMap(Map<String,Object> bodyMap) throws BusinessException {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("code", CODE);
		responseMap.put("desc", DESC);
		responseMap.put("sessionId",bodyMap.get("sessionId")==null?"":bodyMap.get("sessionId").toString());
		return responseMap;
	}
	
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

	public static String getDeviceOrUserIdIdByTopic(String topic) throws BusinessException{
		//从主题获取deviceid
		if(null == topic || "".equals(topic)){
			return "";
		}
		String[] topicArray=topic.split("/");
		if (null==topicArray || topicArray.length<2){
			return "";
		}
		String deviceId=topicArray[topicArray.length-2];
		return deviceId;
	}
	
	/**
	 * 
	 * 描述：从主题里面获取用户信息
	 * @author zhouzongwei
	 * @created 2017年8月30日 上午11:15:24
	 * @since 
	 * @param bodyMap
	 * @return
	 */
	public static  String getUserId(Map<String, Object> bodyMap) throws BusinessException{
		//从主题获取userid
		String topic=bodyMap.get("topic").toString();
		String[] topicArray=topic.split("/");
		if (null==topicArray || topicArray.length<2){
			return "";
		}
		String userId=topicArray[topicArray.length-2];
		return userId;
	}

}
