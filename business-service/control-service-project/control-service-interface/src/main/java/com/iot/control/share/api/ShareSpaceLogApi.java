package com.iot.control.share.api;

import com.iot.control.share.api.fallback.ShareSpaceLogApiFallbackFactory;
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author lucky
 * @ClassName ShareSpaceLogApi
 * @Description share space log
 * @date 2019/1/3 9:45
 * @Version 1.0
 */
@Api("分享家log业务接口")
@FeignClient(value = "control-service", fallbackFactory = ShareSpaceLogApiFallbackFactory.class)
public interface ShareSpaceLogApi {
}
