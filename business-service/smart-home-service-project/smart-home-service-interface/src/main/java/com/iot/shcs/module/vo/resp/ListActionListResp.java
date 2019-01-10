package com.iot.shcs.module.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/** @Author: xfz @Descrpiton: @Date: 11:31 2018/7/3 @Modify by: */
@Data
@ToString
@ApiModel("功能组方法信息")
public class ListActionListResp extends GetActionInfoResp implements Serializable {

  // 是否被选中 默认未选中
  @ApiModelProperty(value = "设备类型id", allowableValues = "true 选中 ,false 未选中")
  private boolean whetherCheck = false;
}
