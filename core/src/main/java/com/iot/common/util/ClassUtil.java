package com.iot.common.util;

import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Method;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具类
 * 功能描述：class工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月21日 上午10:15:57
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月21日 上午10:15:57
 */
public class ClassUtil extends ClassUtils {

    /**
     * 描述：反射执行对象方法
     *
     * @param obj        目标对象
     * @param methodName 方法名称
     * @param argsType   参数类型
     * @param args       方法参数
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:16:57
     * @since
     */
    public static Object invokeMethodByClass(Object obj, String methodName, @SuppressWarnings("rawtypes") Class[] argsType, Object[] args) {
        if (obj == null) {
            return null;
        }
        try {
            Method mh = obj.getClass().getDeclaredMethod(methodName, argsType);
            return mh.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
