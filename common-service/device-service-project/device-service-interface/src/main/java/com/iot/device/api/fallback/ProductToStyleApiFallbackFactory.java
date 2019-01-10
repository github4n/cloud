package com.iot.device.api.fallback;

import com.iot.device.api.ProductToStyleApi;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.ProductToStyleResp;
import com.iot.device.vo.rsp.StyleTemplateResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductToStyleApiFallbackFactory implements FallbackFactory<ProductToStyleApi> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductToStyleApiFallbackFactory.class);


    @Override
    public ProductToStyleApi create(Throwable throwable) {
        return new ProductToStyleApi() {


            @Override
            public Long saveOrUpdate(ProductToStyleReq productToStyleReq) {
                return null;
            }

            @Override
            public void delete(Long id) {

            }

            @Override
            public List<ProductToStyleResp> list(Long productId) {
                return null;
            }
        };
    }
}
