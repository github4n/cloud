package com.iot.device.controller;

import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:34 2018/4/23
 * @Modify by:
 */
public class ProductControllerTest extends BaseTest {


    @Override
    public String getBaseUrl() {
        return "/product/";
    }


    @Test
    public void findProductListByDeviceTypeId() {

        Map<String, String> params = new HashMap<>();
        params.put("deviceTypeId", "-1018");
        mockGet("findProductListByDeviceTypeId", params);

    }

    @Test
    public void findDataPointListByProductId() {
    }

    @Test
    public void getProductById() {

        Map<String, String> params = new HashMap<>();
        params.put("id", "1090211072");
        mockGet("getProductById", params);

    }
}