package com.iot.control.packagemanager.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 *@description 属性值类型是enum类型时，将字符串转为对象
 *@author wucheng
 *@create 2018/12/5 16:11
 */
@Data
public class PropertyEnumType {
    @ApiModelProperty(name ="name", value="名称")
    private String  name;

    @ApiModelProperty(name ="description", value="描述")
    private String  description;

    @ApiModelProperty(name ="value", value="值")
    private String  value;
}
