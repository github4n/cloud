
package com.iot.device.api.fallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iot.device.vo.rsp.product.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.req.GetProductReq;
import com.iot.device.vo.req.ProductInfoResp;
import com.iot.device.vo.req.ProductPageReq;
import com.iot.device.vo.req.ProductPublishHistoryReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.ProductStepVoReq;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.ProductPageResp;
import com.iot.device.vo.rsp.ProductPublishHistoryResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;

import feign.hystrix.FallbackFactory;


@Component
public class ProductApiFallbackFactory implements FallbackFactory<ProductApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductApiFallbackFactory.class);

	@Override
	public ProductApi create(Throwable cause) {
		return new ProductApi() {
			@Override
			public Long saveProduct(ProductReq product) {
				return 0L;
			}

			@Override
			public boolean deleteProductById(Long id) {
				return false;
			}

			@Override
			public ProductResp getProductById(Long id) {
				return null;
			}

			@Override
			public ProductInfoResp getProductInfoByProductId(Long productId) {
				return null;
			}

			@Override
			public ProductResp getProductByModel(String model) {
				return null;
			}

			@Override
			public boolean updateProduct(ProductReq product) {
				return false;
			}

			@Override
			public List<ProductResp> findProductListByDeviceTypeId(Long deviceTypeId) {
				return null;
			}

			@Override
			public ProductResp getProductByDeviceTypeIdAndProperties(Long deviceTypeId, List<Map<String, Object>> propertyList) {
				return null;
			}

			@Override
			public List<DataPointResp> findDataPointListByProductId(Long productId) {
				return null;
			}

			@Override
			public List<ProductResp> findProductListByTenantId(ProductReq product) {
				LOGGER.debug("Fallback, request={}", JSON.toJSONString(product));
				return Lists.newArrayList();
			}

			@Override
			public List<ProductResp> findPrimaryProductListByTenantId(ProductReq product) {
				return null;
			}

			@Override
			public List<ProductResp> findDirectProductListByTenantId(ProductReq product) {
				LOGGER.debug("Fallback, request={}", JSON.toJSONString(product));
				return Lists.newArrayList();
			}
			
			@Override
			public List<ProductResp> findAllDirectProductList() {
				return null;
			}

			@Override
			public ProductResp getProductByModelAndTenantId(String productModel, Long tenantId) {
				return null;
			}

			@Override
			public boolean saveDataPoint(Long productId, ArrayList<DataPointReq> reqs) {
				return false;
			}

			@Override
			public Page<ProductResp> findProductPageByTenantId(GetProductReq req) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
            public Page<ProductPageResp> findProductPage(ProductPageReq pageReq) {
				return null;
			}

            @Override
            public ProductResp findProductByDeviceTypeId(Long deviceTypeId) {
                return null;
            }

            @Override
            public Long addProduct(AddProductReq productReq) {
				LOGGER.info("addProduct error");

				return null;
			}

            @Override
            public Long updateProduct(AddProductReq productReq) {
                return null;
            }

			@Override
			public Long updateProductBaseInfo(AddProductReq productReq) {
				return null;
			}

			@Override
            public void updateDevelopInfoByProductId(Long enterpriseDevelopId, Long productId) {

            }

			@Override
			public void delProductByProductId(Long productId) {
				LOGGER.info("delProductByProductId hystrix error {}", productId);

			}

			@Override
			public Long copyProductByProductId(Long productId) {
				return null;
			}

			@Override
			public Page<ProductResp> getProduct(ProductOtaReq productOtaReq) {
				return null;
			}

			@Override
			public ProductResp getSomeProductPropertyById(Long id) {
				return null;
			}

			@Override
			public Integer getStepByProductId(Long productId) {
				return null;
			}

			@Override
			public void addProductStep(ProductStepVoReq stepVoReq) {

			}

			@Override
			public void confirmRelease(ProductConfirmReleaseReq req) {

			}
			
			@Override
			public void addProductPublish(ProductPublishHistoryReq req) {

			}
			
			@Override
			public List<ProductPublishHistoryResp> getProductPublishHis(Long productId, Long tenantId) {
				return null;
			}

			@Override
			public Page<ProductAuditListResp> queryProductAuditList(ProductAuditPageReq pageReq) {
				return null;
			}

			@Override
			public void reOpenAudit(ReopenAuditReq reopenAuditReq) {

			}

			@Override
			public Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq){
				return null;
			}

			@Override
			public List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(Long tenantId, Long communicationMode) {
				return null;
			}

			@Override
			public int updateServiceGooAndAlxAuditStatus(Long productId, int flag, Integer gooAuditStatus, Integer alxAuditStatus) {
				return 0;
			}
			
			/**
			 * 
			 * 描述：查询套包产品
			 * @author 李帅
			 * @created 2018年12月10日 下午3:42:03
			 * @since 
			 * @param tenantId
			 * @return
			 */
			@Override
			public List<PackageProductResp> getPackageProducts(Long tenantId) {
				return null;
			}
			
			/**
			 * 
			 * 描述：查询网管子产品
			 * @author 李帅
			 * @created 2018年12月10日 下午5:22:48
			 * @since 
			 * @param tenantId
			 * @param productId
			 * @return
			 */
			@Override
			public List<GatewayChildProductResp> getGatewayChildProducts(Long tenantId, Long productId) {
				return null;
			}

			@Override
			public List<PackageProductNameResp> getProductByIds(List<Long> ids) {
				return null;
			}
		};
	}
}
