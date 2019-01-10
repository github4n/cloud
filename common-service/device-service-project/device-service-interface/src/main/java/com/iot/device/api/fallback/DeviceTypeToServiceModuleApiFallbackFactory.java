package com.iot.device.api.fallback;

import com.iot.device.api.DeviceTypeToServiceModuleApi;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.DeviceTypeToServiceModuleResp;
import com.iot.device.vo.rsp.StyleTemplateResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceTypeToServiceModuleApiFallbackFactory implements FallbackFactory<DeviceTypeToServiceModuleApi> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeToServiceModuleApiFallbackFactory.class);


    @Override
    public DeviceTypeToServiceModuleApi create(Throwable throwable) {
        return new DeviceTypeToServiceModuleApi() {

            @Override
            public Long saveOrUpdate(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
                return null;
            }

            @Override
            public void saveMore(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {

            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public List<DeviceTypeToServiceModuleResp> listByDeviceTypeId(Long deviceTypeId, Long tenantId) {
                return null;
            }

            @Override
            public List<DeviceTypeToServiceModuleResp> listByServiceModuleId(Long serviceModuleId, Long tenantId) {
                return null;
            }

            @Override
            public void update(Long id, Integer status) {

            }


            @Override
            public boolean checkDeviceTypeHadIftttType(Long deviceTypeId) {
                return false;
            }

            @Override
            public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long deviceTypeId, String iftttType) {
                return null;
            }
        };
    }
}
