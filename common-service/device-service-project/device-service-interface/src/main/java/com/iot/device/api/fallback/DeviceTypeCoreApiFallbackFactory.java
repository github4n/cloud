package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.vo.req.device.ListDeviceTypeReq;
import com.iot.device.vo.req.device.PageDeviceTypeByParamsReq;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeviceTypeCoreApiFallbackFactory implements FallbackFactory<DeviceTypeCoreApi> {

    @Override
    public DeviceTypeCoreApi create(Throwable cause) {
        return new DeviceTypeCoreApi() {
            @Override
            public GetDeviceTypeInfoRespVo get(Long deviceTypeId) {
                return null;
            }

            @Override
            public List<ListDeviceTypeRespVo> listDeviceType(ListDeviceTypeReq params) {
                return null;
            }

            @Override
            public Page<ListDeviceTypeRespVo> pageDeviceType(PageDeviceTypeByParamsReq params) {
                return null;
            }
        };
    }
}
