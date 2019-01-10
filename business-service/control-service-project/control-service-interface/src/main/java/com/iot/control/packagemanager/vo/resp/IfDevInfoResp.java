package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.IfAttrInfoReq;
import com.iot.control.packagemanager.vo.req.IfDevInfoReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@description 获取if下设备信息时，添加设备名称
 *@author wucheng
 *@create 2018/12/11 8:35
 */
@Data
public class IfDevInfoResp {

    @ApiModelProperty(name = "id", value = "设备类型id或产品id", dataType = "Long" )
    private Long id;

    @ApiModelProperty(name = "idx", value = "执行顺序", dataType = "Long" )
    private Long idx;

    @ApiModelProperty(name = "attrLogic", value = "属性触发规则（and/or）", dataType = "String" )
    private String attrLogic;

    @ApiModelProperty(name = "attrInfo", value = "if参数信息", dataType = "List" )
    private List<IfAttrInfoResp> attrInfo;

    @ApiModelProperty(name = "devName", value="设备名称/产品名称")
    private String devName;
}
