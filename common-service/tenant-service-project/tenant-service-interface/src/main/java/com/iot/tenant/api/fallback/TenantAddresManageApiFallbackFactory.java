package com.iot.tenant.api.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iot.tenant.api.TenantAddresManageApi;
import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

import feign.hystrix.FallbackFactory;

@Component
public class TenantAddresManageApiFallbackFactory implements FallbackFactory<TenantAddresManageApi> {

    @Override
    public TenantAddresManageApi create(Throwable cause) {
        return new TenantAddresManageApi() {

            @Override
            public void save(TenantAddresManageReq req) {

            }

            @Override
            public void update(TenantAddresManageReq req) {

            }

            @Override
            public void delete(Long id, Long tenantId) {

            }

            @Override
            public List<TenantAddresManageResp> getAddresByTenantId(Long tenantId) {
                return null;
            }

        };
    }
}
