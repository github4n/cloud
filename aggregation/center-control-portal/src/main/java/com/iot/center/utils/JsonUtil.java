package com.iot.center.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具类
 * 功能描述：json工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月28日 上午11:56:15
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月28日 上午11:56:15
 */
public class JsonUtil {
	
	/**
	 * 
	 * 描述：将json转化成map
	 * @author mao2080@sina.com
	 * @created 2017年5月26日 下午1:58:56
	 * @since 
	 * @param jsonStr json字符串
	 * @return
	 * @throws BusinessException 
	 */
	public static Map<String, Object> convertJsonStrToMap(String jsonStr) {
		return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 
	 * 描述：将对象格式化成json字符串
	 * @author mao2080@sina.com
	 * @created 2017年4月1日 下午4:38:18
	 * @since 
	 * @param object 对象
	 * @return json字符串
	 * @throws BusinessException 
	 */
	public static String toJson(Object object) {
		return JSON.toJSONString(object,
				new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
						SerializerFeature.DisableCircularReferenceDetect,
						SerializerFeature.WriteNonStringKeyAsString });
	}

	/**
	 * 
	 * 描述：将对象格式化成json字符串（PrettyFormat格式）
	 * @author mao2080@sina.com
	 * @created 2017年4月1日 下午4:38:18
	 * @since 
	 * @param object 对象
	 * @return json字符串
	 * @throws BusinessException 
	 */
	public static String toJsonFormat(Object object) {
		return JSON.toJSONString(object,
				new SerializerFeature[] { SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat,
						SerializerFeature.DisableCircularReferenceDetect,
						SerializerFeature.WriteNonStringKeyAsString });

	}

	/**
	 * 
	 * 描述：转Map
	 * @author mao2080@sina.com
	 * @created 2017年4月1日 下午5:00:20
	 * @since 
	 * @param obj 对象
	 * @return object
	 * @throws BusinessException 
	 */
	public static Object toJsonObject(Object obj) {
		return JSON.toJSON(obj);

	}

	/**
	 * 
	 * 描述：将json串转为对象
	 * @author mao2080@sina.com
	 * @created 2017年4月1日 下午5:01:23
	 * @since 
	 * @param jsonString json串
	 * @param clazz 对象
	 * @return
	 * @throws BusinessException 
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		return (T) JSON.parseObject(jsonString, clazz);
	}

	public static <T> T fromJsonArray(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		return (T) JSONObject.parseArray(jsonString, clazz);
	}

	/**
	 * 
	 * 描述：暂时不开通
	 * @author mao2080@sina.com
	 * @created 2017年4月1日 下午5:08:12
	 * @since 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static <T> T fromJson(String jsonString) throws Exception {
		return JSON.parseObject(jsonString, new TypeReference<T>() {
		}, new Feature[0]);
	}
	
}
