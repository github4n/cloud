package com.iot.shcs.module.vo.resp;

import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/** @Author: xfz @Descrpiton: @Date: 18:05 2018/7/3 @Modify by: */
@Data
@ToString
@ApiModel("功能组方法基础信息")
public class GetActionInfoResp implements Serializable {

  @ApiModelProperty(value = "功能组方法信息")
  private ServiceModuleActionResp actionInfo;

  // 请求参数类型 array  object
  @ApiModelProperty(value = "方法事件请求属性信息")
  private List<ServiceModulePropertyResp> paramPropertyList;

  // 返回参数类型 array object
  @ApiModelProperty(value = "方法事件返回属性信息")
  private List<ServiceModulePropertyResp> returnPropertyList;
}
