package com.iot.building.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ValueUtils {

	public static String getStringValue(Object source) {
		String result = null;
		if (source instanceof String) {
			result = (String) source;
		} else if (source instanceof Double) {
			result = ((Double) source).toString();
		} else if (source instanceof Integer) {
			result = ((Integer) source).toString();
		} else if (source instanceof Long) {
			result = ((Long) source).toString();
		} else if (source instanceof Float) {
			result = ((Float) source).toString();
		} else if (source instanceof BigDecimal) {
			result = ((BigDecimal) source).toString();
		}
		return result;
	}

	public static Double getDoubleValue(Object source) {
		Double result = null;
		if (source instanceof Double) {
			result = (Double) source;
		} else if (source instanceof String) {
			result = Double.valueOf((String) source);
		} else if (source instanceof Integer) {
			result = ((Integer) source).doubleValue();
		} else if (source instanceof Long) {
			result = ((Long) source).doubleValue();
		} else if (source instanceof Float) {
			result = ((Float) source).doubleValue();
		} else if (source instanceof BigDecimal) {
			result = ((BigDecimal) source).doubleValue();
		}
		return result;
	}

	public static Integer getIntegerValue(Object source) {
		Integer result = null;
		if (source instanceof Integer) {
			result = (Integer) source;
		} else if (source instanceof String) {
			result = Integer.valueOf((String) source);
		} else if (source instanceof Double) {
			result = ((Double) source).intValue();
		} else if (source instanceof Long) {
			result = ((Long) source).intValue();
		} else if (source instanceof Float) {
			result = ((Float) source).intValue();
		} else if (source instanceof BigDecimal) {
			result = ((BigDecimal) source).intValue();
		}
		return result;
	}

	public static Long getLongValue(Object source) {
		Long result = null;
		if (source instanceof Long) {
			result = (Long) source;
		} else if (source instanceof Integer) {
			result = ((Integer) source).longValue();
		} else if (source instanceof String) {
			result = Long.valueOf((String) source);
		} else if (source instanceof Double) {
			result = ((Double) source).longValue();
		} else if (source instanceof Float) {
			result = ((Float) source).longValue();
		} else if (source instanceof BigDecimal) {
			result = ((BigDecimal) source).longValue();
		}
		return result;
	}

	public static Float getFloatValue(Object source) {
		Float result = null;
		if (source instanceof Float) {
			result = (Float) source;
		} else if (source instanceof Integer) {
			result = ((Integer) source).floatValue();
		} else if (source instanceof String) {
			result = Float.valueOf((String) source);
		} else if (source instanceof Double) {
			result = ((Double) source).floatValue();
		} else if (source instanceof Long) {
			result = ((Long) source).floatValue();
		} else if (source instanceof BigDecimal) {
			result = ((BigDecimal) source).floatValue();
		}
		return result;
	}

	public static String join(Object[] o, String flag) {
		StringBuffer str_buff = new StringBuffer();
		for (int i = 0, len = o.length; i < len; i++) {
			str_buff.append(String.valueOf(o[i]));
			if (i < len - 1) {
				str_buff.append(flag);
			}
		}
		return str_buff.toString();
	}

	public static Long[] exPosition(String test){
		//[460,2,1542251540000]
		String s = test.substring(0,test.length());
		String sb = s.replace("\"","");
		//截取字符串s
		String[] strArr = sb.split(",");
		//转换long类型的数组
		Long[] strArrNum = (Long[]) ConvertUtils.convert(strArr,Long.class);
		return strArrNum;
	}

	public static Long[] exPositionRelation(String test){
		//String test = "[460,2,1542251540000]";
		String s = test.substring(0,test.length());
		//截取字符串s
		String[] strArr = s.split(",");
		//转换long类型的数组
		Long[] strArrNum = (Long[]) ConvertUtils.convert(strArr,Long.class);
		return strArrNum;
	}
	/**
	 * 字符串转成long数组
	 * @param s
	 * @return
	 */
	public static Long[] exJoin(String s){
		//截取字符串s
		String[] strArr = s.split(",");
		//转换long类型的数组
		Long[] strArrNum = (Long[]) ConvertUtils.convert(strArr,Long.class);
		return strArrNum;
	}

	/**
	 * json字符串转map
	 * @param s
	 * @return
	 */
	public static Map jsonStringToMap(String s){
		JSONObject jsonObject = JSON.parseObject(s);
		Map map=new HashMap();
		map = jsonObject;
		return map;
	}
}
