package com.iot.control.space.service;

import com.iot.control.BaseTest;
import com.iot.control.space.vo.SpaceDeviceReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:03 2018/11/28
 * @Modify by:
 */
public class ISpaceDeviceServiceTest extends BaseTest {

  @Autowired
  private ISpaceDeviceService spaceDeviceService;

  @Override
  public String getBaseUrl() {
    return null;
  }

  @Test
  public void insertOrUpdateSpaceDeviceByDevId() {
    SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();

    spaceDeviceReq.setTenantId(2L);
    spaceDeviceReq.setDeviceId("eb9ea63f713c4fad10d4c901008d1500");
    spaceDeviceReq.setSpaceId(117L);
    spaceDeviceService.insertOrUpdateSpaceDeviceByDevId(spaceDeviceReq);
  }
}