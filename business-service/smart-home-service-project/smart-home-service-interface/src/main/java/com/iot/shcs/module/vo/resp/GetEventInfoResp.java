package com.iot.shcs.module.vo.resp;

import com.iot.device.vo.rsp.ServiceModuleEventResp;
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
@ApiModel("功能模组事件信息")
public class GetEventInfoResp implements Serializable {

  @ApiModelProperty(value = "功能事件信息")
  private ServiceModuleEventResp eventInfo;

  @ApiModelProperty(value = "功能事件属性信息")
  private List<ServiceModulePropertyResp> propertyList;
}
