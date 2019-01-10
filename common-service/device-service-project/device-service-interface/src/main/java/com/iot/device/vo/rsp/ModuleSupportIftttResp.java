package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：支持ifttt模组数据返回
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 11:56
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 11:56
 * 修改描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "支持ifttt模组数据返回")
public class ModuleSupportIftttResp {

    @ApiModelProperty(name = "ifList", value = "if配置项", dataType = "List")
    private List<ModuleIftttDataResp> ifList;

    @ApiModelProperty(name = "thenList", value = "then配置项", dataType = "List")
    private List<ModuleIftttDataResp> thenList;
}
