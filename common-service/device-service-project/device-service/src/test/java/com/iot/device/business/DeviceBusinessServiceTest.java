package com.iot.device.business;

import com.iot.device.BaseTest;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 20:46 2018/12/4
 * @Modify by:
 */
public class DeviceBusinessServiceTest extends BaseTest {

  @Autowired
  private DeviceBusinessService deviceBusinessService;

  @Override
  public String getBaseUrl() {
    return null;
  }

  @Test
  public void saveOrUpdate() {

    deviceBusinessService.saveOrUpdate(UpdateDeviceInfoReq.builder()
            .uuid("xxxxxxxxxxx520")
            .productId(-1L)
            .tenantId(1L)
    .build());
  }

  @Test
  public void saveOrUpdateBatch() {}
}