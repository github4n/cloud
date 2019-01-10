package com.iot.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 模块名称：获取其他接口内容
 * 创建人： wanglei
 * 创建时间：2017年5月10日 13:40
 */
public class ToolUtils {

    //获取UUID
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 获取主题中的uuid
    public static String getClientId(String topic) {
        return topic.split("/")[3];
    }

    public static Long dateFormatLinuxTime(Date date) {
        return (Long) new Date().getTime();
    }

    /*
     * 将时间转换为时间戳
     */
    public static void main(String[] args) throws ParseException {
//        String time = "2018-04-09 18:26:49";
//        System.out.println(timeStringToLong(time));
        try {

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long s) {
        Long timestamp = Long.parseLong(s.toString()) * 1000;
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date timestampToDate(String seconds) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date(Long.parseLong(seconds + "000")));
        return sdf.parse(strDate);
    }

    /**
     * 有毫秒
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static Date timestampToDate(Long s) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long(s);
        String str = format.format(time);
        Date date = format.parse(str);
        return date;
    }

    public static Long timeStringToLong(String date) throws ParseException {
        if (!StringUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(date).getTime();
        }
        return null;
    }

    public static Date stringToDate(String str, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(str);
    }

    public static String dateToSting(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * @param ipAddress
     * @return
     */
    public static boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 判断 两个数是否相等
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean isEqual(Long num1, Long num2) {
        boolean flag = false;

        if (num1 == null || num2 == null) {
            return flag;
        }

        if (num1.compareTo(num2) == 0) {
            flag = true;
        }

        return flag;
    }

    /**
     * 描述：11位手机号脱敏
     * @author nongchongwei
     * @date 2018/10/8 14:28
     * @param
     * @return
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    /**
     * 描述：前三后四脱敏
     * @author nongchongwei
     * @date 2018/10/8 14:28
     * @param
     * @return
     */
    public static String idEncrypt(String id){
        if(TextUtils.isEmpty(id) || (id.length() < 8)){
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }
}