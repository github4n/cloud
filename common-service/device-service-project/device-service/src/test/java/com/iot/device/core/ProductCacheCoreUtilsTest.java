package com.iot.device.core;

import com.iot.device.BaseTest;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.Product;
import org.junit.Test;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:00 2018/6/22
 * @Modify by:
 */
public class ProductCacheCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getCacheProductInfoByProductId() {
        Product product = ProductCacheCoreUtils.getCacheData(1001L, VersionEnum.V1);
        System.out.println(product);
    }

    @Test
    public void updateCacheProductInfo() {
        Product product = new Product();
        product.setId(1001L);
        product.setModel("xfz_model");
        product.setProductName("xfz_productName");
        ProductCacheCoreUtils.cacheData(1001L, product, VersionEnum.V1);
    }

    @Test
    public void updateCacheProductModel() {
        ProductCacheCoreUtils.cacheData("xfz_model", 1001L, VersionEnum.V1);
    }

    @Test
    public void getCacheProductInfoByProductModel() {
        Long productId = ProductCacheCoreUtils.getCacheData("lds.gateway.g151", VersionEnum.V1);
        System.out.println(productId);
    }

    @Test
    public void removeCacheProductInfoByProductId() {
        ProductCacheCoreUtils.removeData(1001L, VersionEnum.V1);
    }

    @Test
    public void removeCacheProductInfoByProductModel() {
        ProductCacheCoreUtils.removeData("xfz_model", VersionEnum.V1);
    }
}