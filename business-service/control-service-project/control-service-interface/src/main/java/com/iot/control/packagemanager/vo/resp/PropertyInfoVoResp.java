package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.req.PropertyInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@description 属性详细信息
 *@author wucheng
 *@return
 */
@Data
public class PropertyInfoVoResp extends PropertyInfoVo{

    @ApiModelProperty(name ="name", value = "属性名称")
    private String name;

    @ApiModelProperty(name ="paramType", value = "参数类型")
    private Integer paramType;
}
