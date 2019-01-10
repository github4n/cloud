package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.AttrInfoBaseVo;
import com.iot.control.packagemanager.vo.req.PropertyInfoVo;
import com.iot.control.packagemanager.vo.req.ThenAttrInfoReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@description 返回then时，添加起参数类型和设备名称
 *@author wucheng
 *@create 2018/12/11 14:29
 */
@Data
public class ThenAttrInfoResp extends AttrInfoBaseVo {

    @ApiModelProperty(name = "actionProperties", value = "方法中的属性详情", dataType = "List")
    private List<PropertyInfoVoResp> actionProperties;

    @ApiModelProperty(name = "name", value="名称")
    private String name;

    @ApiModelProperty(name ="paramType", value ="参数类型")
    private Integer paramType;
}
