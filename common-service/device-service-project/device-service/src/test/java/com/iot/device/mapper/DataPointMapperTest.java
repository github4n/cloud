package com.iot.device.mapper;

import com.iot.device.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:46 2018/4/23
 * @Modify by:
 */
public class DataPointMapperTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private DataPointMapper dataPointMapper;
    @Test
    public void selectDataPointsByDeviceTypeId() {

        dataPointMapper.selectDataPointsByProductId(2132423L);
    }
}