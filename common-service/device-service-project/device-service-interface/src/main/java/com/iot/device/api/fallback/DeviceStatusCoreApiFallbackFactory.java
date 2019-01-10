package com.iot.device.api.fallback;

import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeviceStatusCoreApiFallbackFactory implements FallbackFactory<DeviceStatusCoreApi> {

    @Override
    public DeviceStatusCoreApi create(Throwable cause) {
        return new DeviceStatusCoreApi() {
            @Override
            public List<ListDeviceStatusRespVo> listDeviceStatus(ListDeviceStateReq params) {
                return null;
            }

            @Override
            public GetDeviceStatusInfoRespVo get(Long tenantId, String deviceId) {
                return null;
            }

            @Override
            public void saveOrUpdate(UpdateDeviceStatusReq params) {

            }

            @Override
            public void saveOrUpdateBatch(List<UpdateDeviceStatusReq> paramsList) {

            }
        };
    }
}
