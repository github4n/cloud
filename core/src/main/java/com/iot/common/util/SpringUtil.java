package com.iot.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 描述：获取applicationContext
     *
     * @return
     * @author qizhiyong
     * @created 2018年1月22日 上午10:10:33
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 描述：通过name获取 Bean.
     *
     * @param name
     * @return
     * @author qizhiyong
     * @created 2018年1月22日 上午10:10:41
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 描述：通过class获取Bean.
     *
     * @param clazz
     * @return
     * @author qizhiyong
     * @created 2018年1月22日 上午10:10:58
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 描述：通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @return
     * @author qizhiyong
     * @created 2018年1月22日 上午10:11:08
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
