package com.iot.device.api.fallback;

import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
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
public class ModuleActionToPropertyApiFallbackFactory implements FallbackFactory<ModuleActionToPropertyApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleActionToPropertyApiFallbackFactory.class);

    @Override
    public ModuleActionToPropertyApi create(Throwable cause) {
        return new ModuleActionToPropertyApi() {


            @Override
            public void save(ModuleActionToPropertyReq moduleActionToPropertyReq) {

            }

            @Override
            public void saveMore(ModuleActionToPropertyReq moduleActionToPropertyReq) {

            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public List<ModuleActionToPropertyRsp> listByModuleActionIdAndModulePropertyId(Long moduleActionId, Long modulePropertyId) {
                return null;
            }
        };
    }
}
