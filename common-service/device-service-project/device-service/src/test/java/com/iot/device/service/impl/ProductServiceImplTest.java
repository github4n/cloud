package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.GetProductReq;
import com.iot.device.vo.req.ProductPageReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:00 2018/5/11
 * @Modify by:
 */
public class ProductServiceImplTest extends BaseTest {

    @Autowired
    private IProductService productService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void findDataPointListByProductId() {
        productService.findDataPointListByProductId(2L);
    }

    @Test
    public void findProductPage() {
        ProductPageReq pageReq = new ProductPageReq();
        pageReq.setPageNum(1);
        pageReq.setPageSize(20);
        pageReq.setSearchValues("Light_RGBW");
        productService.findProductPage(pageReq);
    }

    @Test
    public void findProductPageByTenantId() {

        GetProductReq getProductReq = new GetProductReq();
        getProductReq.setPageNum(1);
        getProductReq.setPageSize(20);
        getProductReq.setTenantId(0L);
        getProductReq.setSearchValues("light");
        productService.findProductPageByTenantId(getProductReq);
    }
}