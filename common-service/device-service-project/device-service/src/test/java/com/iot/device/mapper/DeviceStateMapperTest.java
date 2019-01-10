package com.iot.device.mapper;

import com.iot.device.BaseTest;
import com.iot.util.AssertUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:36 2018/4/25
 * @Modify by:
 */
public class DeviceStateMapperTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private DeviceStateMapper deviceStateMapper;

    @Test
    public void selectPropertyName() {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId",4325435654675687L);
        params.put("spaceId","54t54");
        List<Map<String,Object>> mapList = deviceStateMapper.selectPropertyName(params);
        AssertUtils.notEmpty(mapList,"not null");
    }

    @Test
    public void selectDataReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId",4325435654675687L);
        params.put("spaceId","54t54");
        List<Map<String,Object>> mapList = deviceStateMapper.selectDataReport(params);
        AssertUtils.notEmpty(mapList,"not null");
    }
}