package com.iot.common.util;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模块名称：获取其他接口内容
 * 创建人： wanglei
 * 创建时间：2017年5月10日 13:40
 */
public class ToolUtil {

    //获取UUID
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 描述：生成UUID
     *
     * @param count
     * @return
     * @throws BusinessException
     * @author 李帅
     * @created 2018年4月11日 下午2:09:56
     * @since
     */
    public static List<String> getUUIDS(int count) throws BusinessException {
        if (count < 1 || count > 100) {
            throw new BusinessException(ExceptionEnum.UUID_COUNT_EXCEPTION);
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            list.add(getUUID());
        }
        return list;
    }

    public static Long dateFormatLinuxTime(Date date) {
        return (Long) new Date().getTime();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long s) {
        Long timestamp = Long.parseLong(s.toString()) * 1000;
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    /**
     * int转#RGB
     *
     * @param color
     * @return String #RGB
     */
    public static String convertToRGB(int color) {
        String strColor = Integer.toHexString(color);
        if (strColor.length() < 6) {
            int len = strColor.length();
            for (int i = 0; i < 6 - len; i++) {
                strColor = "0" + strColor;
            }
        } else if (strColor.length() > 6) {
            strColor = strColor.substring(2, strColor.length());
        }
        return "#" + strColor.toUpperCase();
    }

    /**
     * @param ipAddress
     * @return
     */
    public static boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static String getRandomStr(){
        Random rand = new Random();
        char[] letters=new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q',
                'R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i',
                'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','r',
                '0','1','2','3','4','5','6','7','8','9'};
        String str = "";
        int index;
        boolean[] flags = new boolean[letters.length];//默认为false
        for(int i=0;i<5;i++){
            do{
                index = rand.nextInt(letters.length);
            }while(flags[index]==true);
            char c = letters[index];
            str += c;
            flags[index]=true;
        }
        return str;
    }
}