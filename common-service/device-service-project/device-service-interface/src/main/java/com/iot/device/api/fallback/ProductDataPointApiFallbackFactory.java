
package com.iot.device.api.fallback;

import com.iot.device.api.ProductDataPointApi;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ProductDataPointApiFallbackFactory implements FallbackFactory<ProductDataPointApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDataPointApiFallbackFactory.class);

	@Override
	public ProductDataPointApi create(Throwable cause) {
		return new ProductDataPointApi() {


		};
	}
}
