package com.iot.device.api.fallback;

import com.iot.device.api.ServiceStyleToTemplateApi;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.ServiceStyleToTemplateResp;
import com.iot.device.vo.rsp.StyleTemplateResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceStyleToTemplateApiFallbackFactory implements FallbackFactory<ServiceStyleToTemplateApi> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStyleToTemplateApiFallbackFactory.class);


    @Override
    public ServiceStyleToTemplateApi create(Throwable throwable) {
        return new ServiceStyleToTemplateApi() {


            @Override
            public Long saveOrUpdate(ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
                return null;
            }

            @Override
            public void saveMore(ServiceStyleToTemplateReq serviceStyleToTemplateReq) {

            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public List<ServiceStyleToTemplateResp> list(ArrayList<Long> moduleStyleId) {
                return null;
            }
        };
    }
}
