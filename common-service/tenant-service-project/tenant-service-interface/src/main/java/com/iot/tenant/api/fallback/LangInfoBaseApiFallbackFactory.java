package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.LangInfoBaseApi;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class LangInfoBaseApiFallbackFactory implements FallbackFactory<LangInfoBaseApi> {

    @Override
    public LangInfoBaseApi create(Throwable throwable) {
        return new LangInfoBaseApi() {
            @Override
            public void saveLangInfoBase(SaveLangInfoBaseReq req) {

            }

            @Override
            public void delLangInfoBase(DelLangInfoBaseReq req) {

            }

            @Override
            public void addLangInfoBase(SaveLangInfoBaseReq req) {

            }

            @Override
            public void updateLangInfoBase(SaveLangInfoBaseReq req) {

            }

            @Override
            public LangInfoBaseResp queryLangInfoBase(QueryLangInfoBaseReq queryLangInfoBaseReq) {
                return null;
            }

            @Override
            public Page<LangInfoBaseResp> pageQuery(QueryLangInfoBasePageReq pageReq) {
                return null;
            }
        };
    }
}
