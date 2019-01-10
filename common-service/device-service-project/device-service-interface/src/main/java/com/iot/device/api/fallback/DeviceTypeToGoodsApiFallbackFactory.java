package com.iot.device.api.fallback;

import com.iot.device.api.DeviceTypeToGoodsApi;
import com.iot.device.vo.rsp.DeviceTypeGoodsResp;
import com.iot.device.vo.rsp.ListGoodsSubDictResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author wangxi
 * @Description: DeviceTypeToGoodsApi熔断类工厂
 * @date 2018/12/2614:32
 */
@Component
public class DeviceTypeToGoodsApiFallbackFactory implements FallbackFactory<DeviceTypeToGoodsApi> {
    @Override
    public DeviceTypeToGoodsApi create(Throwable throwable) {
        return new DeviceTypeToGoodsApi() {

            @Override
            public List<String> getDeviceTypeGoodsCodeByDeviceTypeId(Long deviceTypeId) {
                return null;
            }

            @Override
            public Map<String, List<ListGoodsSubDictResp>> getAllGoodsSubDictMap() {
                return null;
            }

            @Override
            public Map<String, List<ListGoodsSubDictResp>> getGoodsSubDictMapByDeviceTypeId(Long deviceTypeId) {
                return null;
            }

            @Override
            public List<DeviceTypeGoodsResp> getConfigGoodsByDeviceTypeId(Long deviceTypeId) {
                return null;
            }
        };
    }
}
