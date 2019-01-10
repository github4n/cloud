package com.iot.tenant.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.tenant.api.AppVersionLogApi;
import com.iot.tenant.vo.req.AppVersionLogReq;
import com.iot.tenant.vo.resp.AppVersionLogResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:31
 */
@Component
public class AppVersionLogApiFallbackFactory implements FallbackFactory<AppVersionLogApi> {


    @Override
    public AppVersionLogApi create(Throwable throwable) {
        return new AppVersionLogApi() {

            @Override
            public Long insertOrUpdate(AppVersionLogReq appVersionLogReq) {
                return null;
            }

            @Override
            public AppVersionLogResp versionLogByKey(String systemInfo, String appPackage,String version) {
                return null;
            }

            @Override
            public PageInfo page(AppVersionLogReq appVersionLogReq) {
                return null;
            }

            @Override
            public void delete(List<Long> ids) {

            }

        };
    }
}
