package com.iot.device.core.service;

import com.iot.device.BaseTest;
import com.iot.device.model.Product;
import org.junit.Test;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:50 2018/6/29
 * @Modify by:
 */
public class ProductServiceCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getProductByProductId() {
        ProductServiceCoreUtils.getProductByProductId(100009988L);
    }

    @Test
    public void getProductByProductModel() {
        Product product = ProductServiceCoreUtils.getProductByProductModel("lds.light.rgbw");
        System.out.println(product);
    }

    @Test
    public void removeCacheProduct() {
    }

    @Test
    public void cacheProductList() {
    }

    @Test
    public void cacheProduct() {
    }
}