package com.iot.device.api.fallback;

import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.req.device.UpdateProductReq;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class ProductCoreApiFallbackFactory implements FallbackFactory<ProductCoreApi> {

    @Override
    public ProductCoreApi create(Throwable cause) {
        return new ProductCoreApi() {
            @Override
            public List<ListProductRespVo> listProducts(ListProductInfoReq params) {
                return null;
            }

            @Override
            public GetProductInfoRespVo getByProductId(Long productId) {
                return null;
            }

            @Override
            public GetProductInfoRespVo getByProductModel(String productModel) {
                return null;
            }

            @Override
            public List<GetProductInfoRespVo> listByProductModel(Collection<String> productModelList) {
                return null;
            }

            @Override
            public void saveOrUpdate(UpdateProductReq params) {

            }

            @Override
            public void saveOrUpdateBatch(List<UpdateProductReq> paramsList) {

            }

            @Override
            public List<ListProductRespVo> listProductAll() {
                return null;
            }
        };
    }
}
