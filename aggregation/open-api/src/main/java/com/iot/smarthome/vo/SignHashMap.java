package com.iot.smarthome.vo;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 14:26
 * @Modify by:
 */
public class SignHashMap extends HashMap<String, String> {
    private static final long serialVersionUID = -1277791390393392630L;

    public SignHashMap() {
    }

    public SignHashMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public String put(String key, Object value) {
        String strValue;
        if(value == null) {
            strValue = null;
        } else if(value instanceof String) {
            strValue = (String)value;
        } else if(value instanceof Integer) {
            strValue = ((Integer)value).toString();
        } else if(value instanceof Long) {
            strValue = ((Long)value).toString();
        } else if(value instanceof Float) {
            strValue = ((Float)value).toString();
        } else if(value instanceof Double) {
            strValue = ((Double)value).toString();
        } else if(value instanceof Boolean) {
            strValue = ((Boolean)value).toString();
        } else if(value instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strValue = format.format((Date)value);
        } else {
            strValue = value.toString();
        }

        return this.put(key, strValue);
    }

    public String put(String key, String value) {
        return this.areNotEmpty(new String[]{key, value})?(String)super.put(key, value):null;
    }

    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if(values != null && values.length != 0) {
            String[] var2 = values;
            int var3 = values.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String value = var2[var4];
                result &= !StringUtils.isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }
}
