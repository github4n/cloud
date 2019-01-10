package com.iot.comm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notifier")
public class MonitorPropertiesConfig {

    private MonitorMobilePropertiesConfig mobile;

    private MonitorEmailPropertiesConfig email;

    public MonitorMobilePropertiesConfig getMobile() {
        return mobile;
    }

    public void setMobile(MonitorMobilePropertiesConfig mobile) {
        this.mobile = mobile;
    }

    public MonitorEmailPropertiesConfig getEmail() {
        return email;
    }

    public void setEmail(MonitorEmailPropertiesConfig email) {
        this.email = email;
    }
}