package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.AttrInfoBaseVo;
import com.iot.control.packagemanager.vo.req.IfAttrInfoReq;
import com.iot.control.packagemanager.vo.req.PropertyInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@description 获取if下的属性信息时，添加属性名称和参数类型
 *@author wucheng
 *@create 2018/12/11 14:25
 */
@ApiModel(value = "if设备属性详细")
@Data
public class IfAttrInfoResp extends AttrInfoBaseVo{

    @ApiModelProperty(name = "name", value="名称")
    private String name;

    @ApiModelProperty(name ="paramType", value ="参数类型")
    private Integer paramType;

    @ApiModelProperty(name = "eventProperties", value = "事件中的属性详情", dataType = "List")
    private List<PropertyInfoVoResp> eventProperties;

}
