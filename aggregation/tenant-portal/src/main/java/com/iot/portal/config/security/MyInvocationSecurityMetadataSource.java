package com.iot.portal.config.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 项目名称：cloud
 * 功能描述：spring security拦截当前URl，查看是否需要进行权限校验
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
@Service
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
      * @despriction：获取当前url是否已绑定对应的角色
      * @author  yeshiyuan
      * @created 2018/7/14 16:34
      * @param object 当前访问url
      * @return
      */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //这里暂时不走数据库，直接写死，直接让其走MyAccessDecisionManager类，拿url和当前用户id去做校验
        Collection<ConfigAttribute> cas = new ArrayList<>();
        /*ConfigAttribute ca = new SecurityConfig("ROLE_TEST");
        cas.add(ca);*/
        return cas;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<ConfigAttribute>();
    }

    /**
     * 方法返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，或者直接返回true，如果不写的话将会报错Caused by: java.lang.IllegalArgumentException: SecurityMetadataSource does not support secure object class: class org.springframework.security.web.FilterInvocation
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
