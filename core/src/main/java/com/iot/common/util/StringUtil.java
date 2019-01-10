package com.iot.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：字符串工具类
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 上午11:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 上午11:13:00
 */
public class StringUtil extends StringUtils {

    /**小写字母*/
    private static final String LOWER_STR = "abcdefghijklmnopqrstuvwxyz";

    /**大写字母*/
    private static final String UPPER_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**大小写字母数字带0*/
    private static final String FULL_WITH_ZORE_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**大小写字母数字不带0*/
    private static final String FULL_WITH_NO_ZORE_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

    /**数字*/
    private static final String NUMBER_STR = "0123456789";

    /**
     * 描述：转String，对象为null返回""
     *
     * @param obj 目标对象
     * @return String或""
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:26:29
     * @since
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 描述：将字符串的首字母大写
     *
     * @param str 目标对象
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:37:19
     * @since
     */
    public static String toUpperCase(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(String.valueOf(str.charAt(0)).toUpperCase());
        strbuf.append(str.substring(1, str.length()));
        return strbuf.toString();
    }

    /**
     * 描述：将字符串的首字母小写
     *
     * @param str 目标对象
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:37:19
     * @since
     */
    public static String toLowerCase(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(String.valueOf(str.charAt(0)).toLowerCase());
        strbuf.append(str.substring(1, str.length()));
        return strbuf.toString();
    }

    /**
     * 描述：获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
     * @author maochengyuan
     * @created 2018/11/21 19:56
     * @param length 长度
     * @param keyString 数据源
     * @return java.lang.String
     */
    private static String getRandomString(int length, String keyString) {
        if(length < 1){
            return EMPTY;
        }
        StringBuffer sb = new StringBuffer();
        int len = keyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(keyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }

    /**
     * 获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
     */
    public static String getRandomString(int length) {
        return getRandomString(length, FULL_WITH_ZORE_STR);
    }

    /**
     * 描述：获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
     * @author maochengyuan
     * @created 2018/11/21 20:00
     * @param length 长度
     * @param containsLowerCase 是否包含小写字母
     * @param containsUpperCase 是否包含大写字母
     * @param containsNumber 是否包含数字
     * @return java.lang.String
     */
    public static String getRandomString(int length, Boolean containsLowerCase, Boolean containsUpperCase, Boolean containsNumber) {
        //随机字符串的随机字符库
        StringBuffer keyString = new StringBuffer();
        if(containsLowerCase){
            keyString.append(LOWER_STR);
        }
        if(containsUpperCase){
            keyString.append(UPPER_STR);
        }
        if(containsNumber){
            keyString.append(NUMBER_STR);
        }
        return getRandomString(length, keyString.toString());
    }

    /**
     * 获取指定位数的随机字符串(包含小写字母、大写字母、数字排除0,0<length)
     */
    public static String getRandomStringWithNoZore(int length) {
        return getRandomString(length, FULL_WITH_NO_ZORE_STR);
    }
    
    /**
     * 获取指定位数的随机字符串(包含数字,0<length)
     */
    public static String getRandomNumber(int length) {
        return getRandomString(length, NUMBER_STR);
    }

    public static void main(String[] args) {
        System.out.println(toUpperCase("dsaf"));
        System.out.println(toLowerCase("ABCD"));
    }

}
