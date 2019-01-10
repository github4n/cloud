package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.AirSwitchEventApi;
import com.iot.device.api.ProductDataPointApi;
import com.iot.device.api.ProductServiceInfoApi;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *@description 产品增值服务
 *@author wucheng
 *@create 2018/12/21 18:23
 */
public class ProductServiceInfoApiFallbackFactory implements FallbackFactory<ProductServiceInfoApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDataPointApiFallbackFactory.class);

    @Override
    public ProductServiceInfoApi create(Throwable cause) {
        return new ProductServiceInfoApi() {

            @Override
            public int saveProductServiceInfo(ProductServiceInfoReq req) {
                return 0;
            }

            @Override
            public int updateAuditStatusIsNull(Long tenantId, Long productId, Long serviceId) {
                return 0;
            }

            @Override
            public int updateAuditStatus(Long tenantId, Long productId, Long serviceId, Integer auditStatus) {
                return 0;
            }

            @Override
            public int deleteProductServiceInfo(Long tenantId, Long productId, Long serviceId) {
                return 0;
            }

            @Override
            public ProductServiceInfoResp getProductServiceInfo(Long tenantId, Long productId, Long serviceId) {
                return null;
            }

            @Override
            public List<ProductServiceInfoResp> getServiceInfoByProductId(Long tenantId, Long productId) {
                return null;
            }

            @Override
            public Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq) {
                return null;
            }
        };
    }
}
