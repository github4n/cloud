package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.AppVersionApi;
import com.iot.tenant.vo.req.AppVersionListReq;
import com.iot.tenant.vo.req.CheckVersionRequest;
import com.iot.tenant.vo.req.SaveAppVersionReq;
import com.iot.tenant.vo.resp.AppVersionResp;
import com.iot.tenant.vo.resp.CheckVersionResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:31
 */
@Component
public class AppVersionApiFallbackFactory implements FallbackFactory<AppVersionApi> {


    @Override
    public AppVersionApi create(Throwable throwable) {
        return new AppVersionApi() {
            @Override
            public Page<AppVersionResp> list(AppVersionListReq req) {
                return null;
            }

            @Override
            public Long save(SaveAppVersionReq req) {
                return null;
            }

            @Override
            public AppVersionResp getLastVersion(Long appId) {
                return null;
            }

            @Override
            public CheckVersionResponse checkVersion(CheckVersionRequest req) {
                CheckVersionResponse resp = new CheckVersionResponse();
                resp.setResult(0);
                return resp;
            }
        };
    }
}
