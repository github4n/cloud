package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.DevelopInfoApi;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:16 2018/6/29
 * @Modify by:
 */
@Component
public class DevelopInfoApiFallbackFactory implements FallbackFactory<DevelopInfoApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopInfoApiFallbackFactory.class);

    @Override
    public DevelopInfoApi create(Throwable cause) {
        return new DevelopInfoApi() {
            @Override
            public Long addOrUpdateDevelopInfo(AddDevelopInfoReq infoReq) {
                return null;
            }

            @Override
            public List<DevelopInfoListResp> findDevelopInfoListAll() {
                return null;
            }

            @Override
            public Page<DevelopInfoListResp> findDevelopInfoPage(DevelopInfoPageReq pageReq) {
                return null;
            }
        };
    }
}