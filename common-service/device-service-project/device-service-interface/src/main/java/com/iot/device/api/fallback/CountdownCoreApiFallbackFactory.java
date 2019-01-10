package com.iot.device.api.fallback;

import com.iot.device.api.CountdownCoreApi;
import com.iot.device.vo.req.CountDownReq;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CountdownCoreApiFallbackFactory implements FallbackFactory<CountdownCoreApi> {
    @Override
    public CountdownCoreApi create(Throwable cause) {
        return new CountdownCoreApi() {

            public void addCountDown(CountDownReq countDownReq) {

            }
        };
    }
}
