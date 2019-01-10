
package com.iot.device.api.fallback;

import com.iot.device.api.ProductReviewRecodApi;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductAuditApiFallbackFactory implements FallbackFactory<ProductReviewRecodApi> {

    @Override
    public ProductReviewRecodApi create(Throwable throwable) {
        return new ProductReviewRecodApi() {
            @Override
            public void submitAudit(
                    ProductReviewRecordReq req) {

            }

            @Override
            public List<ProductReviewRecordResp> getReviewRecord(Long productId) {
                return null;
            }

            @Override
            public Long getTenantIdById(Long id) {
                return null;
            }

            @Override
            public Long addRecord(ProductReviewRecordReq setServiceReviewReq) {
                return null;
            }
        };
    }
}
