package com.iot.portal.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名称：cloud
 * 功能描述：不被拦截的url
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
@Configuration
@ConfigurationProperties("security.permit")
public class PermitUrlConfig {

    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

}
