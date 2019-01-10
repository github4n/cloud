package com.iot.device.web;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.List;

public class ProductCoreControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/productCore/";
    }

    @Test
    public void listByProductModel() {
        List<String> modelList = Lists.newArrayList("lds.magnet.zbdwsensors0000", "lds.light.zllcolortemperature", "lds.light.zhadimmablelight"
                , "lds.light.zbtextendedcolor");
        mockPost("listByProductModel", modelList);
    }
}