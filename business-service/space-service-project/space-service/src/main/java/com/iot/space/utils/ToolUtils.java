package com.iot.space.utils;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 模块名称：获取其他接口内容
 * 创建人： wanglei
 * 创建时间：2017年5月10日 13:40
 */
public class ToolUtils { 
	
	//获取UUID
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static Long dateFormatLinuxTime(Date date){
	    return new Date().getTime();
	}
	
	/* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long s){
    	Long timestamp = Long.parseLong(s.toString())*1000;  
	    String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));  
        return date;
    }

    /**
	 * string#RGB转int
	 * 
	 * @param argb
	 * @throws NumberFormatException
	 */
//	public static int convertToColorInt(String argb)
//			throws IllegalArgumentException {
//		if (!argb.startsWith("#")) {
//			argb = "#" + argb;
//		}
//		argb =argb+"00";
//		return Color.parseColor(argb);
//	}
	
	/**
	 * int转#RGB
	 * 
	 * @param color
	 * @return String #RGB
	 */
	public static String convertToRGB(int color) {
		String strColor=Integer.toHexString(color);
		if(strColor.length()<6){
			int len=strColor.length();
			for(int i=0;i<6-len;i++){
				strColor="0"+strColor;
			}
		}else if(strColor.length()>6){
			strColor=strColor.substring(2,strColor.length());
		}
		return "#"+ strColor.toUpperCase();
	}
	
	/**
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static boolean isIpv4(String ipAddress) {  
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
  
        Pattern pattern = Pattern.compile(ip);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
    }  
}