package com.iot.device.api.fallback;

import com.iot.device.api.ServiceModuleStyleApi;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
import com.iot.device.vo.rsp.StyleTemplateResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceModuleStyleApiFallbackFactory implements FallbackFactory<ServiceModuleStyleApi> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleStyleApiFallbackFactory.class);


    @Override
    public ServiceModuleStyleApi create(Throwable throwable) {
        return new ServiceModuleStyleApi() {


            @Override
            public Long saveOrUpdate(ServiceModuleStyleReq serviceModuleStyleReq) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public List<ServiceModuleStyleResp> list(ArrayList<Long> serviceModuleId) {
                return null;
            }

            @Override
            public List<ServiceModuleStyleResp> listByStyleTemplateId(Long styleTemplateId) {
                return null;
            }
        };
    }
}
