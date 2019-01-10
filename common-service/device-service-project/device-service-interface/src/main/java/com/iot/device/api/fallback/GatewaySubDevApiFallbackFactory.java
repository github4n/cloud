package com.iot.device.api.fallback;

import com.iot.device.api.GatewaySubDevRelationApi;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GatewaySubDevApiFallbackFactory implements FallbackFactory<GatewaySubDevRelationApi> {

    @Override
    public GatewaySubDevRelationApi create(Throwable arg0) {

        return new GatewaySubDevRelationApi() {
            @Override
            public int batchInsert(List<GatewaySubDevRelationReq> gatewaySubDevRelationReq) {
                return 0;
            }
            @Override
            public int deleteById(List<Long> ids) {
                return 0;
            }

            @Override
            public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(Long parDevId, Long tenantId) {
                return null;
            }

            @Override
            public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevIds(List parDevIds, Long tenantId) {
                return null;
            }

            @Override
            public List<Long> parentProductIds(Long productId, Long tenantId) {
                return null;
            }
        };
    }
}
