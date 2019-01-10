package com.iot.device.service;

import com.iot.device.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:25 2018/9/7
 * @Modify by:
 */
public class IDeviceStatusServiceTest extends BaseTest{

  @Autowired
  private IDeviceStatusService deviceStatusService;

  @Override
  public String getBaseUrl() {
    return null;
  }

  @Test
  public void addOrUpdateDeviceStatusByDeviceIdAndActiveStatus() {}

  @Test
  public void addOrUpdateDeviceOnlineStatusByDeviceId() {


  }

  @Test
  public void addDeviceStatusByDeviceId() {}
}