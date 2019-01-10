package com.iot.common.util;

import org.apache.commons.lang.math.NumberUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：数字工具类
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 上午11:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 上午11:13:00
 */
public class NumberUtil extends NumberUtils {

    /**
     * 描述：判断是否为整数
     *
     * @param value 对象
     * @return boolean
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午4:24:35
     * @since
     */
    public static boolean isInteger(Object value) {
        if (CommonUtil.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?\\d+{1}");
        return pattern.matcher(value.toString()).matches();
    }

    /**
     * 描述：判断是否为数字(包括小数)
     *
     * @param value 对象
     * @return boolean
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午4:24:35
     * @since
     */
    public static boolean isDigit(Object value) {
        if (CommonUtil.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?[0-9]*.?[0-9]*{1}");
        return pattern.matcher(value.toString()).matches();
    }

    /**
     * 描述：转Byte
     *
     * @param obj 目标值
     * @return Byte或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:12
     * @since
     */
    public static Byte toByte(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Byte.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：转Short
     *
     * @param obj 目标值
     * @return Short或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:12
     * @since
     */
    public static Short toShort(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Short.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：转Float
     *
     * @param obj 目标值
     * @return Float或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:18
     * @since
     */
    public static Float toFloat(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Float.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：转Integer
     *
     * @param obj 目标值
     * @return Integer或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:23
     * @since
     */
    public static Integer toInteger(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Integer.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：转Long
     *
     * @param obj 目标值
     * @return Long或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:23
     * @since
     */
    public static Long toLong(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Long.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：转Double
     *
     * @param obj 目标值
     * @return Double或null
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:21:29
     * @since
     */
    public static Double toDouble(Object obj) {
        try {
            if (CommonUtil.isEmpty(obj)) {
                return null;
            }
            return Double.valueOf(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：格式化数字（保留有效数字）
     *
     * @param number  需要格式化的数字
     * @param pattern 格式
     * @return 格式化后的字符串类型数字
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:47:52
     * @since
     */
    public static String format(Object number, String pattern) {
        try {
            if (CommonUtil.isEmpty(number)) {
                return "";
            }
            NumberFormat df = new DecimalFormat(pattern);
            return df.format(number);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：计算百分比
     * @param number 被除数
     * @param divisor 除数
     * @param pattern 格式（0.00）
     * @return 格式化后的字符串类型数字
     * @author mao2080@sina.com
     * @created 2018年7月30日 上午10:47:52
     * @since
     */
    public static String calcPercentage(Double number, Double divisor, String pattern) {
        if (number == null || divisor == null || divisor.intValue() == 0) {
            return "";
        }
        Number result = number*100/divisor;
        return format(result, pattern);
    }

    public static void main(String[] args) {
        System.out.println(isInteger("4"));
        System.out.println(isDigit("4.0"));
        System.out.println(toDouble("2"));
        System.out.println(format(2, "0.00"));
    }


}
