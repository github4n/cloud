package com.iot.cloud.config.shiro;

import com.iot.cloud.util.ConstantUtil;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfiguration {
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") org.apache.shiro.mgt.SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/loginError");
        bean.setUnauthorizedUrl("/unauthorizedUrl.jsp");
        bean.setLoginUrl(ConstantUtil.commonPath + "/login");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/swagger**", "anon"); //表示可以匿名访问
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/getTenantId", "anon");
        filterChainDefinitionMap.put("/CameraController/newCount", "anon");
        filterChainDefinitionMap.put("/CameraController/countEveryHour", "anon");
        filterChainDefinitionMap.put("/CameraController/count", "anon");
        filterChainDefinitionMap.put("/CameraController/findCameraList", "anon");
        filterChainDefinitionMap.put("/template/findLocationSceneRelationList", "anon");
        filterChainDefinitionMap.put("/optical/fileUpload", "anon");
        filterChainDefinitionMap.put("/optical/calculatorHaveTable", "anon");
        filterChainDefinitionMap.put("/businessType/fileUpload", "anon");
        filterChainDefinitionMap.put("/template/product/list", "anon");
        filterChainDefinitionMap.put("/space/downloadFile", "anon");
        filterChainDefinitionMap.put("/sceneController/deleteLocationScene", "anon");
        filterChainDefinitionMap.put("/sceneController/findAllLocationScene", "anon");
        filterChainDefinitionMap.put("/sceneController/saveLocationSceneDetail", "anon");
        filterChainDefinitionMap.put("/sceneController/findLocationSceneDetailList", "anon");
        filterChainDefinitionMap.put("/sceneController/saveLocationScene", "anon");
        filterChainDefinitionMap.put("/sceneController/findLocationSceneList", "anon");
        filterChainDefinitionMap.put("/device/appAddDeviceByExcel", "anon");
        filterChainDefinitionMap.put("/device/appAddDevice", "anon");
        filterChainDefinitionMap.put("/device/propertyInfo", "anon");
        filterChainDefinitionMap.put("/businessType/**", "anon");
        filterChainDefinitionMap.put("/device/queryAirCondition", "anon");
        filterChainDefinitionMap.put("/space/control", "anon");
        filterChainDefinitionMap.put("/getLocation", "anon");
        filterChainDefinitionMap.put("/checkAdmin", "anon");
        filterChainDefinitionMap.put("/client/getClientId", "anon");
        filterChainDefinitionMap.put("/getWxUserInfo", "anon");
        filterChainDefinitionMap.put("/space/**", "anon");
        filterChainDefinitionMap.put("/sceneController/getSceneDetailList**", "anon");
        filterChainDefinitionMap.put("/sceneController/sceneExecute**", "anon");
        filterChainDefinitionMap.put("/socket**", "anon");
        filterChainDefinitionMap.put("/realTime/control**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/socket", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/loginout", "anon");
        filterChainDefinitionMap.put("/register/**", "anon");
        filterChainDefinitionMap.put("/loginError", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/html/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/userCheck/sendMail", "anon");
        filterChainDefinitionMap.put("/userCheck/checkCode", "anon");
        filterChainDefinitionMap.put("/hystrix.stream", "anon");
        filterChainDefinitionMap.put("/device/detail", "anon");
        filterChainDefinitionMap.put("/device/synchronization", "anon");
        filterChainDefinitionMap.put("/realTime/control", "anon");
        filterChainDefinitionMap.put("/device/propertyInfo", "anon");
        filterChainDefinitionMap.put("/device/addDevice", "anon");
        filterChainDefinitionMap.put("/device/propertyInfo", "anon");
        filterChainDefinitionMap.put("/checkAdmin", "anon");
        filterChainDefinitionMap.put("/env", "anon");
        filterChainDefinitionMap.put("/space/saveSpaceDevice", "anon");
        filterChainDefinitionMap.put("/space/findTree", "anon");
        filterChainDefinitionMap.put("/space/getDeploymentList", "anon");
        filterChainDefinitionMap.put("/device/findBusinessTypeList", "anon");
//		filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager(@Qualifier("authRealm") AuthorizingRealm authRealm) {
        System.err.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    //配置自定义的权限登录器
    @Bean(name = "authRealm")
    public AuthorizingRealm authRealm() {
        AuthorizingRealm authRealm = new CustomRealm();
        return authRealm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") org.apache.shiro.mgt.SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}
