package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** @Author: lucky @Descrpiton: @Date: 15:34 2018/11/1 @Modify by: */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceConditionReq {

  // set value params
  private UpdateSetDeviceInfoReq setValueEntity;

  // where condition
  private UpdateWrapperDeviceReq wrapper;
}
