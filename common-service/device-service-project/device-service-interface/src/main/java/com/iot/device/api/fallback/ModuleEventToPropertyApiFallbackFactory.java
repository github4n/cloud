package com.iot.device.api.fallback;

import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ModuleEventToPropertyApi;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:03 2018/7/2
 * @Modify by:
 */
@Component
public class ModuleEventToPropertyApiFallbackFactory implements FallbackFactory<ModuleEventToPropertyApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleEventToPropertyApiFallbackFactory.class);

    @Override
    public ModuleEventToPropertyApi create(Throwable cause) {
        return new ModuleEventToPropertyApi() {


            @Override
            public void save(ModuleEventToPropertyReq moduleEventToPropertyReq) {

            }

            @Override
            public void saveMore(ModuleEventToPropertyReq moduleEventToPropertyReq) {

            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }
        };
    }
}
