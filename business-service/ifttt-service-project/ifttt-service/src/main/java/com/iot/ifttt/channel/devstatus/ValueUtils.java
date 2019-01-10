package com.iot.ifttt.channel.devstatus;

import java.math.BigDecimal;

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
}
