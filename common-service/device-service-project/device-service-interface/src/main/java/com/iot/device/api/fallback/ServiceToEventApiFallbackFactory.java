package com.iot.device.api.fallback;

import com.iot.device.api.ServiceToActionApi;
import com.iot.device.api.ServiceToEventApi;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.req.ServiceToEventReq;
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
public class ServiceToEventApiFallbackFactory implements FallbackFactory<ServiceToEventApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToEventApiFallbackFactory.class);

    @Override
    public ServiceToEventApi create(Throwable cause) {
        return new ServiceToEventApi() {


            @Override
            public void save(ServiceToEventReq serviceToEventReq) {

            }

            @Override
            public void saveMore(ServiceToEventReq serviceToEventReq) {

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
