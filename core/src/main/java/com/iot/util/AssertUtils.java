package com.iot.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:22 2018/4/24
 * @Modify by:
 */
public class AssertUtils {

    public static void notEmpty(Object object, String message) {
        if (StringUtils.isEmpty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }
}
