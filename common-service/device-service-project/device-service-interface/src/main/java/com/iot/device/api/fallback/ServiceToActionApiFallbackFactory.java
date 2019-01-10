package com.iot.device.api.fallback;

import com.iot.device.api.ModuleEventToPropertyApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.req.ServiceToActionReq;
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
public class ServiceToActionApiFallbackFactory implements FallbackFactory<ServiceToActionApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToActionApiFallbackFactory.class);

    @Override
    public ServiceToActionApi create(Throwable cause) {
        return new ServiceToActionApi() {


            @Override
            public void save(ServiceToActionReq serviceToActionReq) {

            }

            @Override
            public void saveMore(ServiceToActionReq serviceToActionReq) {

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
