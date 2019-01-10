package com.iot.device.api.fallback;

import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeviceStateCoreApiFallbackFactory implements FallbackFactory<DeviceStateCoreApi> {

    @Override
    public DeviceStateCoreApi create(Throwable cause) {
        return new DeviceStateCoreApi() {
            @Override
            public Map<String, Map<String, Object>> listStates(ListDeviceStateReq params) {
                return null;
            }

            @Override
            public Map<String, Object> get(Long tenantId, String deviceId) {
                return null;
            }

            @Override
            public void saveOrUpdate(UpdateDeviceStateReq params) {

            }

            @Override
            public void saveOrUpdateBatch(List<UpdateDeviceStateReq> params) {

            }

            @Override
            public void recoveryDefaultState(Long tenantId, String deviceId) {

            }

            @Override
            public void runSaveStateCacheTask() {

            }

            @Override
            public void testSaveStateTask() {
                
            }
        };
    }
}
