package com.iot.user.api.fallback;

import com.iot.user.api.OnlineDebugApi;
import feign.hystrix.FallbackFactory;

public class OnlineDebugApiFallbackFactory implements FallbackFactory<OnlineDebugApi> {
    @Override
    public OnlineDebugApi create(Throwable cause) {
        return new OnlineDebugApi() {
            @Override
            public Boolean checkOnlineDebug(String uuid) {
                return false;
            }

            @Override
            public String updateOnlineDebug(String userName, int state, int tenantId) {
                return null;
            }

        };
    }
}
