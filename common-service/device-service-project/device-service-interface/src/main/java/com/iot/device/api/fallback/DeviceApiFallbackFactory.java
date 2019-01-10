package com.iot.device.api.fallback;

import com.iot.device.api.DeviceApi;
import com.iot.device.vo.rsp.DeviceResp;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeviceApiFallbackFactory implements FallbackFactory<DeviceApi> {

    @Override
    public DeviceApi create(Throwable cause) {
        return new DeviceApi() {


            @Override
            public Map<String, Long> findUuidProductIdMap(List<String> uuIdList) {
                return null;
            }

            @Override
            public Map<String, Long> findUuidTenantIdMap(List<String> uuIdList) {
                return null;
            }

            @Override
            public List<DeviceResp> getVersionByDeviceIdList(List<String> deviceIdList) {
                return null;
            }

        };
    }
}
