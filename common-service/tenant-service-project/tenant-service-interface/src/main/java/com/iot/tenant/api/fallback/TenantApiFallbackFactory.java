package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.req.*;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TenantApiFallbackFactory implements FallbackFactory<TenantApi> {

    @Override
    public TenantApi create(Throwable cause) {
        return new TenantApi() {


            @Override
            public Page<TenantInfoResp> list(GetTenantReq req) {
                return null;
            }

            @Override
            public Long save(SaveTenantReq req) {
                return null;
            }

            @Override
            public Boolean delete(List<Long> ids) {
                return false;
            }

            @Override
            public TenantInfoResp getTenantById(Long id) {
                return null;
            }

            @Override
            public List<TenantInfoResp> getTenantByIds(List<Long> ids){
            	return null;
            }
            
            @Override
            public TenantInfoResp getTenantByCode(String code) {
                return null;
            }

            @Override
            public List<TenantInfoResp> getTenantList() {
                return null;
            }

            @Override
            public Boolean saveAppPack(SaveAppPackReq req) {
                return false;
            }

            @Override
            public AppPackResp getAppPack(String code) {
                return null;
            }
            
            @Override
            public Page<TenantInfoResp> tenantAuditList(GetAuditTenantReq req) {
                return null;
            }
            
            @Override
            public void saveTenantReviewRecord(SaveTenantReviewRecordReq req){
            	
            }
            
            @Override
            public List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(Long tenantId){
            	return null;
            }

            @Override
            public List<Long> searchTenantIdsByName(String name) {
                return null;
            }

            @Override
            public void updateTenantCode(String newCode, String oldCode, Long tenantId) {

            }

        };
    }
}
