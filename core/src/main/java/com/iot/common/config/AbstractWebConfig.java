package com.iot.common.config;

import com.iot.locale.GlobalLocaleFilter;
import com.iot.saas.SaaSContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


public abstract class AbstractWebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new GlobalLocaleFilter()).addPathPatterns("/**");
        registry.addInterceptor(saaSContextInterceptor()); //租戶/拦截

        addCustomInterceptors(registry);
        super.addInterceptors(registry);
    }

    protected abstract void addCustomInterceptors(InterceptorRegistry registry);

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        //请求参数必须加上 post get url路径上必须拼接 lang来修改国际化标准 zh en.... and so on
        lci.setParamName("lang");

        return lci;
    }

    @Bean
    public SaaSContextInterceptor saaSContextInterceptor() {
        return new SaaSContextInterceptor();
    }

}
