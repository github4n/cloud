package com.iot.user.service;

import com.iot.user.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:10 2018/7/30
 * @Modify by:
 */
public class BaseUserServiceTest extends BaseTest{

  @Autowired
  private BaseUserService baseUserService;
  @Override
  public String getBaseUrl() {
    return null;
  }

  @Test
  public void checkUserToken() {
    baseUserService.checkUserToken(null,"1793277b-d062-4064-8c45-9343b6f9972c","APP");
  }
}