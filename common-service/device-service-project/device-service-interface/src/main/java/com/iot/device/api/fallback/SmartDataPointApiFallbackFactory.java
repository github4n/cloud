package com.iot.device.api.fallback;

import com.iot.device.api.SmartDataPointApi;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmartDataPointApiFallbackFactory implements FallbackFactory<SmartDataPointApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartDataPointApiFallbackFactory.class);

    @Override
    public SmartDataPointApi create(Throwable arg0) {

        return new SmartDataPointApi() {
            @Override
            public SmartDataPointResp getSmartDataPoint(Long tenantId, Long propertyId, Integer smart) {
                return null;
            }
        };
    }

}
