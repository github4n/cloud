package com.iot.device.api.fallback;

import com.iot.device.api.ProductTimerApi;
import com.iot.device.vo.req.TimerConfigReq;
import com.iot.device.vo.rsp.ProductTimerResp;
import feign.hystrix.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时配置
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/23 11:27
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/23 11:27
 * 修改描述：
 */
public class ProductTimerApiFallbackFactory  implements FallbackFactory<ProductTimerApi> {

    @Override
    public ProductTimerApi create(Throwable throwable) {

        return new ProductTimerApi() {

            @Override
            public void delProductTimer(Long productId) {

            }

            @Override
            public void uptProductTimer(TimerConfigReq req) {

            }

            @Override
            public void addProductTimer(TimerConfigReq req) {

            }

            @Override
            public List<ProductTimerResp> getProductTimer(Long productId) {
                return null;
            }

            @Override
            public Map<Long, List<ProductTimerResp>> getProductTimers(List<Long> productIds) {
                return null;
            }
        };
    }
}
