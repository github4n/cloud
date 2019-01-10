package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.ThenAttrInfoReq;
import com.iot.control.packagemanager.vo.req.ThenDevInfoReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@description 返回设备时，添加设备名称
 *@author wucheng
 *@create 2018/12/11 8:44
 */
@Data
public class ThenDevInfoResp {

    @ApiModelProperty(name = "id", value = "设备类型id或产品id", dataType = "Long" )
    private Long id;

    @ApiModelProperty(name = "idx", value = "执行顺序", dataType = "Long" )
    private Long idx;

    @ApiModelProperty(name = "attrInfo", value = "then参数信息", dataType = "List" )
    private List<ThenAttrInfoResp> attrInfo;

    @ApiModelProperty(name = "devName", value="设备名称")
    private String devName;
}
