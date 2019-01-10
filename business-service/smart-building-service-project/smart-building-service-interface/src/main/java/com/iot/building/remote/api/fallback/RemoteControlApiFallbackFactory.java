package com.iot.building.remote.api.fallback;


import org.springframework.stereotype.Component;

import com.iot.building.remote.api.RemoteControlApi;

import feign.hystrix.FallbackFactory;


/**
 * @Author: linjihuang
 * @Date: 2018/10/12
 * @Description: *
 */
@Component
public class RemoteControlApiFallbackFactory implements FallbackFactory<RemoteControlApi> {


    @Override
    public RemoteControlApi create(Throwable throwable) {
        return new RemoteControlApi() {

			@Override
			public void synchronousRemoteControl(Long tenantId, Long spaceId) {
			}

        };
    }
}
