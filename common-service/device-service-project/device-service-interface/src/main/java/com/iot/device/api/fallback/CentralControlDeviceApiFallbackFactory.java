
package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CentralControlDeviceApiFallbackFactory implements FallbackFactory<CentralControlDeviceApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CentralControlDeviceApiFallbackFactory.class);

    @Override
    public CentralControlDeviceApi create(Throwable cause) {
        return new CentralControlDeviceApi() {

            @Override
            public Page<DeviceResp> findDirectDevicePageToCenter(DevicePageReq pageReq) {
                return null;
            }

            @Override
            public Page<DeviceResp> findUnDirectDevicePage(DevicePageReq pageReq) {
                return null;
            }

            @Override
            public List<GetDeviceInfoRespVo> findDirectDeviceListByVenderCode(Long tenantId, Long locationId,String venderFlag,Integer isDirectDevice) {
                return null;
            }

            @Override
            public List<String> deleteDeviceByDeviceId(String deviceId) {
                return null;
            }

            @Override
            public List<DeviceResp> findAllUnDirectDeviceList(DevicePageReq pageReq) {
                return null;
            }

            @Override
            public List<DeviceResp> findUnDirectDeviceListByParentDeviceId(String parentDeviceId) {
                return null;
            }
            @Override
            public List<IftttDeviceResp> findIftttDeviceList(CommDeviceInfoReq req) {
                return null;
            }

            @Override
            public Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(DeviceBusinessTypeIDSwitchReq req) {
                return null;
            }

            @Override
            public List<DeviceResp> findDeviceByCondition(DeviceBusinessTypeIDSwitchReq req) {
                return null;
            }

            @Override
            public Page<DeviceResp> queryAirCondition(DevicePageReq pageReq) {
                return null;
            }

            @Override
            public List<GetDeviceInfoRespVo> selectAllDeviceToCenter(DevicePageReq pageReq) {
                return null;
            }

            @Override
            public List<GetDeviceInfoRespVo> findDirectDeviceByDeviceCatgory(String venderCode,Long tenantId,Long locationId) {
                return null;
            }

            @Override
            public GetDeviceInfoRespVo getDeviceByDeviceIp(Long orgId, Long tenantId, String deviceIp) {
                return null;
            }

            @Override
            public Map<String, Object> findDataReport(Long spaceId, String deviceId, String dateType, String deviceType) {
                return null;
            }

			@Override
			public Page<DeviceResp> getGatewayAndSubDeviceList(DevicePageReq pageReq) {
				return null;
			}

			@Override
			public List<Long> getExistProductList(PageDeviceInfoReq params) {
				return null;
			}

			@Override
			public List<GetDeviceInfoRespVo> getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq) {
				return null;
			}
        };
    }
}
