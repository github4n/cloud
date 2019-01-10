package com.iot.device.api.fallback;

import com.iot.device.api.ConfigurationApi;
import com.iot.device.vo.rsp.ConfigurationRsp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigurationApiFallbackFactory implements FallbackFactory<ConfigurationApi>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationApiFallbackFactory.class);

    @Override
    public ConfigurationApi create(Throwable arg0) {

        return new ConfigurationApi() {

            @Override
            public List<ConfigurationRsp> selectConfigByTenantId(Long tenantId) {

                return null;
            }

        };
    }

}
