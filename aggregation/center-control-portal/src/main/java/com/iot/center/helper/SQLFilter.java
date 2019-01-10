package com.iot.center.helper;

import org.apache.commons.lang.StringUtils;

import com.iot.common.exception.BusinessException;

/**
 * sql过滤
 * 
 * @author xlf
 * @email xlfbe696@gmail.com
 * @date 2017年4月19日 上午10:41:25
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * 
     * @param str
     *            待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        // 转换成小写
        str = str.toLowerCase();

        // 非法字符
        String[] keywords = { "master", "truncate", "insert", "select", "delete", "update", "declare", "alert",
                "create", "drop" };
        // 判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.equals(keyword)) {
                throw new BusinessException(BusinessExceptionEnum.ILLEGAL_CHARACTER);
            }
        }

        return str;
    }
}