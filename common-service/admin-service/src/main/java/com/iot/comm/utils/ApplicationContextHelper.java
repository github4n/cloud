package com.iot.comm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHelper.applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    /**
     * 从spring容器中获取Bean实例。
     *
     * @param <T>   目标类。
     * @param clazz 目标类型。
     * @return Bean实例，如果不存在则返回null。
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从spring容器中获取Bean实例。
     *
     * @param <T>      目标类。
     * @param beanName Bean定义名称。
     * @param clazz    目标类型。
     * @return Bean实例，如果不存在则返回null。
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        try {
            return applicationContext.getBean(beanName, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从spring容器中获取Bean实例。
     *
     * @param beanName Bean定义名称。
     * @return Bean实例，如果不存在则返回null。
     */
    public static Object getBean(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }

}

