package com.iot.center.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Maps;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Constants {

	public final static String AIR_CONDITION_DEVICE_IP = "120.24.48.245:11093";
	public final static String AIR_CONDITION_DEVICE_TENANT_ID = "11";
	public final static String AIR_CONDITION_DEVICE_TYPE_ID = "-3000";

	public final static String SCHEDULE_SCENE = "scene";
	public final static String SCHEDULE_IFTTT = "ifttt";
	public final static String SCHEDULE_IFTTT_TEMPLATE = "ifttt_template";
	public final static String SCHEDULE_SCENE_TEMPLATE = "scene_template";
	public final static String SCHEDULE_LOCATION = "location";
	public final static String QUERY_WEEK = "week";
	public final static String QUERY_MONTH = "month";

	public final static String IFTTT_2B_FLAG = "_2B";

	public final static String SCENE_TEMPLATE = "scene_template";

	public final static String MEETING_HOST="center-control.meeting-url";
	public final static String CONFIRM_MEETING_URL="center-control.confirm-url";
	public final static String CONTROL_URL="center-control.control-url";
	

	public final static String IFTTT_TYPE_TEMPLATE = "template";

	public final static String SYSTEM_TYPE = "2B";
	public final static String headImg = "headImg.default";
	
	public final static String ONLINE = "connected";
	
	//用户权限
	public static Map<Long,Object> userPermission=Maps.newHashMap();
	public static Map<Long,Object> dataPermission=Maps.newHashMap();
	
	// 外接设备类型
	public static Map<String, String> getExternalDeviceType() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-3000", "AirConditioning");
		typeMap.put("-4000", "AirSwitch");
		typeMap.put("-4001", "AirSwitchNode");
		return typeMap;
	}
	
	public static TimeZone getTimeZone(String zoneId) {
		return TimeZone.getTimeZone(zoneId);
		//Etc/GMT, Etc/GMT+0, Etc/GMT+1, Etc/GMT+10, Etc/GMT+11, Etc/GMT+12, Etc/GMT+2, Etc/GMT+3, Etc/GMT+4, Etc/GMT+5, Etc/GMT+6, Etc/GMT+7, Etc/GMT+8, Etc/GMT+9, Etc/GMT-0, Etc/GMT-1, Etc/GMT-10, Etc/GMT-11, Etc/GMT-12, Etc/GMT-13, Etc/GMT-14, Etc/GMT-2, Etc/GMT-3, Etc/GMT-4, Etc/GMT-5, Etc/GMT-6, Etc/GMT-7, Etc/GMT-8, Etc/GMT-9, Etc/GMT0, Etc/Greenwich, Etc/UCT, Etc/UTC, Etc/Universal, Etc/Zulu
	}
	
	/**
	* 根据具体年份周数获取日期范围
	* @param year
	* @param week
	* @param targetNum
	* @return
	*/
	public static String getWeekDays(int year,int month,Integer day) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		//设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        if(day==null) {
        	day = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        }
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, day);
		String beginDate = sdf.format(cal.getTime());
		return beginDate;
	}
	
	//base64编码
	public static String base64Encoder(String str) throws UnsupportedEncodingException {
		BASE64Encoder encoder=new BASE64Encoder();
		return encoder.encode(str.getBytes("UTF-8"));
	}
	
	//base64解码码
	public static String base64Decoder(String str) throws IOException {
		 BASE64Decoder decoder = new BASE64Decoder();
		return new String(decoder.decodeBuffer(str), "UTF-8");
	}
	
	//判断是否base64编码 是 true 否 false
	public static Boolean judegeIsBase64Encoder(String str) throws IOException {
		 String decoderStr=base64Decoder(str);
		 String ecoderStr= base64Encoder(decoderStr);
		 return str.equals(ecoderStr);
	}
}
