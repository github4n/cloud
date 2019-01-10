package com.iot.design.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/14
 */
@Configuration
public class I18nCache {
    @Value("${spring.messages.basename}")
    private String i18nBasename;
    @Value("${spring.messages.lang}")
    private String lang;

    @Bean
    @Primary
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(i18nBasename);
        return messageSource;
    }

    public String getMessage(String key) {
        Locale locale = Locale.CHINA;
        if (I18N_LANG.CN.getLang().equals(lang)) {
            locale = Locale.CHINA;
        } else if (I18N_LANG.US.getLang().equals(lang)) {
            locale = Locale.ENGLISH;
        }

        return messageSource().getMessage(key, null, locale);
    }

    enum I18N_LANG {
        CN("CN"), US("US");

        I18N_LANG(String lang) {
            lang = lang;
        }

        private String lang;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }
}
