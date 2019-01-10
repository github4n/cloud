package com.iot.common.config;

import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BaseExceptionHandler;
import com.iot.common.exception.CustomErrorDecoder;
import com.netflix.discovery.DiscoveryManager;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

/**
 * <p>描述：微服务基本配置</p>
 *
 * @author xiangyitao
 * @date 2018/03/06
 */
@SpringBootApplication
@EnableEurekaClient
@Import({BaseExceptionHandler.class, FeignMappingDefaultConfig.class})
public abstract class AbstractApplication {

    /**
     * 错误转码业务异常
     *
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * 时区统一处理
     */
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @PreDestroy
    public void eurekaOffline() {
        DiscoveryManager.getInstance().shutdownComponent();
    }

    @Bean
    public Converter<String, Date> dateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String dateStr) {
                long lt = Long.parseLong(dateStr);
                return new Date(lt);
            }
        };
    }

    @PostConstruct
    public void createDefaultFile(){
        try {
            Properties props=System.getProperties();
            String osName = props.getProperty("os.name");
            if(!osName.startsWith("Windows")) {
                File file = new File(SystemConstants.DEFAULT_UPLOAD_PATH);
                if(!file.exists()){
                    file.mkdirs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
