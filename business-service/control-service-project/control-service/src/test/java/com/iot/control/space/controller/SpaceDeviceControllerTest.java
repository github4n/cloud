package com.iot.control.space.controller;

import com.iot.control.BaseTest;
import com.iot.control.space.vo.SpaceDeviceReq;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:53 2018/11/28
 * @Modify by:
 */
public class SpaceDeviceControllerTest extends BaseTest{

  @Override
  public String getBaseUrl() {
    return "/spaceDevice/";
  }

  @Test
  public void inserSpaceDevice() {}

  @Test
  public void updateSpaceDevice() {}

  @Test
  public void insertOrUpdateSpaceDeviceByDevId() {

    SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();

    spaceDeviceReq.setTenantId(2L);
    spaceDeviceReq.setDeviceId("eb9ea63f713c4fad10d4c901008d1500");
    spaceDeviceReq.setSpaceId(117L);
    mockPost("insertOrUpdateSpaceDeviceByDevId",spaceDeviceReq);

  }
}