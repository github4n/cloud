package com.iot.device.api.fallback;

import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.vo.req.device.ListDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeviceExtendsCoreApiFallbackFactory implements FallbackFactory<DeviceExtendsCoreApi> {

    @Override
    public DeviceExtendsCoreApi create(Throwable cause) {
        return new DeviceExtendsCoreApi() {
            @Override
            public List<ListDeviceExtendRespVo> listDeviceExtends(ListDeviceExtendReq params) {
                return null;
            }

            @Override
            public GetDeviceExtendInfoRespVo get(Long tenantId, String deviceId) {
                return null;
            }

            @Override
            public void saveOrUpdate(UpdateDeviceExtendReq params) {

            }

            @Override
            public void saveOrUpdateBatch(List<UpdateDeviceExtendReq> paramsList) {

            }
        };
    }
}
