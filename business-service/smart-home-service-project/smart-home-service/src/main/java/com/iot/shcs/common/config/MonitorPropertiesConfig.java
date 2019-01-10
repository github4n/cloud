package com.iot.shcs.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notifier")
public class MonitorPropertiesConfig {

    private MonitorEmailPropertiesConfig email;

}