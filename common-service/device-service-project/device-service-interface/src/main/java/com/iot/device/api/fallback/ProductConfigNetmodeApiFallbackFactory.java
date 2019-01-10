
package com.iot.device.api.fallback;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductConfigNetmodeApi;
import com.iot.device.vo.req.*;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.product.ProductAuditListResp;
import com.iot.device.vo.rsp.product.ProductMinimumSubsetResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class ProductConfigNetmodeApiFallbackFactory implements FallbackFactory<ProductConfigNetmodeApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductConfigNetmodeApiFallbackFactory.class);

	@Override
	public ProductConfigNetmodeApi create(Throwable cause) {
		return new ProductConfigNetmodeApi() {

			@Override
			public Long insert(ProductConfigNetmodeReq productConfigNetmodeReq) {
				return null;
			}

			@Override
			public void insertMore(List<ProductConfigNetmodeReq> productConfigNetmodeReqs) {

			}

			@Override
			public List<ProductConfigNetmodeRsp> listByProductId(Long productId) {
				return null;
			}

			@Override
			public void deleteMore(List ids) {

			}
		};
	}
}
