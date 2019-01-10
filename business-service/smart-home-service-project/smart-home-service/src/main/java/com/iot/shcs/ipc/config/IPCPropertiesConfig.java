package com.iot.shcs.ipc.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:12 2018/10/15
 * @Modify by:
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "ipc.config")
public class IPCPropertiesConfig {


    private Integer preRecordLen;

    private Integer evtIntervalLen;

    private Integer evtRecordLen;

    private Integer segmentLen;

}
