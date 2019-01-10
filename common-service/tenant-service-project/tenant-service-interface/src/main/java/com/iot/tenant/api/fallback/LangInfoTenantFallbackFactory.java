package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:18
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:18
 * 修改描述：
 */
@Component
public class LangInfoTenantFallbackFactory implements FallbackFactory<LangInfoTenantApi> {

    @Override
    public LangInfoTenantApi create(Throwable throwable) {
        return new LangInfoTenantApi() {

            @Override
            public void copyLangInfo(CopyLangInfoReq copyLangInfoReq) {

            }

            @Override
            public LangInfoTenantResp queryLangInfo(QueryLangInfoTenantReq queryLangInfoTenantReq) {
                return null;
            }

            @Override
            public void saveLangInfo(SaveLangInfoTenantReq req) {

            }

            @Override
            public int addLangInfo(SaveLangInfoTenantReq req) {
                return 0;
            }

            @Override
            public int deleteLangInfo(String objectType, String objectId, Long tenantId) {
                return 0;
            }

            @Override
            public Page<LangInfoTenantResp> pageQuery(QueryLangInfoTenantPageReq pageReq) {
                return null;
            }

            @Override
            public Map<String,  Map<String, String>> getAppLangInfo(Long appId, Long tenantId) {
                return null;
            }

            @Override
            public Map<String, Map<String, String>> getLangByProductIds(Long tenantId, List<Long> productIds) {
                return null;
            }

            @Override
            public List<LangInfoTenant> getLangInfoByProductIds(Long tenantId, List<Long> productIds) {
                return null;
            }

            @Override
            public int copyLangInfoTenant(CopyLangInfoReq copyLangInfoReq) {
                return 0;
            }
        };
    }
}
