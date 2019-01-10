package com.iot.control.share.api.fallback;

import com.iot.control.share.api.ShareSpaceLogApi;
import feign.hystrix.FallbackFactory;

/**
 * @author lucky
 * @ClassName ShareSpaceLogApiFallbackFactory
 * @Description TODO
 * @date 2019/1/3 9:46
 * @Version 1.0
 */
public class ShareSpaceLogApiFallbackFactory implements FallbackFactory<ShareSpaceLogApi> {

    @Override
    public ShareSpaceLogApi create(Throwable cause) {
        return new ShareSpaceLogApi() {

        };
    }
}