package com.iot.common.util;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：core
 * 功能描述：字典表工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2018/7/4 17:04
 * 修改人： mao2080@sina.com
 * 修改时间：2018/7/4 17:04
 * 修改描述：
 */
public class DictUtil {

    /**
     * 描述：依据字典表获取itemId获取名称或描述
     * @author maochengyuan
     * @created 2018/7/5 11:28
     * @param dictMap 字典表
     * @param itemId itemId
     * @return java.lang.String
     */
    public static String getItemNameByItemId(Map<String, String> dictMap, Object itemId){
        if(dictMap == null || dictMap.isEmpty() || itemId == null){
            return "";
        }
        String val = dictMap.get(itemId.toString());
        return val == null?"":val;
    }

}
