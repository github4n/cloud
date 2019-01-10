package com.iot.device.api.fallback;

import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:03 2018/7/2
 * @Modify by:
 */
@Component
public class ProductToServiceModuleApiFallbackFactory implements FallbackFactory<ProductToServiceModuleApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductToServiceModuleApiFallbackFactory.class);

    @Override
    public ProductToServiceModuleApi create(Throwable cause) {
        return new ProductToServiceModuleApi() {


            @Override
            public void save(ProductToServiceModuleReq productToServiceModuleReq) {

            }

            @Override
            public void saveMore(ProductToServiceModuleReq productToServiceModuleReq) {

            }

            @Override
            public boolean checkProductHadIftttType(Long productId) {
                return false;
            }

            @Override
            public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long productId, String iftttType) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {



            }
        };
    }
}
