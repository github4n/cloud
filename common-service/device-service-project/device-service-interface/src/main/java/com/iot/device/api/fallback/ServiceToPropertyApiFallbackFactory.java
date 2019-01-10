package com.iot.device.api.fallback;

import com.iot.device.api.ServiceToEventApi;
import com.iot.device.api.ServiceToPropertyApi;
import com.iot.device.vo.req.ServiceToEventReq;
import com.iot.device.vo.req.ServiceToPropertyReq;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:03 2018/7/2
 * @Modify by:
 */
@Component
public class ServiceToPropertyApiFallbackFactory implements FallbackFactory<ServiceToPropertyApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToPropertyApiFallbackFactory.class);

    @Override
    public ServiceToPropertyApi create(Throwable cause) {
        return new ServiceToPropertyApi() {


            @Override
            public void save(ServiceToPropertyReq serviceToPropertyReq) {

            }

            @Override
            public void saveMore(ServiceToPropertyReq serviceToPropertyReq) {

            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public void update(Long id, Integer status) {

            }
        };
    }
}
